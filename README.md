# MultiSegment Excel Import Spring Boot Starter

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

## 项目简介

MultiSegment Excel Import Starter 是一个基于 Hutool SAX 方式实现的高性能、多段分区的 Excel 异步导入框架。  
支持按不同段落定义独立标题行、独立数据段和处理逻辑，灵活应对复杂 Excel 文件导入需求。

核心特点：
- 多段分区导入，支持独立标题行
- 基于注解的字段映射与校验
- 支持批量处理，减少数据库交互
- 轻量无侵入，适配 Spring Boot 生态
- 兼容 Excel 2003/2007+ 格式


MultiSegment Excel Import Starter is a high-performance, multi-segment Excel asynchronous import framework based on Hutool's SAX parsing approach.
It supports defining independent headers, data segments, and processing logic for different parts of an Excel file, making it highly adaptable to complex import scenarios.

Key Features:

- Multi-segment import with independent header rows

- Annotation-based field mapping and validation

- Supports batch processing to reduce database interactions

- Lightweight and non-intrusive, fully compatible with Spring Boot ecosystem

- Supports Excel 2003 and 2007+ formats



## 开源协议

本项目采用 Apache License 2.0 许可证，详情请见 [LICENSE](LICENSE) 文件。

## 安装使用

请将本项目作为 Maven 依赖引入：

```xml
<dependency>
  <groupId>com.han.excelimport</groupId>
  <artifactId>multi-segment-excel-import-spring-boot-starter</artifactId>
  <version>1.0.0</version>
</dependency>


