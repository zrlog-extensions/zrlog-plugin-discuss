package com.zrlog.plugin.discuss;


import com.zrlog.plugin.discuss.controller.DiscussController;
import com.zrlog.plugin.client.NioClient;
import com.zrlog.plugin.render.SimpleTemplateRender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application {
    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        List<Class<?>> classList = new ArrayList<>();
        classList.add(DiscussController.class);
        new NioClient(null, new SimpleTemplateRender(), new DiscussClientActionHandler()).connectServer(args, classList, DiscussPluginAction.class);
    }
}
