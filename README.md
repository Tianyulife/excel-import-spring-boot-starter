# MultiSegment Excel Import Spring Boot Starter

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

## 项目简介

MultiSegment Excel Import Starter 是一个基于 Hutool SAX 方式实现的高性能、多段分区的 Excel 异步导入框架。  
支持按不同段落定义独立标题行、独立数据段和处理逻辑，灵活应对复杂 Excel 文件导入需求。

核心特点：
- 多段分区导入，支持独立标题行
- 基于注解的字段映射与校验
- 支持批量处理，减少数据库交互
- 轻量无侵入，适配 Spring Boot 生态
- 兼容 Excel 2003/2007+ 格式


MultiSegment Excel Import Starter is a high-performance, multi-segment Excel asynchronous import framework based on Hutool's SAX parsing approach.
It supports defining independent headers, data segments, and processing logic for different parts of an Excel file, making it highly adaptable to complex import scenarios.

Key Features:

- Multi-segment import with independent header rows

- Annotation-based field mapping and validation

- Supports batch processing to reduce database interactions

- Lightweight and non-intrusive, fully compatible with Spring Boot ecosystem

- Supports Excel 2003 and 2007+ formats



## 开源协议

本项目采用 Apache License 2.0 许可证，详情请见 [LICENSE](LICENSE) 文件。

## 安装使用

请将本项目作为 Maven 依赖引入：

```xml
<dependency>
    <groupId>io.github.tianyulife</groupId>
    <artifactId>excel-import-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 重大更新

### 新增企业微信群机器人


#### 功能介绍 / Features

本功能提供了基于 Spring Boot 的企业微信群机器人自动配置支持。通过简单的配置即可启用机器人消息推送服务，方便项目中快速集成企业微信群消息通知。

- 通过 wechat.robot.enable 开关控制是否启用该功能。

- 通过 wechat.robot.webhook-url 配置机器人 Webhook 地址，支持文本、Markdown 等格式消息发送。

- 自动装配 WeChatRobotService，提供易用的发送接口。

- 支持灵活集成，可用于导入任务完成通知、异常报警等场景。

This feature provides Spring Boot auto-configuration support for WeChat Work group chat robots. By simply configuring properties, you can enable robot message push service, making it easy to integrate WeChat Work notifications into your projects.

- Controlled by the switch wechat.robot.enable to enable or disable the feature.

- the robot Webhook URL via wechat.robot.webhook-url, supporting text, markdown, and other message formats.

- Auto-configures the WeChatRobotService, offering simple APIs to send messages.

- Flexible integration for scenarios like import completion notifications, error alerts, and more.



#### 企业微信群机器人自动配置（WeChatRobotProperties）
**本模块基于 Spring Boot 自动配置，方便集成企业微信群机器人的消息推送功能。**

**配置说明**
| 配置项                        | 说明                   | 示例                                                             | 必填 |
| -------------------------- | -------------------- | -------------------------------------------------------------- | -- |
| `wechat.robot.enable`      | 是否启用企业微信群机器人功能       | `true` 或 `false`（默认不启用）                                        | 是  |
| `wechat.robot.webhook-url` | 企业微信群机器人的 Webhook 地址 | `https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=xxxxxxx` | 是  |

使用示例（application.yml）
```yaml
wechat:
  robot:
    enable: true
    webhook-url: https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=YOUR_WEBHOOK_KEY
```






