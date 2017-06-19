package com.mb14.configdroid

import com.mb14.configdroid.models.BaseField
import com.mb14.configdroid.models.PropField
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
    HashMap<String, BaseField> fields

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

        fields.each {k,v ->
             classBuilder.addField(FieldSpec.builder(v.getValueType(), k)
                        .addModifiers(getModifiers(Modifier.FINAL, Modifier.STATIC))
                        .initializer(v.getInitializerType(), v.getValue())
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