package io.github.tianyulife.excelimport.core;

import java.util.HashMap;
import java.util.Map;

public class ImportContext {

    /** 通用动态参数容器 */
    private Map<String, Object> ext = new HashMap<>();

    public ImportContext put(String key, Object value) {
        ext.put(key, value);
        return this;
    }

    public Object get(String key) {
        return ext.get(key);
    }

    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(ext.get(key));
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }
}
