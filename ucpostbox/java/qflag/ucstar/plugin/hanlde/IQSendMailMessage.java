package qflag.ucstar.plugin.hanlde;

import org.jivesoftware.wildfire.IQHandlerInfo;
import org.jivesoftware.wildfire.auth.UnauthorizedException;
import org.jivesoftware.wildfire.handler.IQHandler;
import org.xmpp.packet.IQ;

public class IQSendMailMessage extends IQHandler {
	
	private IQSendMailMessage info;

	public IQSendMailMessage(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IQHandlerInfo getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		// TODO Auto-generated method stub
		return null;
	}

}
