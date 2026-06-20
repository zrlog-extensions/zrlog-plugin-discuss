package com.zrlog.plugin.discuss;

import com.zrlog.plugin.RunConstants;
import com.zrlog.plugin.common.PluginNativeImageUtils;
import com.zrlog.plugin.discuss.controller.DiscussController;
import com.zrlog.plugin.type.RunType;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class GraalvmAgentApplication {


    public static void main(String[] args) throws IOException {
        RunConstants.runType = RunType.AGENT;
        PluginNativeImageUtils.usedGsonObject();
        String basePath = System.getProperty("user.dir").replace("\\target","").replace("/target", "");
        //PathKit.setRootPath(basePath);
        File file = new File(basePath + "/src/main/resources");
        PluginNativeImageUtils.doLoopResourceLoad(file.listFiles(), file.getPath()  + "/", "/");
        //Application.nativeAgent = true;
        PluginNativeImageUtils.exposeController(Collections.singletonList(DiscussController.class));
        Application.main(args);

    }
}
