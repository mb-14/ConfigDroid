package com.mb14.configdroid.models

import com.squareup.javapoet.TypeName

class FileField extends BaseField {

    File file;

    FileField(File file){
        this.file = file
    }

    @Override
    String getValue() {
        return file.text
    }

    @Override
    String getInitializerType() {
        return "\$S"
    }

    @Override
    TypeName getValueType() {
        return TypeName.get(String.class)
    }

}
