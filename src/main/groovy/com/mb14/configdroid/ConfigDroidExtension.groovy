package com.mb14.configdroid

import com.android.build.gradle.api.BaseVariant
import com.android.builder.model.ProductFlavor
import com.mb14.configdroid.models.BuildTypeConfig
import com.mb14.configdroid.models.ConfigClosure
import com.mb14.configdroid.models.ConfigField
import com.mb14.configdroid.models.ProductFlavorConfig
import com.mb14.configdroid.utils.FieldUtils
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project


public class ConfigDroidExtension extends ConfigClosure {
    String packageName = "com.mb14.configdroid"
    String className = "ConfigDroid"
    String access = "public"
    NamedDomainObjectContainer<BuildTypeConfig> buildTypeConfigList;
    NamedDomainObjectContainer<ProductFlavorConfig> productFlavorConfigList;
    Project project

    ConfigDroidExtension(Project project, productFlavorConfigList, buildTypeConfigList) {
        this.project = project
        this.buildTypeConfigList = buildTypeConfigList;
        this.productFlavorConfigList = productFlavorConfigList;
    }

    def buildTypes(Closure closure) {
        buildTypeConfigList.configure(closure)
    }

    def productFlavors(Closure closure) {
        productFlavorConfigList.configure(closure);
    }

    void injectTask(BaseVariant variant) {
        HashMap<String, ConfigField> configProperties = getMergedProperties(variant)
        def genConfigTask = project.tasks.create("genConfig${variant.name.capitalize()}", GenConfigTask)
        def output = new File("$project.buildDir/generated/source/configdroid/${variant.dirName}");
        genConfigTask.configure {
            properties = configProperties
            className = this.className
            packageName = this.packageName
            access = this.access
            outputDir = output
        }
        variant.registerJavaGeneratingTask(genConfigTask, output)
    }

    Map<String, ConfigField> getMergedProperties(BaseVariant variant) {
        HashMap<String, ConfigField> properties = new HashMap<>();
        BuildTypeConfig buildTypeConfig = buildTypeConfigList.findByName(variant.getBuildType().name)

        // Merge build type properties
        if (buildTypeConfig) {
            FieldUtils.mergeMap(properties, buildTypeConfig.getProperties())
        }

        // Merge product flavor properties
        List<ProductFlavor> productFlavors = variant.getProductFlavors()
        productFlavors.each { productFlavor ->
            ProductFlavorConfig productFlavorConfig = productFlavorConfigList.findByName(productFlavor.name)
            if (productFlavorConfig) {
                FieldUtils.mergeMap(properties, productFlavorConfig.getProperties())
            }
        }

        // Merge global properties
        FieldUtils.mergeMap(properties, getProperties())

        return properties;

    }
}