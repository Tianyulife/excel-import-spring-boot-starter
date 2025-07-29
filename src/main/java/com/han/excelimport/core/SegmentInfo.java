package com.han.excelimport.core;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/28 16:57
 * @Description: com.han.excelimport.core
 */

import lombok.Getter;

@Getter
public class SegmentInfo<T> {
    private final FileImportHandler<T> handler;
    private final int headerRow;
    private final int startRow;
    private final int endRow;

    public SegmentInfo(FileImportHandler<T> handler, int headerRow, int startRow, int endRow) {
        this.handler = handler;
        this.headerRow = headerRow;
        this.startRow = startRow;
        this.endRow = endRow;
    }
}

