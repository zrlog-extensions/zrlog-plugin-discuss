package com.zrlog.plugin.discuss.controller;

import com.google.gson.Gson;
import com.zrlog.plugin.IOSession;
import com.zrlog.plugin.common.IdUtil;
import com.zrlog.plugin.data.codec.ContentType;
import com.zrlog.plugin.data.codec.HttpRequestInfo;
import com.zrlog.plugin.data.codec.MsgPacket;
import com.zrlog.plugin.data.codec.MsgPacketStatus;
import com.zrlog.plugin.type.ActionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DiscussController {

    private static final String CONFIG_KEYS = "status,serverURLs,scriptUrl,path";
    private static final String DEFAULT_SCRIPT_URL = "https://lib.baomitu.com/discuss/1.2.1/discuss.js";

    private final IOSession session;
    private final MsgPacket requestPacket;
    private final HttpRequestInfo requestInfo;
    private final Gson gson = new Gson();

    public DiscussController(IOSession session, MsgPacket requestPacket, HttpRequestInfo requestInfo) {
        this.session = session;
        this.requestPacket = requestPacket;
        this.requestInfo = requestInfo;
    }

    public void update() {
        Map<String, Object> params = new HashMap<>(requestInfo.simpleParam());
        if (!Objects.equals(params.get("status"), "on")) {
            params.put("status", "off");
        }
        if (isBlank(params.get("scriptUrl"))) {
            params.put("scriptUrl", DEFAULT_SCRIPT_URL);
        }
        session.sendMsg(new MsgPacket(params, ContentType.JSON, MsgPacketStatus.SEND_REQUEST, IdUtil.getInt(),
                ActionType.SET_WEBSITE.name()), msgPacket -> {
            Map<String, Object> map = new HashMap<>();
            map.put("success", true);
            session.sendMsg(new MsgPacket(map, ContentType.JSON, MsgPacketStatus.RESPONSE_SUCCESS, requestPacket.getMsgId(), requestPacket.getMethodStr()));
        });
    }

    public void info() {
        response(loadConfig());
    }

    public void index() {
        Map<String, Object> data = new HashMap<>();
        data.put("theme", isDarkMode() ? "dark" : "light");
        data.put("data", gson.toJson(pageData()));
        session.responseHtml("/templates/index", data, requestPacket.getMethodStr(), requestPacket.getMsgId());
    }

    public void json() {
        response(pageData());
    }

    public void widget() {
        Map<String, Object> config = loadConfig();
        boolean enabled = Objects.equals(config.get("status"), "on") && !isBlank(config.get("serverURLs"));
        Map<String, Object> data = new HashMap<>();
        data.put("enabledJson", gson.toJson(enabled));
        data.put("serverURLsJson", gson.toJson(toServerURLsConfig(config.get("serverURLs"))));
        data.put("scriptUrlJson", gson.toJson(asString(config.get("scriptUrl"))));
        data.put("pathJson", gson.toJson(asString(config.get("path"))));
        session.responseHtml("/widget", data, requestPacket.getMethodStr(), requestPacket.getMsgId());
    }

    private Map<String, Object> pageData() {
        Map<String, Object> data = new HashMap<>();
        data.put("dark", isDarkMode());
        data.put("colorPrimary", getAdminColorPrimary());
        data.put("plugin", session.getPlugin());
        data.put("config", loadConfig());
        return successMap(data);
    }

    private Map<String, Object> loadConfig() {
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("key", CONFIG_KEYS);
        Map response = session.getResponseSync(ContentType.JSON, keyMap, ActionType.GET_WEBSITE, Map.class);
        Map<String, Object> config = response == null ? new HashMap<>() : new HashMap<>(response);
        if (!Objects.equals(config.get("status"), "on")) {
            config.put("status", "off");
        }
        if (isBlank(config.get("scriptUrl"))) {
            config.put("scriptUrl", DEFAULT_SCRIPT_URL);
        }
        if (isBlank(config.get("serverURLs"))) {
            config.put("serverURLs", "");
        }
        if (isBlank(config.get("path"))) {
            config.put("path", "");
        }
        config.put("version", session.getPlugin().getVersion());
        return config;
    }

    private Object toServerURLsConfig(Object value) {
        String raw = asString(value);
        if (raw.trim().isEmpty()) {
            return "";
        }
        String[] pieces = raw.split("[\\r\\n,]+");
        List<String> urls = new ArrayList<>();
        for (String piece : pieces) {
            String trimmed = piece.trim();
            if (!trimmed.isEmpty()) {
                urls.add(trimmed);
            }
        }
        if (urls.size() == 1) {
            return urls.get(0);
        }
        return urls;
    }

    private boolean isBlank(Object value) {
        return value == null || String.valueOf(value).trim().isEmpty();
    }

    private String asString(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private Map<String, Object> successMap(Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("data", data);
        return map;
    }

    private void response(Map<String, Object> map) {
        session.sendMsg(ContentType.JSON, map, requestPacket.getMethodStr(), requestPacket.getMsgId(), MsgPacketStatus.RESPONSE_SUCCESS);
    }

    private boolean isDarkMode() {
        return requestInfo.isDarkMode();
    }

    private String getAdminColorPrimary() {
        return requestInfo.getAdminColorPrimary();
    }
}
