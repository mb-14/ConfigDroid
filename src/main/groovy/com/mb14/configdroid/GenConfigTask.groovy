package com.mb14.configdroid

import com.mb14.configdroid.models.ConfigField
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import javax.lang.model.element.Modifier


class GenConfigTask extends DefaultTask {
    @Input
    HashMap<String, ConfigField> properties

    @OutputDirectory
    File outputDir

    @Input
    String className

    @Input
    String packageName

    @Input
    String access

    @TaskAction
    def generateConfig() {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(getModifiers(Modifier.FINAL))

        properties.each {k,v ->
            classBuilder.addField(FieldSpec.builder(v.getType(), k)
                    .addModifiers(getModifiers(Modifier.FINAL, Modifier.STATIC))
                    .initializer("\$L", v.value)
                    .build())
        }

        JavaFile.builder(packageName, classBuilder.build())
                .build()
                .writeTo(outputDir)
    }

    Modifier[] getModifiers(Modifier... modifiers) {
        if(access.equals("package-private"))
            return modifiers
        else
            return modifiers.plus(Modifier.PUBLIC)
    }
}