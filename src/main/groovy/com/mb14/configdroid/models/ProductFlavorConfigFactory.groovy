package com.mb14.configdroid.models

import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator

class ProductFlavorConfigFactory implements NamedDomainObjectFactory<ProductFlavorConfig> {

    final Instantiator instantiator

    ProductFlavorConfigFactory(Instantiator instantiator) {
        this.instantiator = instantiator
    }

    @Override
    ProductFlavorConfig create(String name) {
        return instantiator.newInstance(ProductFlavorConfig.class, name)
    }
}
