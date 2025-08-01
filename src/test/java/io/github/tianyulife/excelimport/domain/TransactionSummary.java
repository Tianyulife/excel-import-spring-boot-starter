package io.github.tianyulife.excelimport.domain;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/23 15:26
 * @Description: com.zws.miniapp.domain
 */

import io.github.tianyulife.excelimport.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
public class TransactionSummary {

    @Excel(index = 13)
    private String transactionType;

    @Excel(index = 14)
    private Integer transactionCount;

    @Excel(index = 15)
    private BigDecimal transactionAmount;

    @Excel(index = 16)
    @DecimalMin(value = "0.00", inclusive = true, message = "费用不能小于0")
    private BigDecimal fee;

    @Excel(index = 17)
    private BigDecimal settlementAmount;


}

