package io.github.tianyulife.excelimport.core;

import cn.hutool.core.lang.UUID;
import cn.hutool.poi.excel.ExcelUtil;
import io.github.tianyulife.excelimport.constant.FileConstant;
import io.github.tianyulife.excelimport.transaction.TransactionExecutor;
import io.github.tianyulife.excelimport.util.CsvWriteUtils;
import io.github.tianyulife.excelimport.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/8/1 11:29
 * @Description: io.github.tianyulife.excelimport.core
 */
@Slf4j
@Component
public class ExcelFileImportProcessor {


    @Autowired
    private Executor executor;

    @Autowired(required = false)
    private TransactionExecutor transactionExecutor;

    private static final int NUM_PER_PROCESS = 2000;

     public <T> CompletableFuture<ImportResult<Void>> importFile(File tempFile, FileImportHandler<T> handler){
        return importFile(tempFile, handler, null);
    }

    @Async
    public <T> CompletableFuture<ImportResult<Void>> importFile(File tempFile, FileImportHandler<T> handler,ImportContext context){

        AtomicLong rows = new AtomicLong();
        AtomicLong fails = new AtomicLong();
        AtomicLong successNum = new AtomicLong();
        String errFileName = UUID.randomUUID().toString();

        AtomicReference<List<String>> stringList = new AtomicReference<>();
        AtomicBoolean titleWritten = new AtomicBoolean(false);
        List<String> titleList = new ArrayList<>();
        List<List<String>> failBuffer = Collections.synchronizedList(new ArrayList<>());
        List<T> successBuffer = new ArrayList<>();
        List<Map<String, Object>> rawBuffer = new ArrayList<>();

        List<CompletableFuture<Void>> tasks = new ArrayList<>();

        ExcelUtil.readBySax(tempFile, 0, (sheetIndex, rowIndex, rowCells) -> {
            if (rowIndex == 0) {
                stringList.set(rowCells.stream().map(Object::toString).collect(Collectors.toList()));
            } else {
                Map<String, Object> rowMap = new HashMap<>();
                Map<Integer, Object> indexRowMap = new HashMap<>();
                for (int i = 0; i < rowCells.size(); i++) {
                    rowMap.put(stringList.get().get(i), rowCells.get(i));
                    indexRowMap.put(i, rowCells.get(i));
                }

                RowHandleResult<T> result = handler.handleRow(rowMap,indexRowMap);

                if (result.isSuccess()) {
                    successBuffer.add(result.getRecord());
                    rawBuffer.add(rowMap);

                    if (successBuffer.size() >= NUM_PER_PROCESS) {
                        tasks.add(submitBatch(handler, successBuffer, rawBuffer, stringList.get(),
                                titleList, titleWritten, errFileName, fails, successNum,failBuffer,context));
                        successBuffer.clear();
                        rawBuffer.clear();
                    }
                } else {
                    rowMap.put("失败原因", result.getErrorMsg());
                    writeFailRow(rowMap, stringList.get(), titleList, titleWritten, errFileName, failBuffer);
                    fails.incrementAndGet();
                }
            }
            rows.set(rowIndex);
        });

        // 处理剩余成功数据
        if (!successBuffer.isEmpty()) {
            tasks.add(submitBatch(handler, successBuffer, rawBuffer, stringList.get(),
                    titleList, titleWritten, errFileName, fails, successNum,failBuffer,context));
        }


        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();
        // 写入剩余失败数据
        synchronized (failBuffer){
            if (!failBuffer.isEmpty()) {
                CsvWriteUtils.writeDataToCsv(failBuffer, errFileName, true, false);
            }
        }

        ImportResult<Void> importResult = new ImportResult<>();
        if (fails.get() == 0) {
            importResult.setSuccess(true);
        }else {
            importResult.setSuccess(false);
            importResult.setFailFile(new File(System.getProperty("user.dir"), errFileName + FileConstant.CSV_SUFFIX_CSV));
        }
        importResult.setSuccessCount(successNum.get());
        importResult.setFailCount(fails.get());
        return CompletableFuture.completedFuture(importResult);
    }

    private <T> CompletableFuture<Void> submitBatch(
            FileImportHandler<T> handler,
            List<T> batchList,
            List<Map<String, Object>> rawData,
            List<String> headers,
            List<String> titleList,
            AtomicBoolean titleWritten,
            String errFileName,
            AtomicLong fails,
            AtomicLong successNum,
            List<List<String>> failBuffer,ImportContext context) {

        List<T> batchCopy = new ArrayList<>(batchList);
        List<Map<String, Object>> rawCopy = new ArrayList<>(rawData);

        return CompletableFuture.runAsync(() -> {
            try {
                transactionExecutor.runInTransaction(() -> {
                    handler.batchProcess(batchCopy,context);
                });
                successNum.addAndGet(batchCopy.size());
                log.info(" 批处理成功，累计成功记录数：{}", successNum.get());// 批处理成功后，统一加成功数
            } catch (Exception e) {
                log.error(" 批处理失败，记录未计入成功数，本批数据：{}", batchCopy, e);
                log.error("批量处理异常: {}", batchCopy, e);
                String errorMsg = StringUtils.substring(e.getMessage(), 0, 200);
                for (Map<String, Object> row : rawCopy) {
                    row.put("失败原因", errorMsg);
                    writeFailRow(row, headers, titleList, titleWritten, errFileName, failBuffer);
                    fails.incrementAndGet();
                }

                synchronized (failBuffer) {
                    if (failBuffer.size() >= NUM_PER_PROCESS) {
                        CsvWriteUtils.writeDataToCsv(failBuffer, errFileName, true, false);
                        failBuffer.clear();
                    }
                }
            }
        }, executor);
    }

    private void writeFailRow(
            Map<String, Object> row,
            List<String> headers,
            List<String> titleList,
            AtomicBoolean titleWritten,
            String fileName,
            List<List<String>> buffer) {


        if (titleWritten.compareAndSet(false, true)) {
            titleList.addAll(headers);
            titleList.add("失败原因");
            CsvWriteUtils.writeSingleDataToCsv(titleList, fileName, true, true);
        }


        List<String> values = titleList.stream()
                .map(key -> {
                    Object val = row.get(key);
                    return ObjectUtils.isEmpty(val) ? "" : val.toString();
                })
                .collect(Collectors.toList());

        synchronized (buffer) {
            buffer.add(values);
        }
    }





}
