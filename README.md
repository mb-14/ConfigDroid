ConfigDroid
===
[ ![Download](https://api.bintray.com/packages/mb-14/ConfigDroid/configdroid/images/download.svg) ](https://bintray.com/mb-14/ConfigDroid/configdroid/_latestVersion)

ConfigDroid is a gradle plugin for Android Projects which lets you access configuration properties defined in your `build.gradle` file in the form of constants generated in a java class.


## Download and setup

Add these dependencies to your `build.gradle`:

```groovy
buildscript {
    repositories {
        jcenter()
        maven {
            url  "http://dl.bintray.com/mb-14/ConfigDroid"
        }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.3.1'
        classpath 'com.mb14:configdroid:1.0.1'
    }
}
apply plugin: 'com.mb14.configdroid'

configdroid {
    // Define properties here
}
```

## Usage

### Basic Usage

Global properties can be defined in the configdroid closure. You also need to define the class name, package name and output directory for the generated java class.

```groovy
configdroid {
    className "ConfigDroid"
    packageName "com.mb14.configdroid"
    access "public" // Options: public, package-private
    
    prop "API_ENDPOINT", "https://api.twitter.com"
    prop "ENABLE_LOGGING", true
    prop "DATABASE_VERSION", 3
    prop "API_KEY", TWITTER_API_KEY //Property defined in gradle.properties
}
```

This generates the following source code in `ConfigDroid.java`

```java
package com.example.utils;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;

public class Constants {
  public static final String API_ENDPOINT = "https://api.twitter.com";

  public static final Boolean ENABLE_LOGGING = true;
  
  public static final Integer DATABASE_VERSION = 3;

  public static final String "API_KEY" = "hvtcOOoFM4";
}


```


### Build Variant Specific Config

You can define config properties specific to product flavors and build variants as well. You can use the same key in different variants and ConfigDroid will override the values in the following order: global < product flavor < build type


```groovy 
configdroid {
    className "Constants"
    packageName "com.example.utils"
    access "public"
    prop "DATABASE_VERSION", 3
    
    productFlavors {
        pro {
            prop "ENABLE_FEATURE_X", true
        }
        
        free {
            prop "ENABLE_FEATURE_X", false
        
        }
    }
    
    buildTypes {
        debug {
            prop "API_ENDPOINT", "http://localhost:3000/twitterapi"
            prop "ENABLE_LOGGING", true
        }
        release {
            prop "API_ENDPOINT", "https://api.twitter.com"
            prop "ENABLE_LOGGING", false
        }
    }
}
```

### Possible property types
```groovy
configdroid {
    // Primitives
    prop "API_ENDPOINT", "http://api.twitter.com"
    prop "ENABLE_LOGGING", true
    prop "DATABASE_VERSION", 3
    prop "API_TOKEN", TWITTER_API_TOKEN //Property defined in gradle.properties
    prop "FLOAT_CONSTANT", 9.8f
    prop "DOUBLE_CONSTANT", 9.8d
    prop "LONG_CONSTANT", 122424343535l
    prop "CHAR_CONSTANT", 'a' as char
    prop "BYTE_CONSTANT", 0b11 as byte
    prop 'BUILD_UNIXTIME', System.currentTimeMillis()

    // Arrays
    prop "STRING_ARRAY", ["hello", "world"]
    prop "INT_ARRAY", [1,2,3,4]
    prop "DOUBLE_ARRAY", [9.8d, 24.34d, 44.44d]

    // Custom objects and initializations
    prop 'java.util.Date', 'BUILD_DATE', 'new Date(' + System.currentTimeMillis() + 'L)'
    prop 'boolean', "REQUEST_RUNTIME_PERM", 'android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M'
    prop 'int', 'THEME_COLOR', 'android.graphics.Color.parseColor(\"ff0000\")'
}
```