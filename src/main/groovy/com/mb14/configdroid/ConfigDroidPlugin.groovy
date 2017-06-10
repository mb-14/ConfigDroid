package com.mb14.configdroid

import com.mb14.configdroid.models.BuildTypeConfig
import com.mb14.configdroid.models.BuildTypeConfigFactory
import com.mb14.configdroid.models.ProductFlavorConfig
import com.mb14.configdroid.models.ProductFlavorConfigFactory
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator
import com.mb14.configdroid.utils.AndroidUtils

import javax.inject.Inject

class ConfigDroidPlugin implements Plugin<Project> {

    protected Instantiator instantiator

    @Inject
    public ConfigDroidPlugin(Instantiator instantiator) {
       this.instantiator = instantiator;
    }

    void apply(Project project) {
        def productFlavorConfigList = project.container(ProductFlavorConfig, new ProductFlavorConfigFactory(instantiator))
        def buildTypeConfigList = project.container(BuildTypeConfig, new BuildTypeConfigFactory(instantiator))

        project.extensions.create("config", ConfigDroidExtension, project, productFlavorConfigList, buildTypeConfigList)

        if (AndroidUtils.isApplicationProject(project)) {
            project.android.applicationVariants.all { variant ->
                project.config.injectTask(variant);
            }
        }
        else if (AndroidUtils.isLibraryProject(project)) {
            project.android.libraryVariants.all { variant ->
                project.config.injectTask(variant);
            }
        }
    }
}