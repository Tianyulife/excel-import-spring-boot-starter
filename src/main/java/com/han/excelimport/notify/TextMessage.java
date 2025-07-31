package com.han.excelimport.notify;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/30 15:16
 * @Description: cn.utrust.fintech.utrusts.rpa.admin.core.domain
 */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextMessage {
    /**
     * 消息内容
     */
    private String content;

    /**
     * @ 的用户 ID 列表（企业微信内的 userid）
     */
    private List<String> mentionedUserIds;

    /**
     * @ 的用户手机号列表（推荐）
     */
    private List<String> mentionedMobiles;
}
