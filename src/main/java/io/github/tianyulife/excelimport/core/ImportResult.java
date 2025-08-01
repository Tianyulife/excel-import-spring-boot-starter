package io.github.tianyulife.excelimport.core;

import lombok.Data;

import java.io.File;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/8/1 11:32
 * @Description: io.github.tianyulife.excelimport.core
 */
@Data
public class ImportResult<T> {

    /**
     * 是否导入成功（true 表示全量成功，false 表示有失败）
     */
    private boolean success;

    /**
     * 失败数据文件（可用于下载或上传）
     */
    private File failFile;

    /**
     * 成功数量
     */
    private long successCount;

    /**
     * 失败数量
     */
    private long failCount;

    /**
     * 成功导入的业务数据，泛型T，用户自己决定传什么类型
     */
    private T successData;

}


