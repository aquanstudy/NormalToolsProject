package com.pillowcase.game.plugin.detection.modules;

import java.util.Objects;

/**
 * Author      :  PillowCase
 * Created On  ： 2020-07-28 17:01
 * Description ： 外挂APP相关信息
 */
public class GamePlugin {
    private String appName;
    private String packageName;
    private String applicationName;
    private boolean isRunning = false;

    @Override
    public String toString() {
        return "GamePlugin{" + '\n' +
                "appName='" + appName + '\n' +
                ", packageName='" + packageName + '\n' +
                ", applicationName='" + applicationName + '\n' +
                ", isRunning='" + isRunning + '\n' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GamePlugin)) return false;
        GamePlugin plugin = (GamePlugin) o;
        return appName.equals(plugin.appName) &&
                packageName.equals(plugin.packageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appName, packageName);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
