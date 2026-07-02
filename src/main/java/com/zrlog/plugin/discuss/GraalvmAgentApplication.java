package com.zrlog.plugin.discuss;

import com.zrlog.plugin.RunConstants;
import com.zrlog.plugin.common.PluginNativeImageUtils;
import com.zrlog.plugin.discuss.controller.DiscussApiResponse;
import com.zrlog.plugin.discuss.controller.DiscussConfig;
import com.zrlog.plugin.discuss.controller.DiscussController;
import com.zrlog.plugin.discuss.controller.DiscussPageData;
import com.zrlog.plugin.discuss.controller.WebsiteKeyRequest;
import com.zrlog.plugin.type.RunType;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class GraalvmAgentApplication {


    public static void main(String[] args) throws IOException {
        RunConstants.runType = RunType.AGENT;
        PluginNativeImageUtils.usedGsonObject();
        PluginNativeImageUtils.gsonNativeAgentByClazz(Arrays.asList(DiscussApiResponse.class, DiscussConfig.class,
                DiscussPageData.class, WebsiteKeyRequest.class));
        String basePath = System.getProperty("user.dir").replace("\\target","").replace("/target", "");
        //PathKit.setRootPath(basePath);
        File file = new File(basePath + "/src/main/resources");
        PluginNativeImageUtils.doLoopResourceLoad(file.listFiles(), file.getPath()  + "/", "/");
        //Application.nativeAgent = true;
        PluginNativeImageUtils.exposeController(Collections.singletonList(DiscussController.class));
        Application.main(args);

    }
}
