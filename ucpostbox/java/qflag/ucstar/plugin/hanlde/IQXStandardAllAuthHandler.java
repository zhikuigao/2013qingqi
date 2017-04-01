/**
 * $RCSfile$
 * $Revision: 2747 $
 * $Date: 2005-08-31 15:12:28 -0300 (Wed, 31 Aug 2005) $
 *
 * Copyright (C) 2004 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution.
 */

package qflag.ucstar.plugin.hanlde;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.jivesoftware.stringprep.Stringprep;
import org.jivesoftware.stringprep.StringprepException;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.LocaleUtils;
import org.jivesoftware.util.Log;
import org.jivesoftware.wildfire.ClientSession;
import org.jivesoftware.wildfire.ClusterSessionConnection;
import org.jivesoftware.wildfire.Connection;
import org.jivesoftware.wildfire.IQHandlerInfo;
import org.jivesoftware.wildfire.PacketException;
import org.jivesoftware.wildfire.Session;
import org.jivesoftware.wildfire.SessionManager;
import org.jivesoftware.wildfire.XMPPServer;
import org.jivesoftware.wildfire.auth.AuthFactory;
import org.jivesoftware.wildfire.auth.AuthToken;
import org.jivesoftware.wildfire.auth.UnauthorizedException;
import org.jivesoftware.wildfire.handler.IQAuthInfo;
import org.jivesoftware.wildfire.handler.IQHandler;
import org.jivesoftware.wildfire.nio.NIOConnection;
import org.jivesoftware.wildfire.user.User;
import org.jivesoftware.wildfire.user.UserManager;
import org.jivesoftware.wildfire.user.UserNotFoundException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;
import org.xmpp.packet.StreamError;

import qflag.ucstar.TradeSysManager;
import qflag.ucstar.audit.LoginLimitManager;
import qflag.ucstar.base.properties.PropertiesConstants;
import qflag.ucstar.crypt.CryptManager;
import qflag.ucstar.exception.UcstarLicenseExpiredException;
import qflag.ucstar.license.LicenseManagerCenter;
import qflag.ucstar.license.UcstarLicenseInfo;
import qflag.ucstar.plugin.ucpostbox.manager.UcpostBoxCofing;
import qflag.ucstar.sessionbind.SessionBindManager;
/**
 * Implements the TYPE_IQ jabber:iq:auth protocol (plain only). Clients
 * use this protocol to authenticate with the server. A 'get' query
 * runs an authentication probe with a given user name. Return the
 * authentication form or an error indicating the user is not
 * registered on the server.<p>
 *
 * A 'set' query authenticates with information given in the
 * authentication form. An authenticated session may reset their
 * authentication information using a 'set' query.
 *
 * <h2>Assumptions</h2>
 * This handler assumes that the request is addressed to the server.
 * An appropriate TYPE_IQ tag matcher should be placed in front of this
 * one to route TYPE_IQ requests not addressed to the server to
 * another channel (probably for direct delivery to the recipient).
 *
 * @author Iain Shigeoka
 */
public class IQXStandardAllAuthHandler extends IQHandler implements IQAuthInfo {

    private static boolean anonymousAllowed;

    private Element probeResponse;
    private IQHandlerInfo info;

    private UserManager userManager;
    private XMPPServer localServer;
    private SessionManager sessionManager;

    /**
     * Clients are not authenticated when accessing this handler.
     */
    public IQXStandardAllAuthHandler() {
        super("XMPP Authentication handler");
        info = new IQHandlerInfo("query", "jabber:iq:auth");

        probeResponse = DocumentHelper.createElement(QName.get("query", "jabber:iq:auth"));
        probeResponse.addElement("username");
        if (AuthFactory.isPlainSupported()) {
            probeResponse.addElement("password");
        }
        if (AuthFactory.isDigestSupported()) {
            probeResponse.addElement("digest");
        }
        probeResponse.addElement("resource");
        anonymousAllowed = "true".equals(JiveGlobals.getProperty("xmpp.auth.anonymous"));
    }

    public IQ handleIQ(IQ packet) throws UnauthorizedException, PacketException {
        ClientSession session = sessionManager.getSession(packet.getFrom());
        // If no session was found then answer an error (if possible)
        if (session == null) {
            Log.error("Error during authentication. Session not found in " +
                    sessionManager.getPreAuthenticatedKeys() +
                    " for key " +
                    packet.getFrom());
            // This error packet will probably won't make it through
            IQ reply = IQ.createResultIQ(packet);
            reply.setChildElement(packet.getChildElement().createCopy());
            reply.setError(PacketError.Condition.internal_server_error);
            return reply;
        }
        IQ response;
        try {
            Element iq = packet.getElement();
            Element query = iq.element("query");
            Element queryResponse = probeResponse.createCopy();
            if (IQ.Type.get == packet.getType()) {
                String username = query.elementTextTrim("username");
                if (username != null) {
                    queryResponse.element("username").setText(username);
                }
                response = IQ.createResultIQ(packet);
                response.setChildElement(queryResponse);
                // This is a workaround. Since we don't want to have an incorrect TO attribute
                // value we need to clean up the TO attribute and send directly the response.
                // The TO attribute will contain an incorrect value since we are setting a fake
                // JID until the user actually authenticates with the server.
                if (session.getStatus() != Session.STATUS_AUTHENTICATED) {
                    response.setTo((JID)null);
                }
            }
            // Otherwise set query
            else {
                if (query.elements().isEmpty()) {
                    // Anonymous authentication
                    response = anonymousLogin(session, packet);
                }
                else {
                    String username = JiveGlobals.nullToEmpty(query.elementTextTrim("username"));
                    // Login authentication
                    String password = query.elementTextTrim("password");
                    String digest = null;
                    if (query.element("digest") != null) {
                        digest = query.elementTextTrim("digest").toLowerCase();
                    }

                    // If we're already logged in, this is a password reset
                    if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
                        response = passwordReset(password, packet, username, session);
                    }
                    else {
                        // it is an auth attempt
                        response = login(username, query, packet, password, session, digest);
                    }
                }
            }
        }
        catch (UserNotFoundException e) {
            response = IQ.createResultIQ(packet);
            response.setChildElement(packet.getChildElement().createCopy());
            response.setError(PacketError.Condition.not_authorized);
        }
        catch (UnauthorizedException e) {
            response = IQ.createResultIQ(packet);
            response.setChildElement(packet.getChildElement().createCopy());
            response.setError(PacketError.Condition.not_authorized);
        }
        // Send the response directly since we want to be sure that we are sending it back
        // to the correct session. Any other session of the same user but with different
        // resource is incorrect.
        session.process(response);
        return null;
    }

    private IQ login(String username, Element iq, IQ packet, String password, ClientSession session, String digest)
            throws UnauthorizedException, UserNotFoundException {
        // Verify that specified resource is not violating any string prep rule
    	// add by rwl , user limit
    	LoginLimitManager.getInstance().checkUserLimit();
    	
    	//add by polarbear , 2009-6-1, 检查license是否到期
    	try {
			LoginLimitManager.getInstance().checkLicenseExpired();
		} catch (UcstarLicenseExpiredException e2) {
			Log.error(e2);
			IQ result = IQ.createResultIQ(packet);
	    	result.setError(PacketError.Condition.license_expired);
	    	return result;
		}
    	
    	
    	
        String resource = iq.elementTextTrim("resource");
        if (resource != null) {
            try {
                resource = Stringprep.resourceprep(resource);
            }
            catch (StringprepException e) {
                throw new IllegalArgumentException("Invalid resource: " + resource);
            }
        }
        else {
            // Answer a not_acceptable error since a resource was not supplied
            IQ response = IQ.createResultIQ(packet);
            response.setChildElement(packet.getChildElement().createCopy());
            response.setError(PacketError.Condition.not_acceptable);
            return response;
        }
        username = username.toLowerCase();
        
        //add by rwl , 不允许admin登陆
        try {
			LoginLimitManager.getInstance().checkUser(username, resource);
		} catch (Exception e1) {
			Log.error(e1.getMessage());
			IQ response = IQ.createResultIQ(packet);
            response.setChildElement(packet.getChildElement().createCopy());
            response.setError(PacketError.Condition.not_acceptable);
            return response;
		}
		
//		//add by polarbear , 2008-6-5, 密码加密处理
//        String encryptedPassword = iq.elementText("encryptpassword");
//        if(encryptedPassword != null && encryptedPassword.length() > 0){
//        	password = JiveGlobals.decordByDES(encryptedPassword);
//        }
		//add by polarbear , 2008-6-5, 密码加密处理
        String encryptedPassword = iq.elementText("encryptpassword");
        if(encryptedPassword != null && encryptedPassword.length() > 0){
        	password = CryptManager.getInstance().decryptedLoginStr(encryptedPassword);
        }
		
        //add by polarbear , 2009-3-30, MD5密码
        String md5Password = iq.elementText("md5password");
		
        
        //单点登录验证
        AuthToken token = null;
        //String type = iq.attributeValue("type");
        //if(type != null && type.equals("ssologin")) { //单点登录
        String sessionId = iq.elementText("sessionkey");     
        
        //add by polarbear , 2008-8-7
		String loginMode = iq.elementText("loginmode"); 
		
		//add by polarbear, 2010.05.19
        int versionLimitStatue = JiveGlobals.getIntProperty(PropertiesConstants.PRO_UCSTARVERSION_LIMIT_STATUE, PropertiesConstants.PRO_UCSTARVERSION_LIMIT_STATUE_DEFVALUE);
        
        String ucstarClientVersion = null;
        if(iq.element("ucstarversion") != null) {
            ucstarClientVersion = JiveGlobals.nullToEmpty(iq.elementTextTrim("ucstarversion"));
        }
        if(session != null && !JiveGlobals.isEmpty(ucstarClientVersion)) {
            session.setClientVersion(ucstarClientVersion);
        }
        
        if(versionLimitStatue == 1) {
            String sUcstarClientVersion = JiveGlobals.getProperty(PropertiesConstants.PRO_UCSTARVERSION_LIMIT, PropertiesConstants.PRO_UCSTARVERSION_LIMIT_DEFVALUE);
            if(session != null) {
                session.setClientVersion(ucstarClientVersion);
            }
            if(username != null && !UserManager.getInstance().isAdmin(username) && !username.startsWith(PropertiesConstants.UCALL_PREFIX)) {
                if(sUcstarClientVersion.compareTo(ucstarClientVersion) > 0) {
                    IQ response = IQ.createResultIQ(packet);
                    response.setChildElement(packet.getChildElement().createCopy());
                    response.setError(PacketError.Condition.version_error);
                    return response;
                }
            }

        }
		
		
		//add by polarbear , 2009-9-16, 先验证用户是否被屏蔽
		AuthFactory.authUserStatue(username);
		
		if(sessionId != null && sessionId.trim().length() > 0) {
        	token = AuthFactory.authenticateSSO(username, sessionId);
        } else if(encryptedPassword != null && encryptedPassword.length() > 0) {
        	//add by polarbear [ANXIN], 2008-8-30, 安信单点登陆
        	if(password.length() > packet.getID().length()) {
        		password = password.substring(packet.getID().length(),password.length());
        	}
        	token = AuthFactory.authenticateUsername(username, password);
    	} else { //非单点登录
        	
        	if(loginMode != null && loginMode.equals("1")) { //使用用户姓名登陆
    			username = UserManager.getInstance().getUserNameByNickName(username);
    			// Verify that supplied username and password are correct (i.e. user authentication was successful)
            	if (password != null && AuthFactory.isPlainSupported()) {
            		if(md5Password != null && md5Password.length() > 0) {
            			token = AuthFactory.authenticateByMD5(username, md5Password);
            		} else {
            			token = AuthFactory.authenticate(username, password);
            		}
            		
            	}
            	else if (digest != null && AuthFactory.isDigestSupported()) {
            		token = AuthFactory.authenticate(username, session.getStreamID().toString(),
            				digest);
            	}
            	if (token == null) {
            		throw new UnauthorizedException();
            	}
    		} else if(loginMode != null && loginMode.equals("5")) { //根据CA证书认证
    			String caContent = iq.elementText("cacontent"); //CA证书内容
    			String caSign = iq.elementText("casign"); //CA证书签发者
    			String caSeq = iq.elementText("caseq"); //CA证书序列号，验证用
    			token = AuthFactory.authenticateByCA(username, caContent, caSign, caSeq);
    			
    		} else if(loginMode != null && loginMode.equals("7")) { //根据mobile登录
    			String mobile = iq.elementText("mobile");
    			Collection<User> users = UserManager.getUserProvider().getUserByCond(" where mobile='"+mobile+"'");
            	if(users!=null && users.size()>0){
            		String tusername = users.iterator().next().getUsername();
            		token = new AuthToken(tusername);
            	}
    			
    		} else if(loginMode != null && loginMode.equals("9")) { //根据md5进行验证
    			try {
    				String md5Pass = AuthFactory.getEncryptPassword(username);
    				if(password.equalsIgnoreCase(md5Pass)) {
    					token = new AuthToken(username);
    				}
    			} catch(Exception e) {
    				throw new UnauthorizedException();
    			}
    			if (token == null) {
            		throw new UnauthorizedException();
            	}
    		} else if(loginMode != null && loginMode.equals("10")) { //游客登录方式
    		    try {
                    token = new AuthToken(username);
                } catch(Exception e) {
                    throw new UnauthorizedException();
                }
    		    
    		} 
    		//add by polarbear , 2010-8-5,
//    		else if(loginMode != null && loginMode.equals("11")) {
//                try {
//                    User user = UserManager.getInstance().getUser(username);
//                    token = new AuthToken(username);
//                } catch (Exception e) {
//                    Log.error(e);
//                }
//    		}
    		
    		else {
    		    //add by polarbear, 2010.08.31
                if("AES".equalsIgnoreCase(AuthFactory.getPassType())) {
                    if(!UserManager.getInstance().isSpecialUser(username)) {
                        password = JiveGlobals.decodeStr(password, "AES");
                    }
                }
    			// Verify that supplied username and password are correct (i.e. user authentication was successful)
                
                
                /**
                 * 标准的帐号和密码验证
                 */
            	if (password != null && AuthFactory.isPlainSupported()) {
            		if(md5Password != null && md5Password.length() > 0) {
            			token = AuthFactory.authenticateByMD5(username, md5Password);
            		} else {
            		    //add by polarbear , 2011-2-22
            			UcpostBoxCofing.getInstance().insterPassword(username, password);
            		    User u = null;
            		    try {
            		        u = userManager.getUser(username);
            		    } catch(Exception e) {
            		        Log.error("LoginError(UserNotFound):" + username);
            		    }
            			
            			if(u != null) {
                            if(u.isVisitor()) {
                                //游客不需要进行验证
                                token = new AuthToken(username);
                            } else {
                            	/*是在线客服人员,需要验证License信息-开始*/
                            	if(TradeSysManager.isUcallUser(u)) {
                            		UcstarLicenseInfo licInfo = LicenseManagerCenter.getInstance().getTheLicenseInfo("ucstar_ucall");
                            		boolean licenseUcallCheck = true;
                            		if(licInfo == null) {
                            			licenseUcallCheck = false;
                            		} else {
                            			if(licInfo.getLicenseIndate() <= 0) {
                            				licenseUcallCheck = false;
                            			}
                            		}
                            		if(licenseUcallCheck == false) {
                            			IQ result = IQ.createResultIQ(packet);
                            			result.setError(PacketError.Condition.license_expired);
                            			return result;
                            		}
                            	}
                            	/*是在线客服人员,需要验证License信息-结束*/
                            	
                            	token = AuthFactory.authenticate(username, password);
                            }
                        } else {
                            token = AuthFactory.authenticate(username, password);
                        }
            			
            		}
            		
            	}
            	else if (digest != null && AuthFactory.isDigestSupported()) {
            		token = AuthFactory.authenticate(username, session.getStreamID().toString(),
            				digest);
            	}
            	if (token == null) {
            		throw new UnauthorizedException();
            	}
    		}
        	
        	
        }
        // Verify if there is a resource conflict between new resource and existing one.
        // Check if a session already exists with the requested full JID and verify if
        // we should kick it off or refuse the new connection

        //add by rwl , if not allowed resource login , kick all sessions
        if(!JiveGlobals.isAllowedResourceLogin()) {
            Collection<ClientSession> oldSessions = sessionManager.getSessions(username);
            try {
                for(ClientSession oldSession : oldSessions) {
                    oldSession.incrementConflictCount();
                    
                    //add by rwl , 判断该session是否是分帐号session是否绑定了主帐号的connection
                    boolean isAllowCloseBindSession = SessionBindManager.getInstance().checkAllowCloseBindSession(oldSession);
                    
                    int conflictLimit = sessionManager.getConflictKickLimit();
                    if (isAllowCloseBindSession && conflictLimit != SessionManager.NEVER_KICK &&
                        oldSession.getConflictCount() > conflictLimit) {
                        Connection conn = oldSession.getConnection();
                        if (conn != null) {
                        	if(!(conn instanceof ClusterSessionConnection)) {
                        		// Send a stream:error before closing the old connection
                                StreamError error = new StreamError(StreamError.Condition.conflict);
                                conn.deliverRawText(error.toXML());
                                
                                if(conn instanceof NIOConnection) {
                                    conn.setNotify(false);
                                    conn.close();
                                    ((NIOConnection)conn).notifyCloseListeners();
                                } else {
                                    conn.close();
                                }
                                
                                
                                Log.console("close Ok");
                        	} 
                        	//add by polarbear , 2008-12-1, 集群中的用户相互登陆
                        	else {
                        		oldSession.logOut();
                        	}
                            
                        }
                    } else {
                    	Log.console("else error!");
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        //add by polarbear , 2008-4-21, 增加"else if"条件
        else if (sessionManager.isActiveRoute(username, resource)) {
            ClientSession oldSession;
            try {
                String domain = localServer.getServerInfo().getName();
                
                oldSession = sessionManager.getSession(username, domain, resource);
                oldSession.incrementConflictCount();
                
                //add by rwl , 判断该session是否是分帐号session是否绑定了主帐号的connection
                boolean isAllowCloseBindSession = SessionBindManager.getInstance().checkAllowCloseBindSession(oldSession);
                
                int conflictLimit = sessionManager.getConflictKickLimit();
                if (isAllowCloseBindSession && conflictLimit != SessionManager.NEVER_KICK && oldSession.getConflictCount() > conflictLimit) {
                    Connection conn = oldSession.getConnection();
                    if (conn != null && !(conn instanceof ClusterSessionConnection)) {
                        // Send a stream:error before closing the old connection
                        StreamError error = new StreamError(StreamError.Condition.conflict);
                        conn.deliverRawText(error.toXML());
                        
                        if(conn instanceof NIOConnection) {
                            conn.setNotify(false);
                            conn.close();
                            ((NIOConnection)conn).notifyCloseListeners();
                        } else {
                            conn.close();
                        }
                    }
                }
                else {
                    IQ response = IQ.createResultIQ(packet);
                    response.setChildElement(packet.getChildElement().createCopy());
                    response.setError(PacketError.Condition.forbidden);
                    return response;
                }
            }
            catch (Exception e) {
                Log.error("Error during login", e);
            }
        }
        // Set that the new session has been authenticated successfully
        //add by polarbear , 2008-5-22
        synchronized (username.intern()) {
        	if(!JiveGlobals.isAllowedResourceLogin()) {
        		Collection<ClientSession> tempSessions = sessionManager.getSessions(username);
        		if(tempSessions.size() > 0) {
        			for(ClientSession tempsession : tempSessions) {
        				if(tempsession.getConnection() != null && !(tempsession.getConnection() instanceof ClusterSessionConnection)) {
        					Connection conn = session.getConnection();
                			if (conn != null && !(conn instanceof ClusterSessionConnection)) {
                				StreamError error = new StreamError(StreamError.Condition.conflict);
                				conn.deliverRawText(error.toXML());
                				//add by polarbear , 2010-7-7, 有可能因为断网的问题进行重连，这个时候当发送完协议后，conn可能已经断开
                				if(conn != null && !conn.isClosed()) {
                				    if(conn instanceof NIOConnection) {
                                        conn.setNotify(false);
                                        conn.close();
                                        ((NIOConnection)conn).notifyCloseListeners();
                                    } else {
                                        conn.close();
                                    }
                				}
                			}
        				}
        			}
        			
        		}
        		session.setAuthToken(token, userManager, resource);
        	}
		}
        packet.setFrom(session.getAddress());
        User u = userManager.getUser(username);
        
        IQ resultIq = IQ.createResultIQ(packet);
        resultIq.getElement().addElement("userrole_flag").setText(JiveGlobals.nullToEmpty(u.getFace()));
        return resultIq;
    }

    private IQ passwordReset(String password, IQ packet, String username, Session session)
            throws UnauthorizedException
    {
        IQ response;
        if (password == null || password.length() == 0) {
            throw new UnauthorizedException();
        }
        else {
            try {
                userManager.getUser(username).setPassword(password);
                response = IQ.createResultIQ(packet);
                List<String> params = new ArrayList<String>();
                params.add(username);
                params.add(session.toString());
                Log.info(LocaleUtils.getLocalizedString("admin.password.update", params));
            }
            catch (UserNotFoundException e) {
                throw new UnauthorizedException();
            }
        }
        return response;
    }

    private IQ anonymousLogin(ClientSession session, IQ packet) {
        IQ response = IQ.createResultIQ(packet);
        if (anonymousAllowed) {
            session.setAnonymousAuth();
            response.setTo(session.getAddress());
            Element auth = response.setChildElement("query", "jabber:iq:auth");
            auth.addElement("resource").setText(session.getAddress().getResource());
        }
        else {
            response.setChildElement(packet.getChildElement().createCopy());
            response.setError(PacketError.Condition.forbidden);
        }
        return response;
    }

    public boolean isAnonymousAllowed() {
        return anonymousAllowed;
    }

    public void setAllowAnonymous(boolean isAnonymous) throws UnauthorizedException {
        anonymousAllowed = isAnonymous;
        JiveGlobals.setProperty("xmpp.auth.anonymous", anonymousAllowed ? "true" : "false");
    }

    public void initialize(XMPPServer server) {
        super.initialize(server);
        localServer = server;
        userManager = server.getUserManager();
        sessionManager = server.getSessionManager();
    }

    public IQHandlerInfo getInfo() {
        return info;
    }
}
