package io.github.tianyulife.excelimport.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.github.tianyulife.excelimport.annotation.Excel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 聚合支付对账单明细表对象 pay_statement_detail
 * 
 * @author 
 * @date 2024-08-23
 */
@Data

public class PayStatementDetail implements Serializable {
    private static final long serialVersionUID = 1L;



    /** 账单日期 yyyyMMdd */
    
    @Length(message = "账单日期 yyyy-MM-dd的字符长度不可大于10", max = 10)
    private String billDate;

    /** 门店编号 */
    @Length(message = "门店编号的字符长度不可大于64", max = 64)
    @Excel(name = "门店编号")
    private String storeNo;

    /** 门店名称 */

    @Length(message = "门店名称的字符长度不可大于64", max = 64)
    @Excel(name = "门店名称")
    private String storeName;

    /** 商户名称 */
    @Length(message = "商户名称的字符长度不可大于64", max = 64)
    @Excel(name = "商户名称")
    private String merName;

    /** 第三方商户号 */

    @Length(message = "第三方商户号的字符长度不可大于64", max = 64)
    @Excel(name = "第三方商户号")
    private String thridMerId;

    /** 商户号 */

    @Length(message = "商户号的字符长度不可大于64", max = 64)
    @Excel(name = "商户号")
    private String merId;

    /** 商户订单号 */

    @Length(message = "商户订单号的字符长度不可大于64", max = 64)
    @Excel(name = "商户订单号")
    private String orderNo;

    /** 银行流水号 */

    @Length(message = "银行流水号的字符长度不可大于64", max = 64)
    @Excel(name = "银行流水")
    private String bankFlowNo;

    /** 交易日期 */

    @Length(message = "交易日期的字符长度不可大于10", max = 10)
    @Excel(name = "交易日期")
    private String tradeDate;

    /** 交易时间 */

    @Length(message = "交易时间的字符长度不可大于10", max = 10)
    @Excel(name = "交易时间")
    private String tradeTime;

    /** 商品名称 */

    @Length(message = "商品名称的字符长度不可大于100", max = 100)
    @Excel(name = "商品名称")
    private String name;

    /** 交易金额（元） */

    @Excel(name = "交易金额",defaultValue = "0")
    private BigDecimal tradeAmount;

    /** 优惠金额（元） */

    @Excel(name = "优惠金额(免充值优惠券金额)",defaultValue = "0")
    private BigDecimal couponAmount;

    /** 交易币种 */

    @Length(message = "交易币种的字符长度不可大于10", max = 10)
    @Excel(name = "交易币种")
    private String currency;

    /** 费率 */

    @Excel(name = "费率",defaultValue = "0")
    private BigDecimal feeRate;

    /** 付款银行 */

    @Length(message = "付款银行的字符长度不可大于100", max = 100)
    @Excel(name = "付款银行")
    private String payBankName;

    /** 支付方式 */

    @Length(message = "支付方式的字符长度不可大于10", max = 10)
    @Excel(name = "支付方式")
    private String payType;

    /** 交易类型 */

    @Length(message = "交易类型的字符长度不可大于10", max = 10)
    @Excel(name = "交易类型")
    private String tradeType;

    /** 交易状态 */

    @Excel(name = "交易状态",defaultValue = "S")
    private String tradeState;

    /** 结算金额（元） */

    @Excel(name = "结算金额" ,defaultValue = "0")
    private BigDecimal settleAmount;

    /** 手续费（元） */
    @Excel(name = "手续费",defaultValue = "0")

    private BigDecimal feeAmount;

    /** 第三方订单号 */

    @Length(message = "第三方订单号的字符长度不可大于64", max = 64)
    @Excel(name = "第三方订单号")
    private String thirdOrderNo;

    /** appId */

    @Length(message = "appId不可大于64", max = 64)
    @Excel(name = "AppID")
    private String appId;
    
    /** 清分结果 */

    @Length(message = "清分结果的字符长度不可大于64", max = 64)
    @Excel(name = "清分结果")
    private String clearResult;

    /** 清分日期 */

    @Length(message = "清分日期的字符长度不可大于64", max = 64)
    @Excel(name = "清分日期")
    private String clearDate;

    /** 清分账号 */

    @Length(message = "清分账号的字符长度不可大于64", max = 64)
    @Excel(name = "清分账号")
    private String clearAccount;

    /** 付款人信息 */

    @Length(message = "付款人信息的字符长度不可大于255", max = 255)
    @Excel(name = "付款人信息")
    private String payerInfo;

    /** 完成时间 */

    @Length(message = "完成时间的字符长度不可大于64", max = 64)
    @Excel(name = "完成日期")
    private String finishTime;

    /** 付款人用户名 */

    @Length(message = "付款人用户名的字符长度不可大于255", max = 255)
    @Excel(name = "付款人用户名")
    private String payerName;
    
    /** 入账流水号 */

    @Length(message = "入账流水号的字符长度不可大于64", max = 64)
    @Excel(name = "入账流水")
    private String entryFlowNo;

    /** 创建时间 */
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;



}
