package com.zrlog.plugin.discuss.controller;

import java.util.Objects;

public class DiscussConfig {

    private String status;
    private String serverURLs;
    private String scriptUrl;
    private String path;
    private String version;

    public void normalize(String defaultScriptUrl, String version) {
        if (!Objects.equals(status, "on")) {
            status = "off";
        }
        if (isBlank(scriptUrl)) {
            scriptUrl = defaultScriptUrl;
        }
        if (isBlank(serverURLs)) {
            serverURLs = "";
        }
        if (isBlank(path)) {
            path = "";
        }
        this.version = version;
    }

    public boolean isEnabled() {
        return Objects.equals(status, "on") && !isBlank(serverURLs);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServerURLs() {
        return serverURLs;
    }

    public void setServerURLs(String serverURLs) {
        this.serverURLs = serverURLs;
    }

    public String getScriptUrl() {
        return scriptUrl;
    }

    public void setScriptUrl(String scriptUrl) {
        this.scriptUrl = scriptUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
