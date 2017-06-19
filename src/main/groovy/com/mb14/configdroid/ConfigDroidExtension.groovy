package com.mb14.configdroid

import com.android.build.gradle.api.BaseVariant
import com.android.builder.model.ProductFlavor
import com.mb14.configdroid.models.BaseField
import com.mb14.configdroid.models.BuildTypeConfig
import com.mb14.configdroid.models.ConfigClosure
import com.mb14.configdroid.models.PropField
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
        HashMap<String, PropField> configFields = getMergedFields(variant)
        def genConfigTask = project.tasks.create("genConfig${variant.name.capitalize()}", GenConfigTask)
        def output = new File("$project.buildDir/generated/source/configdroid/${variant.dirName}");
        genConfigTask.configure {
            fields = configFields
            className = this.className
            packageName = this.packageName
            access = this.access
            outputDir = output
        }
        variant.registerJavaGeneratingTask(genConfigTask, output)
    }

    Map<String, PropField> getMergedFields(BaseVariant variant) {
        HashMap<String, BaseField> fields = new HashMap<>();
        BuildTypeConfig buildTypeConfig = buildTypeConfigList.findByName(variant.getBuildType().name)

        // Merge build type fields
        if (buildTypeConfig) {
            FieldUtils.mergeMap(fields, buildTypeConfig.getFields())
        }

        // Merge product flavor fields
        List<ProductFlavor> productFlavors = variant.getProductFlavors()
        productFlavors.each { productFlavor ->
            ProductFlavorConfig productFlavorConfig = productFlavorConfigList.findByName(productFlavor.name)
            if (productFlavorConfig) {
                FieldUtils.mergeMap(fields, productFlavorConfig.getFields())
            }
        }

        // Merge global fields
        FieldUtils.mergeMap(fields, getFields())

        return fields;
    }
}