package io.github.tianyulife.excelimport.core;


import io.github.tianyulife.excelimport.annotation.Excel;
import io.github.tianyulife.excelimport.util.DateUtils;
import io.github.tianyulife.excelimport.util.ExcelHandlerAdapter;
import io.github.tianyulife.excelimport.util.SpringUtils;
import io.github.tianyulife.excelimport.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/7/23 11:18
 * @Description: com.ruoyi.common.core.utils.poi
 */

public interface FileImportHandler<T> {
    /**
     * 默认实现的处理方法,
     * @param row 数据行
     * @return RowHandleResult<T> 处理结果
     * 如果要对数据进行特殊校验,请自信实行该方法,或者先调用该方法 再自行校验
     */

    /**
     * 简化版本，仅使用字段名
     */
//    default RowHandleResult<T> handleRow(Map<String, Object> row) {
//        // 为了兼容旧逻辑，构造一个空的 indexRow
//        return handleRow(row, Collections.emptyMap());
//    }


    default RowHandleResult<T> handleRow(Map<String, Object> row, Map<Integer, Object> indexRow) {
        try {
            ParameterizedType type = null;
            for (Type iface : getClass().getGenericInterfaces()) {
                if (iface instanceof ParameterizedType &&
                        ((ParameterizedType) iface).getRawType() == FileImportHandler.class) {
                    type = (ParameterizedType) iface;
                    break;
                }
            }
            if (type == null) {
                throw new IllegalStateException("无法获取泛型类型 T");
            }

            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) type.getActualTypeArguments()[0];
            T record = clazz.getDeclaredConstructor().newInstance();

            for (Field field : record.getClass().getDeclaredFields()) {
                Excel excelAnnotation = field.getAnnotation(Excel.class);
                if (excelAnnotation != null) {
                    field.setAccessible(true);
                    Object value = null;


                    // 1. 优先通过 index 获取
                    if (excelAnnotation.index() > 0) {
                        int idx = excelAnnotation.index() - 1;
                        if (idx < indexRow.size()) {
                            value = indexRow.get(idx);
                        }
                    }

                    // 2. 如果 index 获取不到，再尝试通过 name 获取
                    else if (!excelAnnotation.name().isEmpty()) {
                        value = row.get(excelAnnotation.name());
                    }
                    // 赋值或默认值
                    if (value != null) {
                        Object convertedValue = convertValue(field.getType(), value, excelAnnotation);
                        field.set(record, convertedValue);
                    } else {
                        String defaultValue = excelAnnotation.defaultValue();
                        if (!defaultValue.isEmpty()) {
                            Object convertedDefault = convertValue(field.getType(), defaultValue, excelAnnotation);
                            field.set(record, convertedDefault);
                        }
                    }
                }
            }

            Validator validator = SpringUtils.getBean(Validator.class);
            Set<ConstraintViolation<T>> violations = validator.validate(record);
            if (!violations.isEmpty()) {
                String errorMsg = violations.stream()
                        .map(v -> v.getPropertyPath() + " " + v.getMessage())
                        .collect(Collectors.joining("; "));
                return RowHandleResult.fail("字段校验失败: " + errorMsg);
            }

            return RowHandleResult.success(record);
        } catch (Exception e) {
            return RowHandleResult.fail("处理失败: " + StringUtils.substring(e.getMessage(), 0, 200));
        }
    }


    static Object convertValue(Class<?> fieldType, Object value, Excel excelAnnotation) {

        if (value == null) {
            return null;
        }

        String raw = String.valueOf(value).trim();

        // =============================
// 1）优先执行 Excel.handler()
// =============================
        if (excelAnnotation.handler() != null && !excelAnnotation.handler().equals(ExcelHandlerAdapter.class)) {
            try {
                ExcelHandlerAdapter handler = (ExcelHandlerAdapter) excelAnnotation.handler().newInstance();
                return handler.format(value, excelAnnotation.args());

            } catch (Exception e) {
                throw new RuntimeException("Excel.handler 处理失败: " + excelAnnotation.handler().getSimpleName(), e);
            }
        }
        if (raw.isEmpty()) return null ;
        if (fieldType.equals(String.class)) {
            return raw;
        } else if (fieldType.equals(BigDecimal.class)) {
            BigDecimal bigDecimal = new BigDecimal(raw);
            if (excelAnnotation.scale() != -1) {
                bigDecimal = bigDecimal.setScale(excelAnnotation.scale(), excelAnnotation.roundingMode());
            }
            return bigDecimal;
        } else if (fieldType.equals(Integer.class)) {
            // 先转BigDecimal，避免 "30.0" 解析失败
            BigDecimal bd = new BigDecimal(raw);
            return bd.setScale(0, RoundingMode.DOWN).intValueExact();
        } else if (fieldType.equals(Long.class)) {
            BigDecimal bd = new BigDecimal(raw);
            return bd.setScale(0, RoundingMode.DOWN).longValueExact();
        } else if (fieldType.equals(Double.class)) {
            return Double.parseDouble(raw);
        } else if (fieldType.equals(Boolean.class)) {
            return Boolean.parseBoolean(raw);
        } else if (fieldType.equals(Date.class)) {
            return DateUtils.parseDate(raw);
        }  else if (fieldType.equals(LocalDateTime.class)){
            return DateUtils.parseStringToLocalDateTime(raw, excelAnnotation.dateFormat());
        } else if (fieldType.equals(LocalDate.class)) {
            // 如果 Excel 是 yyyy-MM-dd 格式
            return DateUtils.parseStringToLocalDate(raw, excelAnnotation.dateFormat());
        }
        else {
            return raw;
        }
    }





    // 新增：支持带 ImportContext 的 batchProcess
    default void batchProcess(List<T> list, ImportContext ctx) {
        batchProcess(list); // 自动兼容旧的 Handler
    }

    // 保留旧的 batchProcess
     default void batchProcess(List<T> list) {}



}
