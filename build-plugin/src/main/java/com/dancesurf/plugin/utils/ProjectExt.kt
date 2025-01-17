package com.dancesurf.plugin.utils

import com.dancesurf.plugin.ProjectConfig
import org.gradle.api.Project

/**
 * Find a namespace for the gradle module starting from the last directory in the root path.
 *
 * @return namespace
 */
internal val Project.nameSpace: String
    get() = "${ProjectConfig.NAMESPACE}.$name".also {
        println(">>> Gradle module \"$name\" has the following namespace: $it")
    }

internal val Project.moduleName: String
    get() = path.drop(1).replace(":", "-")