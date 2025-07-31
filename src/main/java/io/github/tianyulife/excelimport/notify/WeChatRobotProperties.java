package io.github.tianyulife.excelimport.notify;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/31 09:47
 * @Description: com.han.excelimport.notify
 */
@Data
@ConfigurationProperties(prefix = "wechat.robot")
public class WeChatRobotProperties {
    /**
     * 企业微信群机器人 Webhook URL
     */
    private String webhookUrl;

}

