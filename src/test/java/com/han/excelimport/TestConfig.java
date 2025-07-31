package com.han.excelimport;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/31 14:19
 * @Description: com.han.excelimport
 */
@Configuration
@ComponentScan("com.han.excelimport") // 你的starter包路径，扫描你的组件
public class TestConfig {

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
