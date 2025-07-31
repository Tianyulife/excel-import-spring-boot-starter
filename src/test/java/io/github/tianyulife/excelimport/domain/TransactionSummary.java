package io.github.tianyulife.excelimport.domain;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/23 15:26
 * @Description: com.zws.miniapp.domain
 */

import io.github.tianyulife.excelimport.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionSummary {

    @Excel(index = 12)
    private String transactionType;

    @Excel(index = 13)
    private Integer transactionCount;

    @Excel(index = 14)
    private BigDecimal transactionAmount;

    @Excel(index = 15)
    private BigDecimal fee;

    @Excel(index = 16)
    private BigDecimal settlementAmount;


}

