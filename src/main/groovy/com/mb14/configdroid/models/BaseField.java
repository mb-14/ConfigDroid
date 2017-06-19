package com.mb14.configdroid.models;


import com.squareup.javapoet.TypeName;

import java.io.Serializable;

public abstract class BaseField implements Serializable {

    static final long serialVersionUID = 4851015942761796149L;

    public abstract String getValue();

    public abstract String getInitializerType();

    public abstract TypeName getValueType();

}
