package com.mb14.configdroid.models

import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator

class BuildTypeConfigFactory implements NamedDomainObjectFactory<BuildTypeConfig> {

    final Instantiator instantiator

    BuildTypeConfigFactory(Instantiator instantiator) {
        this.instantiator = instantiator
    }

    @Override
    BuildTypeConfig create(String name) {
        return instantiator.newInstance(BuildTypeConfig.class, name)
    }
}
