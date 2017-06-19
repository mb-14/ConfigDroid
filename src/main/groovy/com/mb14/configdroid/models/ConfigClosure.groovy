package com.mb14.configdroid.models
import com.mb14.configdroid.utils.FieldUtils

class ConfigClosure {

    Map<String, BaseField> fields = new HashMap<>();

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

    public void file(String key, File file) {
        addFile(key, file);
    }

    HashMap<String, BaseField> getFields() {
        return fields;
    }

    private void addProperty(String key, Object value, Class clazz) {
        if (value instanceof List) {
            clazz = FieldUtils.getListType(value)
            if (clazz == null)
                return
            fields.put(key, new ListField(clazz, value))
        }
        else {
            fields.put(key, new PropField(clazz, value))
        }
    }

    private void addProperty(String type, String key, String value) {
        if (FieldUtils.isArrayNotation(FieldUtils.getSimpleName(type))) {
            fields.put(key, new ListField(type, value))
        }
        else {
            fields.put(key, new PropField(type, value))
        }
    }

    private void addFile(String key, File file) {
        fields.put(key, new FileField(file));
    }

}