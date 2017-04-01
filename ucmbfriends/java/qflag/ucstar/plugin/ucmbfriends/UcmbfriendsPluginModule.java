package qflag.ucstar.plugin.ucmbfriends;

import qflag.ucstar.plugin.manager.UcstarPlugin;
import qflag.ucstar.plugin.manager.UcstarPluginModule;

public class UcmbfriendsPluginModule implements UcstarPluginModule {

    public void startPlugin(UcstarPlugin _plugin) {
        System.out.println("启动插件:" + _plugin.getName());
    }

    public void stopPlugin(UcstarPlugin _plugin) {
        
    }

}
