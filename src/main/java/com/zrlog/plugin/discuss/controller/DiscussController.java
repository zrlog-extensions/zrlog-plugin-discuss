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
        session.sendMsg(new MsgPacket(requestConfig(), ContentType.JSON, MsgPacketStatus.SEND_REQUEST, IdUtil.getInt(),
                ActionType.SET_WEBSITE.name()), msgPacket -> {
            response(DiscussApiResponse.success());
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
        DiscussConfig config = loadConfig();
        Map<String, Object> data = new HashMap<>();
        data.put("enabledJson", gson.toJson(config.isEnabled()));
        data.put("serverURLsJson", gson.toJson(toServerURLsConfig(config.getServerURLs())));
        data.put("scriptUrlJson", gson.toJson(asString(config.getScriptUrl())));
        data.put("pathJson", gson.toJson(asString(config.getPath())));
        session.responseHtml("/widget", data, requestPacket.getMethodStr(), requestPacket.getMsgId());
    }

    private DiscussApiResponse<DiscussPageData> pageData() {
        DiscussPageData data = new DiscussPageData();
        data.setDark(isDarkMode());
        data.setColorPrimary(getAdminColorPrimary());
        data.setPlugin(session.getPlugin());
        data.setConfig(loadConfig());
        return DiscussApiResponse.success(data);
    }

    private DiscussConfig loadConfig() {
        DiscussConfig config = session.getResponseSync(ContentType.JSON, WebsiteKeyRequest.of(CONFIG_KEYS), ActionType.GET_WEBSITE,
                DiscussConfig.class);
        if (config == null) {
            config = new DiscussConfig();
        }
        config.normalize(DEFAULT_SCRIPT_URL, session.getPlugin().getVersion());
        return config;
    }

    private Object toServerURLsConfig(String value) {
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

    private String asString(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private DiscussConfig requestConfig() {
        DiscussConfig config = new DiscussConfig();
        String status = paramValue("status");
        config.setStatus(Objects.equals(status, "on") ? "on" : "off");
        config.setServerURLs(asString(paramValue("serverURLs")));
        String scriptUrl = asString(paramValue("scriptUrl"));
        config.setScriptUrl(scriptUrl.isEmpty() ? DEFAULT_SCRIPT_URL : scriptUrl);
        config.setPath(asString(paramValue("path")));
        return config;
    }

    private String paramValue(String key) {
        if (requestInfo.getParam() == null || requestInfo.getParam().get(key) == null || requestInfo.getParam().get(key).length == 0) {
            return null;
        }
        return requestInfo.getParam().get(key)[0];
    }

    private void response(Object data) {
        session.sendMsg(ContentType.JSON, data, requestPacket.getMethodStr(), requestPacket.getMsgId(), MsgPacketStatus.RESPONSE_SUCCESS);
    }

    private boolean isDarkMode() {
        return requestInfo.isDarkMode();
    }

    private String getAdminColorPrimary() {
        return requestInfo.getAdminColorPrimary();
    }
}
