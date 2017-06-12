package com.mb14.configdroid.models

import com.mb14.configdroid.utils.FieldUtils
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.TypeName

class ConfigField implements Serializable {

    static final long serialVersionUID = 4851015942761796149L;

    String value
    boolean isList
    String simpleName
    String packageName

    ConfigField(String className, String value) {
        this.simpleName = FieldUtils.getSimpleName(className)
        this.packageName = FieldUtils.getPackageName(className)
        this.isList = FieldUtils.isArrayNotation(simpleName)
        if (isList) {
            //Remove box notation
            this.simpleName = simpleName.substring(0, simpleName.length() - 2)
        }
        this.value = value;
    }

    ConfigField(Class clazz, Object value) {
        Class fieldClass = clazz
        this.isList = value instanceof List
        this.packageName = fieldClass.getPackage().getName()
        this.simpleName = fieldClass.getSimpleName()
        this.value = FieldUtils.getFormattedValue(value, simpleName)
    }

    TypeName getType() {
        if (isList) {
            return ArrayTypeName.of(FieldUtils.getClassType(this))
        }
        else {
            return FieldUtils.getClassType(this)
        }
    }
}
