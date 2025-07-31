package com.han.excelimport.domain;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/31 11:44
 * @Description: com.han.excelimport.domain
 */

import com.han.excelimport.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeDetail {

    @Excel(name = "交易类型")
    private String tradeType;

    @Excel(name = "交易笔数")
    private Integer tradeCount;

    @Excel(name = "交易金额（元）")
    private BigDecimal tradeAmount;

    @Excel(name = "手续费（元）")
    private BigDecimal fee;

    @Excel(name = "结算金额（元）")
    private BigDecimal settlementAmount;
}

