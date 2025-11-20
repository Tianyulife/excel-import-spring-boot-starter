package io.github.tianyulife.excelimport;

import io.github.tianyulife.excelimport.core.*;
import io.github.tianyulife.excelimport.domain.DemoExcelData;
import io.github.tianyulife.excelimport.domain.PayStatementDetail;
import io.github.tianyulife.excelimport.domain.TransactionSummary;
import io.github.tianyulife.excelimport.notify.WeChatRobotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/31 11:32
 * @Description: com.han.excelimport
 */
@SpringBootTest(classes = TestConfig.class)
@ExtendWith(SpringExtension.class)
public class ExcelImportServiceTest {
    @Autowired
    private MultiSegmentExcelSaxProcessor multiSegmentExcelSaxProcessor;
//    @Autowired(required = false)
//    private WeChatRobotService weChatRobotService;
    @Autowired
    private ExcelFileImportProcessor excelFileImportProcessor;

    @Test
    public void testMultiThreadImport()  {
         excelFileImportProcessor.importFile(new File("D:\\YCJK\\multi-segment-excel-import-spring-boot-starter\\src\\test\\resources\\demo_excel_data.xlsx"), new FileImportHandler<DemoExcelData>() {
            /**
             * 批量处理数据，成功的记录 每批次只进行一次和数据库交互操作
             *
             * @param records 成功的记录列表
             */
            @Override
            public void batchProcess(List<DemoExcelData> records) {
                records.forEach(System.out::println);
            }
        }).thenAccept(importResult -> {
             if (importResult.isSuccess()) {
                 System.out.println("全部成功");
             } else {
                 System.out.println("失败条数: " + importResult.getFailCount());
                 System.out.println("成功条数: " + importResult.getSuccessCount());
                 if (importResult.getFailFile() != null) {
                     System.out.println("失败的文件路径: " + importResult.getFailFile().getAbsolutePath());
                 }
             }
         });

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
            System.out.println("全部成功");}
        else {
            System.out.println("失败条数:" + process.getFailCount());
            System.out.println("成功条数" + process.getSuccessCount());
            System.out.println("失败的文件路径: " + process.getFailFile().getAbsolutePath());
        }

    }
//    @Test
//    public void testWechatRobot(){
//
//        WeChatRobotService weChatRobotService = new WeChatRobotService("");
//
//        WeChatRobotService.SendResult sendResult = weChatRobotService.sendTextToAll("可以收到吗?");
//        if(sendResult.isSuccess()){
//            System.out.println("应该是收到了");
//        }
//    }



}
