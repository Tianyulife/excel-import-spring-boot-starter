package com.han.excelimport.autoconfig;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/28 17:48
 * @Description: com.han.excelimport.autoconfig
 */

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.han.excelimport") // 或整个 com.han.excelimport
public class ExcelImportAutoConfiguration {
    // 这里可以配置默认 Bean，或者让用户自己配置 Handler
}

