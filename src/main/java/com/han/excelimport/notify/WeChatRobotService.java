package com.han.excelimport.notify;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/30 15:07
 * @Description: cn.utrust.fintech.utrusts.rpa.admin.core.service
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class WeChatRobotService {

    private final String webhookUrl;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public WeChatRobotService(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    /** 发送结果封装 */
    public static class SendResult {
        private final boolean success;
        private final int httpCode;
        private final String responseBody;
        private final String errorMessage;

        public SendResult(boolean success, int httpCode, String responseBody, String errorMessage) {
            this.success = success;
            this.httpCode = httpCode;
            this.responseBody = responseBody;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() {
            return success;
        }

        public int getHttpCode() {
            return httpCode;
        }

        public String getResponseBody() {
            return responseBody;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    /**
     * 快捷发送文本消息给 @所有人
     */
    public SendResult sendTextToAll(String content) {
        TextMessage message = TextMessage.builder()
                .content(content)
                .mentionedUserIds(Collections.singletonList("@all"))
                .build();
        return sendText(message);
    }

    /**
     * 发送文本消息
     */
    public SendResult sendText(TextMessage message) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("msgtype", "text");

        Map<String, Object> text = new HashMap<>();
        text.put("content", message.getContent());

        if (message.getMentionedUserIds() != null && !message.getMentionedUserIds().isEmpty()) {
            text.put("mentioned_list", message.getMentionedUserIds());
        }

        if (message.getMentionedMobiles() != null && !message.getMentionedMobiles().isEmpty()) {
            text.put("mentioned_mobile_list", message.getMentionedMobiles());
        }

        payload.put("text", text);
        return postJson(payload);
    }

    /**
     * 发送 Markdown 格式消息
     */
    public SendResult sendMarkdown(String content) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("msgtype", "markdown");

        Map<String, String> markdown = new HashMap<>();
        markdown.put("content", content);
        payload.put("markdown", markdown);

        return postJson(payload);
    }

    /**
     * 发送图片消息示例
     * @param base64 图片内容base64编码
     * @param md5    图片的md5值
     */
    public SendResult sendImage(String base64, String md5) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("msgtype", "image");

        Map<String, String> image = new HashMap<>();
        image.put("base64", base64);
        image.put("md5", md5);

        payload.put("image", image);
        return postJson(payload);
    }

    /**
     * 发送请求核心方法
     */
    private SendResult postJson(Map<String, Object> data) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(webhookUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String json = OBJECT_MAPPER.writeValueAsString(data);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            BufferedReader reader;
            if (code == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            String responseBody = responseBuilder.toString();

            if (code != 200) {
                log.error("企业微信机器人发送失败，HTTP状态码：{}，响应内容：{}", code, responseBody);
                return new SendResult(false, code, responseBody, "HTTP错误");
            }

            return new SendResult(true, code, responseBody, null);

        } catch (Exception e) {
            log.error("企业微信机器人消息发送异常", e);
            return new SendResult(false, -1, null, e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}

