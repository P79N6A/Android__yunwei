package com.zhongzhi.grid.GDactivity.activity.Bean;

public class UpdateInfo {
    //app名字
    private String appname;
    //服务器版本
    private int serverVersion;
    //强制升级
    private boolean whetherForce;
    //app最新版本地址
    private String updateurl;
    //升级信息
    private String upgradeinfo;

    /**
     * 都生成对于的set and get 方法
     * 这里我就不把set and get 方法贴出来了，读者注意去生成就好了
     */
    public void setAppname(String appname) {
        this.appname = appname;
    }

    public void setServerVersion(int serverVersion) {
        this.serverVersion = serverVersion;
    }

    public void setWhetherForce(boolean whetherForce) {
        this.whetherForce = whetherForce;
    }

    public void setUpdateurl(String updateurl) {
        this.updateurl = updateurl;
    }

    public void setUpgradeinfo(String upgradeinfo) {
        this.upgradeinfo = upgradeinfo;
    }

    public String getAppname() {
        return appname;
    }

    public int getServerVersion() {
        return serverVersion;
    }

    public boolean isWhetherForce() {
        return whetherForce;
    }

    public String getUpdateurl() {
        return updateurl;
    }

    public String getUpgradeinfo() {
        return upgradeinfo;
    }
}
