package io.github.tianyulife.excelimport.domain;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/8/1 14:13
 * @Description: io.github.tianyulife.excelimport.domain
 */

import io.github.tianyulife.excelimport.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.Max;

@Data
public class DemoExcelData {

    @Excel(index = 1)
    private String name;

    @Excel(index = 2)
    @Max(value = 50, message = "年龄不能大于50")
    private Integer age;

    @Excel(index = 3)
    private String gender;

    @Excel(index = 4)
    private String phone;

    @Excel(index = 5)
    private String email;

    @Excel(index = 6)
    private String address;
}

