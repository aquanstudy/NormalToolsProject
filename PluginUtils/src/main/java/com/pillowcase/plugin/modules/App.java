package com.pillowcase.plugin.modules;

import java.util.Objects;

/**
 * Author      :  PillowCase
 * Created On  ： 2020-12-14 14:57
 * Description ： APP相关信息
 */
public class App {
    private String appName;
    private String label;
    private String packageName;
    private String applicationName;
    private boolean isRunning = false;

    @Override
    public String toString() {
        return "App{" + '\n' +
                "appName='" + appName + '\n' +
                ", label='" + label + '\n' +
                ", packageName='" + packageName + '\n' +
                ", applicationName='" + applicationName + '\n' +
                ", isRunning=" + isRunning +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof App)) return false;
        App that = (App) o;
        return Objects.equals(getAppName(), that.getAppName()) &&
                Objects.equals(getPackageName(), that.getPackageName()) &&
                Objects.equals(getApplicationName(), that.getApplicationName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAppName(), getPackageName(), getApplicationName(), isRunning());
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
