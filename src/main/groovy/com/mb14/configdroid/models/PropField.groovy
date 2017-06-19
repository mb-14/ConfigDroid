package com.mb14.configdroid.models

import com.mb14.configdroid.utils.FieldUtils
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.TypeName

class PropField extends BaseField {

    String value
    protected String simpleName
    protected String packageName

    PropField(String className, String value) {
        this.simpleName = FieldUtils.getSimpleName(className)
        this.packageName = FieldUtils.getPackageName(className)
        this.value = value;
    }

    PropField(Class clazz, Object value) {
        Class fieldClass = clazz
        this.packageName = fieldClass.getPackage().getName()
        this.simpleName = fieldClass.getSimpleName()
        this.value = getFormattedValue(value, simpleName)
    }

    @Override
    TypeName getValueType() {
        return FieldUtils.getClassType(this)
    }

    @Override
    String getValue() {
        return value;
    }

    @Override
    String getInitializerType() {
        return "\$L"
    }

    String getFormattedValue(Object value, String simpleName){
        FieldUtils.getFormattedValue(value, simpleName)
    }
}
