package com.han.excelimport;

import com.han.excelimport.core.FileImportHandler;
import com.han.excelimport.core.MultiSegmentExcelSaxProcessor;
import com.han.excelimport.core.SegmentInfo;
import com.han.excelimport.domain.PayStatementDetail;
import com.han.excelimport.domain.TransactionSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

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
public class ExcelImportServiceTest {

    @Autowired
    private MultiSegmentExcelSaxProcessor multiSegmentExcelSaxProcessor;

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



}
