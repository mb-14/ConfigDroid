package com.mb14.configdroid.models
import com.mb14.configdroid.utils.FieldUtils

class ConfigClosure {

    Map<String, ConfigField> properties = new HashMap<>();

    public void prop(String key, String value) {
        addProperty(key, value, String.class)
    }

    public void prop(String key, long value) {
        addProperty(key, value, Long.class)
    }

    public void prop(String key, int value) {
        addProperty(key, value, Integer.class)
    }
    public void prop(String key, boolean value) {
        addProperty(key, value, Boolean.class)
    }

    public void prop(String key, float value) {
        addProperty(key, value, Float.class)
    }

    public void prop(String key, double value) {
        addProperty(key, value, Double.class)
    }

    public void prop(String key, byte value) {
        addProperty(key, value, Byte.class)
    }

    public void prop(String key, char value) {
        addProperty(key, value, Character.class)
    }

    public void prop(String key, List<?> value) {
        addProperty(key, value, value.class)
    }

    public void prop(String type, String key, String value) {
        addProperty(type, key, value)
    }

    Map<String, ConfigField> getProperties() {
        return properties;
    }

    private void addProperty(String key, Object value, Class clazz) {
        if (value instanceof List) {
            clazz = FieldUtils.getListType(value)
            if (clazz == null)
                return
        }
        ConfigField configField = new ConfigField(clazz, value)
        properties.put(key, configField)
    }

    private void addProperty(String type, String key, String value) {
        ConfigField configField = new ConfigField(type, value)
        properties.put(key, configField)
    }

}