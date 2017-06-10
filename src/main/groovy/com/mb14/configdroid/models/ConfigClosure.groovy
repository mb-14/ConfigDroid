package com.mb14.configdroid.models

class ConfigClosure {

    Map<String, Object> properties = new HashMap<>();

    public void prop(String name, Object value) {
        properties.put(name, value);
    }

    Map<String, Object> getProperties() {
        return properties;
    }
}