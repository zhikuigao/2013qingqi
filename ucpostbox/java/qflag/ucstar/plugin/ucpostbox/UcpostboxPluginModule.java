package qflag.ucstar.plugin.ucpostbox;

import qflag.ucstar.plugin.manager.UcstarPlugin;
import qflag.ucstar.plugin.manager.UcstarPluginModule;
import qflag.ucstar.plugin.thread.UcpostGetNewMessage;
import qflag.ucstar.plugin.thread.UcpostSendMain;

/**
 * @author gzkui
 * 2014/9/23
 * 插件启动模块
 */
public class UcpostboxPluginModule implements UcstarPluginModule {
	
	public void startPlugin(UcstarPlugin arg0){
		System.out.println("<<<<<<<<<启动邮件同步插件............");
		System.out.println("<<<<<<<<<启动邮件状态监控............");
		//邮件插件启动
		new Thread(new UcpostGetNewMessage()).start();
		UcpostSendMain.getInstance().startCheckUnreadMail();
	}
	
	public void stopPlugin(UcstarPlugin arg0) {
		// TODO Auto-generated method stub
	}
}
