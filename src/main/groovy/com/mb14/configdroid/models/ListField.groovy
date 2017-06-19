package com.mb14.configdroid.models

import com.mb14.configdroid.utils.FieldUtils
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.TypeName

class ListField extends PropField {

    ListField(String className, String value) {
        super(className, value)
        //Remove box notation
        simpleName = simpleName.substring(0, simpleName.length() - 2)
    }
    ListField(Class clazz, Object value) {
        super(clazz, value)
    }

    @Override
    TypeName getValueType() {
        return ArrayTypeName.of(FieldUtils.getClassType(this))
    }

    @Override
    String getFormattedValue(Object object, String simpleName) {
        List<?> objects = (List) object;
        String value = String.format("new %s[]{", simpleName)
        for (int i = 0; i < objects.size() - 1; i++) {
            value += FieldUtils.getFormattedValue(objects[i], simpleName) + ","
        }
        value += FieldUtils.getFormattedValue(objects[objects.size() - 1], simpleName) + "}"
        return value
    }
}
