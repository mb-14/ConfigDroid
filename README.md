ConfigDroid
===

ConfigDroid is a gradle plugin for Android Projects which lets you access configuration properties defined in your `build.gradle` file in the form of constants generated in a java class.


## Download and setup

Add these dependencies to your `build.gradle`:

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.3.1'
        classpath 'com.mb14:configdroid:1.0.0'
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
    className "Constants"
    packageName "com.example.utils"
    output "src/main/java"
    
    prop "API_ENDPOINT", "https://api.twitter.com"
    prop "ENABLE_LOGGING", true
    prop "DATABASE_VERSION", 3
}
```

This generates the following source code in `src/main/java/com/example/utils/Constants.java`:

```java
package com.example.utils;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;

public class Constants {
  public static final String API_ENDPOINT = "https://api.twitter.com";

  public static final Boolean ENABLE_LOGGING = true;
  
  public static final Integer DATABASE_VERSION = 3;
}


```


### Build Variant Specific Config

You can define config properties specific to product flavors and build variants as well. You can use the same key in different variants and ConfigDroid will override the values in the following order: global < product flavor < build type


```groovy 
configdroid {
    className "Constants"
    packageName "com.example.utils"
    output "src/main/java"
  
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