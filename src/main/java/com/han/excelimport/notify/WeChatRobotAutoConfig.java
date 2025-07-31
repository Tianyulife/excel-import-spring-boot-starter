package com.han.excelimport.notify;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/31 09:45
 * @Description: com.han.excelimport.notify
 */
@Configuration
@EnableConfigurationProperties(WeChatRobotProperties.class)
@ConditionalOnProperty(name = "wechat.robot.enable", havingValue = "true")
public class WeChatRobotAutoConfig {

    @Bean
    public WeChatRobotService weChatRobotService(WeChatRobotProperties properties) {
        return new WeChatRobotService(properties.getWebhookUrl());
    }
}

