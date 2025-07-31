package com.han.excelimport.core;

import cn.hutool.poi.excel.ExcelUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/23 14:26
 * @Description: com.ruoyi.common.core.utils.poi
 */
@Component
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
    public  Map<SegmentInfo<?>, List<Object>> process(File file, int sheetIndex, List<SegmentInfo<?>> segments) {
        Map<SegmentInfo<?>, List<Object>> segmentSuccessData = new LinkedHashMap<>();
        Map<SegmentInfo<?>, Map<Integer, String>> segmentHeaderMaps = new LinkedHashMap<>();

        // 初始化缓存结构
        for (SegmentInfo<?> segment : segments) {
            segmentSuccessData.put(segment, new ArrayList<>());
            segmentHeaderMaps.put(segment, new HashMap<>());
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
                        System.err.printf("第%d行读取数据，但段 [%s] 的标题（行 %d）尚未读取到！\n",
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
                        System.err.printf("行 %d 处理失败：%s\n", rowNum, res.getErrorMsg());
                    }
                    break;
                }
            }
        });

        for (SegmentInfo<?> segment : segments) {
            List<Object> dataList = segmentSuccessData.get(segment);
            if (!dataList.isEmpty()) {
                long end = segment.getEndRow() == -1 ? endRow.get() : segment.getEndRow();
                System.out.printf("处理段 [行%d-%d] 共 %d 条数据\n",
                        segment.getStartRow(),end , dataList.size());

                @SuppressWarnings("unchecked")
                FileImportHandler<Object> handler = (FileImportHandler<Object>) segment.getHandler();
                handler.batchProcess(dataList);
            }
        }

        return segmentSuccessData;
    }

}

