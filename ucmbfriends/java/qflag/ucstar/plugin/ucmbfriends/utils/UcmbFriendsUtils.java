package qflag.ucstar.plugin.ucmbfriends.utils;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.jivesoftware.util.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import qflag.ucstar.plugin.ucmbfriends.manager.UcmbFriendsConfigManager;

public class UcmbFriendsUtils {
	
	public static String getUploadFile() {
		String netdiskPath = UcmbFriendsConfigManager.getInstance().getUploadPath();
		
        return netdiskPath;
    }
	
	public static JSONObject getJsonObjByStr(String _jsonStr){
		JSONObject jsonObject = null;
		try {
			jsonObject = JSONObject.fromObject(_jsonStr);
		} catch (Exception e) {
			Log.error(e);
		}
		return jsonObject;
		 
	}
	
	public static boolean getSmall(String str,String str1){
		return UcmbFriendsConfigManager.getInstance().getImagesmall(str,str1);
	}
	
	public static void main(String[] args) {
		String json = "{\"USERID\":\"test01\", \"VERSION\":\"0\"}";  
        JSONObject jsonObject = JSONObject.fromObject( json ); 
        System.out.println(jsonObject);
        Object bean = JSONObject.toBean( jsonObject );
        try {
			System.out.println(jsonObject.get( "USERID" )+" "+PropertyUtils.getProperty( bean, "USERID" ));
	        System.out.println(jsonObject.get( "VERSION" )+" "+PropertyUtils.getProperty( bean, "VERSION" ));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
}
