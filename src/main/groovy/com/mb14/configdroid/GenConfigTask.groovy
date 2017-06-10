package com.mb14.configdroid

import com.squareup.javapoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import javax.lang.model.element.Modifier


class GenConfigTask extends DefaultTask {
    @Input
    HashMap<String, Object> properties;

    @OutputDirectory
    File outputDir

    @Input
    String className

    @Input
    String packageName

    @TaskAction
    def generateConfig() {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)

        properties.each {k,v ->
            classBuilder.addField(FieldSpec.builder(v.class, k)
                    .addModifiers(Modifier.FINAL, Modifier.STATIC)
                    .initializer(getFormat(v), v)
                    .build())
        }
        JavaFile.builder(packageName, classBuilder.build())
                .build()
                .writeTo(outputDir);
    }

    String getFormat(v) {
        if (v instanceof String) {
            return "\$S"
        }
        return "\$L"
    }
}