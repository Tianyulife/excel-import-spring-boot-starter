package io.github.tianyulife.excelimport.exception;

import java.util.List;
import java.util.Map;

/**
 * 自定义异常：当 handler 遇到 SQL/业务异常且希望由 handler 决定哪些行失败时抛出。
 * 使用方式示例：在 handler 内 throw new ImportBizException("msg") {
 *     @Override
 *     public List<Map<String, Object>> processRawCopy(List<Map<String, Object>> rawCopy) {
 *         // 对 rawCopy 做处理，返回需要写入 CSV 的行集合（可以是原 list 的子集或新的 list）
 *     }
 * };
 */
public abstract class ImportBizException extends RuntimeException {

    public ImportBizException(String message) {
        super(message);
    }

    public ImportBizException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 框架在 catch 到此异常时会调用该方法，将当前批次的 rawCopy 传入，
     * 由异常（也就是由 handler 提供的实现）决定哪些行需要写失败 CSV，
     * 方法应返回要写入 CSV 的行集合（List<Map<String,Object>>）。
     *
     * 注意：实现可以选择就地修改 rawCopy 的行（比如 put("失败原因", "...")），
     * 并返回这些被修改过的行；也可以返回全新的行集合。
     */
    public abstract List<Map<String, Object>> processRawCopy(List<Map<String, Object>> rawCopy);
}
