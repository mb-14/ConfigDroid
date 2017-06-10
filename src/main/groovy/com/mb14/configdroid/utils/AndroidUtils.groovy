package com.mb14.configdroid.utils

import org.gradle.api.Project

class AndroidUtils {

    public static boolean isApplicationProject(Project project) {
        project.plugins.hasPlugin('com.android.application')
    }

    public static boolean isLibraryProject(Project project) {
        project.plugins.hasPlugin('com.android.library')
    }
}