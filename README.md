# zrlog-plugin-discuss

ZrLog Discuss 评论框插件。通过独立插件接入 Discuss 自托管评论系统，不扩展内置评论插件的业务边界。

## 功能

- 配置 Discuss 服务地址
- 配置前台 `discuss.js` 脚本地址
- 控制 Discuss 评论框是否输出
- 可选固定评论路径，默认使用当前页面路径

## 构建

```shell
export JAVA_HOME=${HOME}/dev/graalvm-jdk-latest
export PATH=${JAVA_HOME}/bin:$PATH
```
