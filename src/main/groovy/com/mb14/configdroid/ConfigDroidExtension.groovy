package com.mb14.configdroid

import com.android.build.gradle.api.BaseVariant
import com.android.builder.model.ProductFlavor
import com.mb14.configdroid.models.BuildTypeConfig
import com.mb14.configdroid.models.ConfigClosure
import com.mb14.configdroid.models.ProductFlavorConfig
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project


public class ConfigDroidExtension extends ConfigClosure {
    String packageName = "com.mb14.configdroid"
    String className = "ConfigDroid"
    String output = "src/main/java"
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
        HashMap<String, Object> configProperties = getMergedProperties(variant)
        def genConfigTask = project.tasks.create("genConfig${variant.name.capitalize()}", GenConfigTask)
        genConfigTask.configure {
            properties = configProperties
            className = this.className
            packageName = this.packageName
            access = this.access
            outputDir = new File(this.output)
        }
        variant.getPreBuild().dependsOn(genEnvTask)
    }

    /**
     * Merge patch into target without overriding existing keys
     * Preference order: build type > product flavor > global
     * @param target
     * @param patch
     */
    void mergeMap(HashMap target, HashMap patch) {
        Map tmp = new HashMap(patch);
        tmp.keySet().removeAll(target.keySet());
        target.putAll(tmp);
    }

    Map<String, Object> getMergedProperties(BaseVariant variant) {
        HashMap<String, Object> properties = new HashMap<>();
        BuildTypeConfig buildTypeConfig = buildTypeConfigList.findByName(variant.getBuildType().name)

        // Merge build type properties
        if (buildTypeConfig) {
            mergeMap(properties, buildTypeConfig.getProperties())
        }

        // Merge product flavor properties
        List<ProductFlavor> productFlavors = variant.getProductFlavors()
        productFlavors.each { productFlavor ->
            ProductFlavorConfig productFlavorConfig = productFlavorConfigList.findByName(productFlavor.name)
            if (productFlavorConfig) {
                mergeMap(properties, productFlavorConfig.getProperties())
            }
        }

        // Merge global properties
        mergeMap(properties, getProperties())

        return properties;

    }
}