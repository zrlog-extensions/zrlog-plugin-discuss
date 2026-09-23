# zrlog-plugin-discuss

ZrLog Discuss 评论框插件。通过独立插件接入 Discuss 自托管评论系统，不扩展内置评论插件的业务边界。

## 功能

- 配置 Discuss 服务地址
- 配置站点页面使用的 `discuss.js` 脚本地址
- 控制 Discuss 评论框是否输出
- 可选固定评论路径，默认使用当前页面路径

## 构建

```shell
export JAVA_HOME=${HOME}/dev/graalvm-jdk-latest
export PATH=${JAVA_HOME}/bin:$PATH
```

## 原生制品发布

Linux amd64/arm64 制品在上传前会调用 `zrlog-artifact-service`，通过与 `plugin-core`
相同的固定版本 `process-artifact` Action 完成压缩和 SHA-256、文件大小校验。
处理成功后才会生成最终制品的 MD5 并上传；处理失败会停止该平台的发布。
服务接收的版本号使用 `bin/build-info.sh` 生成的实际插件版本。

发布前需要配置 Actions Secret `ARTIFACT_SERVICE_TOKEN`，可在仓库中单独设置，
或授权该仓库使用同名组织 Secret。服务地址为 `https://webdav.zrlog.com/artifact`。
