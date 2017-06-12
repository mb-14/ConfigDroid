package com.mb14.configdroid.utils

import com.mb14.configdroid.models.ConfigField
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName

class FieldUtils {

    private static final PRIMITIVE_MAP = [
            "int"     : TypeName.INT,
            "float"   : TypeName.FLOAT,
            "boolean" : TypeName.BOOLEAN,
            "char"    : TypeName.CHAR,
            "long"    : TypeName.LONG,
            "byte"    : TypeName.BYTE
    ]

    private static final VALUE_FORMAT_MAP = [
            "Long"      : "%dL",
            "String"    : "\"%s\"",
            "Float"     : "%fF",
            "Double"    : "%fD",
            "Character" : "\'%c\'"
    ]

    /**
     * Merge patch into target without overriding existing keys
     * Preference order: build type > product flavor > global
     * @param target
     * @param patch
     */
    static void mergeMap(HashMap target, HashMap patch) {
        Map tmp = new HashMap(patch);
        tmp.keySet().removeAll(target.keySet());
        target.putAll(tmp);
    }

    static Class getListType(List<?> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0).class
    }

    static TypeName getClassType(ConfigField configField) {
        if (configField.packageName == null || configField.packageName.isEmpty()) {
            return PRIMITIVE_MAP.containsKey(configField.simpleName) ?
                    PRIMITIVE_MAP.get(configField.simpleName) : TypeName.forName(configField.simpleName)
        }
        else {
            return ClassName.get(configField.packageName, configField.simpleName)
        }
    }

    static String getSimpleName(String className) {
        int lastIndex = className.lastIndexOf('.')
        if (lastIndex == -1) return className
        return className.substring(lastIndex + 1)
    }

    static String getPackageName(String className) {
        int lastIndex = className.lastIndexOf('.')
        if (lastIndex == -1) return null
        return className.substring(0, lastIndex)
    }

    static boolean isArrayNotation(String simpleName) {
        return simpleName ==~ /^[a-zA-Z_$][a-zA-Z_$0-9]*\[\]$/
    }

    static String getFormattedValue(Object object, String simpleName) {
        if (object instanceof List) {
            List<?> objects = (List) object;
            String value = String.format("new %s[]{", simpleName)
            for (int i = 0; i < objects.size() - 1; i++) {
                value += getFormattedValue(objects[i], simpleName) + ","
            }
            value += getFormattedValue(objects[objects.size() - 1], simpleName) + "}"
            return value
        }
        return VALUE_FORMAT_MAP.containsKey(simpleName) ?
                String.format(VALUE_FORMAT_MAP.get(simpleName), object) : String.valueOf(object)
    }
}
