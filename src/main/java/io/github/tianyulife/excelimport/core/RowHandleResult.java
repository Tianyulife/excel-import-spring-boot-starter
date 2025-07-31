package io.github.tianyulife.excelimport.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/28 17:13
 * @Description: com.han.excelimport.core
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowHandleResult<T> {
    private boolean success;
    private T record;
    private String errorMsg;

    public static <T> RowHandleResult<T> success(T record) {
        RowHandleResult<T> result = new RowHandleResult<>();
        result.setSuccess(true);
        result.setRecord(record);
        return result;
    }

    public static <T> RowHandleResult<T> fail(String errorMsg) {
        RowHandleResult<T> result = new RowHandleResult<>();
        result.setSuccess(false);
        result.setErrorMsg(errorMsg);
        return result;
    }

}

