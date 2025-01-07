package com.dancesurf.plugin.utils

import org.gradle.api.Project
import java.io.File

/**
 * Find a namespace for the gradle module starting from the last directory in the root path.
 *
 * Approach:
 * namespace will be collected until package does not contain any files and has only one subdirectory.
 * The process will be continued until any file found or and of the path
 *
 * @param rootPath path to the root package
 * @return namespace
 */
internal fun Project.getNameSpace(rootPath: String): String = StringBuilder()
    .apply {
        val root = File(rootPath)
        var files = root.listFiles() ?: emptyArray()
        while (files.size == 1 && files.first().isDirectory) {
            append(files.first()?.name)
            append(".")
            files = files.first().listFiles() ?: emptyArray()
        }
        deleteCharAt(lastIndex)
    }
    .toString()
    .also {
        println(">>> Gradle module \"$name\" has the following namespace: $it")
    }