package io.github.tianyulife.excelimport.core;

import cn.hutool.core.lang.UUID;
import cn.hutool.poi.excel.ExcelUtil;
import io.github.tianyulife.excelimport.constant.FileConstant;
import io.github.tianyulife.excelimport.util.CsvWriteUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/23 14:26
 * @Description: com.ruoyi.common.core.utils.poi
 */
@Component
@Slf4j
public class MultiSegmentExcelSaxProcessor {


    public  <T> List<T> getTypedData(Map<SegmentInfo<?>, List<Object>> resultMap, SegmentInfo<T> segment) {
        @SuppressWarnings("unchecked")
        List<T> typedList = (List<T>) resultMap.get(segment);
        return typedList;
    }

    /**
     * 多段分区读取Excel，每段支持独立标题行
     *
     * @param file        Excel 文件
     * @param sheetIndex  sheet 索引，-1 表示全部
     * @param segments    段落定义列表
     *
     */

    public  ImportResult<Map<SegmentInfo<?>, List<Object>>> process(File file, int sheetIndex, List<SegmentInfo<?>> segments){
        return process(file, sheetIndex, segments, null);
    }


    public  ImportResult<Map<SegmentInfo<?>, List<Object>>> process(File file, int sheetIndex, List<SegmentInfo<?>> segments,ImportContext importContext) {
        Map<SegmentInfo<?>, List<Object>> segmentSuccessData = new LinkedHashMap<>();
        Map<SegmentInfo<?>, Map<Integer, String>> segmentHeaderMaps = new LinkedHashMap<>();

        // 用于收集失败行，List 中每个元素是失败行的字段值列表，末尾额外加一列失败原因
        Map<SegmentInfo<?>, List<List<String>>> segmentFailData = new LinkedHashMap<>();

        // 初始化缓存结构
        for (SegmentInfo<?> segment : segments) {
            segmentSuccessData.put(segment, new ArrayList<>());
            segmentHeaderMaps.put(segment, new HashMap<>());
            segmentFailData.put(segment, new ArrayList<>());
        }
        AtomicLong endRow = new AtomicLong();
        ExcelUtil.readBySax(file, sheetIndex, (sheet, rowIndexZeroBased, rowList) -> {
            long rowNum = rowIndexZeroBased + 1;
            endRow.set(rowNum);
            for (SegmentInfo<?> segment : segments) {
                if (rowNum == segment.getHeaderRow()) {
                    Map<Integer, String> headerMap = segmentHeaderMaps.get(segment);
                    headerMap.clear();
                    for (int i = 0; i < rowList.size(); i++) {
                        headerMap.put(i, String.valueOf(rowList.get(i)).trim());
                    }
                    return;
                }
            }

            for (SegmentInfo<?> segment : segments) {
                if (rowNum >= segment.getStartRow() && (rowNum <= segment.getEndRow() || segment.getEndRow() < 0)) {
                    Map<Integer, String> headerMap = segmentHeaderMaps.get(segment);
                    if (headerMap.isEmpty()) {
                       log.error("第{}行读取数据，但段 {} 的标题（行 {}）尚未读取到！\n",
                                rowNum,
                                segment.getHandler().getClass().getSimpleName(),
                                segment.getHeaderRow()
                        );
                        return;
                    }

                    Map<String, Object> nameRowMap = new HashMap<>();
                    Map<Integer, Object> indexRowMap = new HashMap<>();

                    for (int i = 0; i < rowList.size(); i++) {
                        String key = headerMap.get(i);
                        if (key != null && !key.isEmpty()) {
                            nameRowMap.put(key, rowList.get(i));
                        }
                        indexRowMap.put(i, rowList.get(i));
                    }

                    @SuppressWarnings("unchecked")
                    FileImportHandler<Object> handler = (FileImportHandler<Object>) segment.getHandler();
                    RowHandleResult<Object> res = handler.handleRow(nameRowMap, indexRowMap);

                    if (res.isSuccess()) {
                        segmentSuccessData.get(segment).add(res.getRecord());
                    } else {
                       log.error("行 {} 处理失败：{}\n", rowNum, res.getErrorMsg());

                        // 收集失败数据，转换成字符串列表，末尾追加错误信息
                        List<String> failRow = new ArrayList<>();
                        for (int i = 0; i < rowList.size(); i++) {
                            failRow.add(rowList.get(i) == null ? "" : rowList.get(i).toString());
                        }
                        failRow.add(res.getErrorMsg());
                        segmentFailData.get(segment).add(failRow);
                    }
                    break;
                }
            }
        });

        String errFileName = UUID.randomUUID().toString();
        AtomicBoolean titleWritten = new AtomicBoolean(false);


        for (SegmentInfo<?> segment : segments) {
            List<Object> dataList = segmentSuccessData.get(segment);
            if (!dataList.isEmpty()) {
                long end = segment.getEndRow() == -1 ? endRow.get() : segment.getEndRow();
                log.info("处理段 [行{}-{}] 共 {} 条数据\n",
                        segment.getStartRow(),end , dataList.size());

                @SuppressWarnings("unchecked")
                FileImportHandler<Object> handler = (FileImportHandler<Object>) segment.getHandler();
                handler.batchProcess(dataList,importContext);

                List<List<String>> failRows = segmentFailData.get(segment);
                if (!failRows.isEmpty()){
                    Map<Integer, String> headerMap = segmentHeaderMaps.get(segment);
                    List<String> headers = new ArrayList<>();
                    // 按列序号排序，取标题
                    int maxCol = headerMap.keySet().stream().max(Integer::compareTo).orElse(-1);
                    for (int i = 0; i <= maxCol; i++) {
                        headers.add(headerMap.getOrDefault(i, "列" + i));
                    }
                    headers.add("错误原因");
                    CsvWriteUtils.writeSingleDataToCsv(headers, errFileName, true, titleWritten.compareAndSet(false, true));
                    CsvWriteUtils.writeDataToCsv(failRows,errFileName,true, false);
                }
            }
        }

        long totalSuccess = segmentSuccessData.values().stream()
                .mapToLong(List::size)
                .sum();

        long totalFail = segmentFailData.values().stream()
                .mapToLong(List::size)
                .sum();

        log.info("导入结果：成功 {} 条，失败 {} 条", totalSuccess, totalFail);

        ImportResult<Map<SegmentInfo<?>, List<Object>>> mapImportResult = new ImportResult<>();
        if (totalFail == 0){
            mapImportResult.setSuccess(true);
        } else {
            mapImportResult.setSuccess(false);
            mapImportResult.setFailFile(new File(System.getProperty("user.dir"), errFileName + FileConstant.CSV_SUFFIX_CSV));
        }
        mapImportResult.setSuccessCount(totalSuccess);
        mapImportResult.setFailCount(totalFail);
        mapImportResult.setSuccessData(segmentSuccessData);

        return mapImportResult;
    }

}

