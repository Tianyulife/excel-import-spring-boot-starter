package io.github.tianyulife.excelimport;

import io.github.tianyulife.excelimport.core.FileImportHandler;
import io.github.tianyulife.excelimport.core.MultiSegmentExcelSaxProcessor;
import io.github.tianyulife.excelimport.core.SegmentInfo;
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
    @Autowired(required = false)
    private WeChatRobotService weChatRobotService;

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
        multiSegmentExcelSaxProcessor.process(file, -1, segments);
    }
    @Test
    public void testWechatRobot(){
        WeChatRobotService.SendResult sendResult = weChatRobotService.sendTextToAll("可以收到吗?");
        if(sendResult.isSuccess()){
            System.out.println("应该是收到了");
        }
    }



}
