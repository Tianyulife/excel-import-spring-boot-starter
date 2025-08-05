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

## 🪪 License
This project is licensed under the Apache License 2.0.
For more details, please refer to the [LICENSE](LICENSE) file.

## 安装使用

请将本项目作为 Maven 依赖引入：

## 📦 Installation
**Add the following dependency to your Maven pom.xml:**

```xml
<dependency>
    <groupId>io.github.tianyulife</groupId>
    <artifactId>excel-import-spring-boot-starter</artifactId>
    <version>2.0.0</version>
</dependency>
```

## 示例代码

```java
@SpringBootTest(classes = TestConfig.class)
@ExtendWith(SpringExtension.class)
public class ExcelImportServiceTest {
    @Autowired
    private MultiSegmentExcelSaxProcessor multiSegmentExcelSaxProcessor;
    @Autowired(required = false)
    private WeChatRobotService weChatRobotService;
    @Autowired
    private ExcelFileImportProcessor excelFileImportProcessor;

    @Test
    public void testMultiThreadImport()  {
        ImportResult<Void> voidImportResult = excelFileImportProcessor.importFile(new File("D:\\YCJK\\multi-segment-excel-import-spring-boot-starter\\src\\test\\resources\\demo_excel_data.xlsx"), new FileImportHandler<DemoExcelData>() {
            /**
             * 批量处理数据，成功的记录 每批次只进行一次和数据库交互操作
             *
             * @param records 成功的记录列表
             */
            @Override
            public void batchProcess(List<DemoExcelData> records) {
                records.forEach(System.out::println);
            }
        });
        if (voidImportResult.isSuccess()) {
            System.out.println("全部成功");
        }
        else {
            System.out.println("失败条数:" + voidImportResult.getFailCount());
            System.out.println("成功条数" + voidImportResult.getSuccessCount());
            System.out.println("失败的文件路径: " + voidImportResult.getFailFile().getAbsolutePath());
        }
    }

    @Test
    public void testImport(){
        SegmentInfo<TransactionSummary> segment = new SegmentInfo<>(
                new FileImportHandler<TransactionSummary>() {
                    @Override
                    public void batchProcess(List<TransactionSummary> records) {
                        records.forEach(System.out::println);
                    }
                }, 8, 9, 11
        );
        SegmentInfo<PayStatementDetail> statementDetailSegmentInfo = new SegmentInfo<>(
                new FileImportHandler<PayStatementDetail>() {
                    @Override
                    public void batchProcess(List<PayStatementDetail> records) {
                        records.forEach(System.out::println);
                    }
                }, 14, 15, -1
        );

        List<SegmentInfo<?>> segments = new ArrayList<>();
        segments.add(segment);
        segments.add(statementDetailSegmentInfo);
        File file = new File("C:\\Users\\12092\\Downloads\\123.xlsx");
        ImportResult<Map<SegmentInfo<?>, List<Object>>> process = multiSegmentExcelSaxProcessor.process(file, -1, segments);

        if (process.isSuccess()) {
            System.out.println("全部成功");
        }
        else {
            System.out.println("失败条数:" + process.getFailCount());
            System.out.println("成功条数" + process.getSuccessCount());
            System.out.println("失败的文件路径: " + process.getFailFile().getAbsolutePath());
        }

    }
    @Test
    public void testWechatRobot(){
        WeChatRobotService.SendResult sendResult = weChatRobotService.sendTextToAll("可以收到吗?");
        if(sendResult.isSuccess()){
            System.out.println("应该是收到了");
        }
    }



}
```

**完整使用示例请查看:**


📁 [excelimport](https://github.com/Tianyulife/excel-import-spring-boot-starter/tree/main/src/test/java/io/github/tianyulife/excelimport)

示例包括：
- 如何定义 Excel 映射注解类（TestModel）
- 如何调用 `importExcel(...)` 方法
- 如何处理导入结果

## 🔍 Example Usage
**For a complete usage example, see:**

📁 [excelimport](https://github.com/Tianyulife/excel-import-spring-boot-starter/tree/main/src/test/java/io/github/tianyulife/excelimport)

The example demonstrates:

- How to define an Excel mapping class using annotations (e.g. TestModel)

- How to call the importExcel(...) method

- How to handle the import result (success and failure cases)




## 重大更新

## 🚀 Major Updates


### ✅ 注解驱动的字段映射与校验
使用 @Excel(name = "列名") 或 @Excel(index = 索引) 完成 Excel 与字段的自动映射。

提供内置的字段校验注解，支持以下常用限制：

@MaxInt(value = 50)：整型字段最大值限制，

@MaxDecimal(value = 5000)：BigDecimal 类型的金额限制，

@MinDecimal(value = 0)：BigDecimal 类型的最小值限制

### ✅ Annotation-driven Field Mapping & Validation
Use @Excel(name = "Column Name") or @Excel(index = Index) for automatic Excel column-to-field mapping.
Built-in field validation annotations include:

- @MaxInt(value = 50): Integer field maximum value restriction

- @MaxDecimal(value = 5000): BigDecimal maximum value restriction

- @MinDecimal(value = 0): BigDecimal minimum value restriction (≥ 0)

Supports custom validation annotations and Hibernate Validator integration.


### ✅ 增对单表头新增通用多线程异步导入方法： 更加灵活

### 🧵 Generic Multithreaded Import Support for Flat Headers
Introduced a flexible asynchronous import method specifically designed for single-header Excel files.
Supports large batch processing with task chunking and thread pool execution.

### 📄 自动收集失败数据并写入 CSV 文件
所有解析失败、校验失败的数据均会统一记录并输出到 CSV 文件中，支持自定义文件名、是否追加、是否写入表头等。默认路径 System.getProperty("user.dir")
使用 UTF-8 BOM 输出，避免 Excel 打开乱码。
提供工具类 CsvWriteUtils：

### 📄 Automatic CSV Export for Failed Data
- All rows that fail to parse or validate will be collected and exported to a CSV file.

- Output location: System.getProperty("user.dir") by default

- Output format: UTF-8 with BOM to avoid Excel encoding issues

- Supports: custom file name, append mode, optional header line

- Utility provided: CsvWriteUtils


### 🔁 异步事务执行器支持
提供自动配置的 TransactionExecutor 组件，基于 Spring TransactionTemplate 实现异步环境下的事务处理。

自动注入 TaskExecutor 与事务模板，无需手动配置。

提供兜底的 NoOpTransactionExecutor，在未配置事务的情况下也可执行导入逻辑（无事务回滚）

### 🔁 Asynchronous Transaction Executor Support
A configurable TransactionExecutor is provided for executing import logic inside asynchronous threads with transaction control.

- Automatically configures TransactionTemplate and TaskExecutor

- Fallback to NoOpTransactionExecutor when no transaction manager is available (no rollback support)

- Defined in: TransactionExecutorAutoConfiguration



### 新增企业微信群机器人

### 🤖 WeChat Work Robot Integration (Optional)
Send formatted notification messages to a WeChat Work group via robot webhook during or after import.
Can be used to push summaries of success/failure counts, file URLs, or business alerts.




#### 功能介绍 / Features

本功能提供了基于 Spring Boot 的企业微信群机器人自动配置支持。通过简单的配置即可启用机器人消息推送服务，方便项目中快速集成企业微信群消息通知。

- 通过 wechat.robot.enable 开关控制是否启用该功能。

- 通过 wechat.robot.webhook-url 配置机器人 Webhook 地址，支持文本、Markdown 等格式消息发送。

- 自动装配 WeChatRobotService，提供易用的发送接口。

- 支持灵活集成，可用于导入任务完成通知、异常报警等场景。

This feature provides Spring Boot auto-configuration support for WeChat Work group chat robots. By simply configuring properties, you can enable robot message push service, making it easy to integrate WeChat Work notifications into your projects.

- Controlled by the switch wechat.robot.enable to enable or disable the feature.

- the robot Webhook URL via wechat.robot.webhook-url, supporting text, markdown, and other message formats.

- Auto-configures the WeChatRobotService, offering simple APIs to send messages.

- Flexible integration for scenarios like import completion notifications, error alerts, and more.



#### 企业微信群机器人自动配置（WeChatRobotProperties）
**本模块基于 Spring Boot 自动配置，方便集成企业微信群机器人的消息推送功能。**

**配置说明**
| 配置项                        | 说明                   | 示例                                                             | 必填 |
| -------------------------- | -------------------- | -------------------------------------------------------------- | -- |
| `wechat.robot.enable`      | 是否启用企业微信群机器人功能       | `true` 或 `false`（默认不启用）                                        | 是  |
| `wechat.robot.webhook-url` | 企业微信群机器人的 Webhook 地址 | `https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=xxxxxxx` | 是  |

使用示例（application.yml）
```yaml
wechat:
  robot:
    enable: true
    webhook-url: https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=YOUR_WEBHOOK_KEY
```