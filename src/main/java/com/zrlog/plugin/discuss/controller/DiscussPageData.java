package com.zrlog.plugin.discuss.controller;

import com.zrlog.plugin.message.Plugin;

public class DiscussPageData {

    private boolean dark;
    private String colorPrimary;
    private Plugin plugin;
    private DiscussConfig config;

    public boolean isDark() {
        return dark;
    }

    public void setDark(boolean dark) {
        this.dark = dark;
    }

    public String getColorPrimary() {
        return colorPrimary;
    }

    public void setColorPrimary(String colorPrimary) {
        this.colorPrimary = colorPrimary;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public DiscussConfig getConfig() {
        return config;
    }

    public void setConfig(DiscussConfig config) {
        this.config = config;
    }
}
