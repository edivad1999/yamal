package com.yamal.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register
import org.gradle.work.DisableCachingByDefault
import org.jetbrains.kotlin.konan.properties.suffix
import java.net.URL
import java.util.zip.ZipFile

class DesignIconsPlugin : Plugin<Project> {
    override fun apply(project: Project) =
        with(project) {
            tasks.register("generateIcons") {
                group = "design assets"
                description = "Regenerates Design SVG icons"
            }

            val download =
                tasks.register<DownloadAntDesignIconsTask>("downloadIcons") {
                    outputZip.set(
                        layout.buildDirectory.file("ant-design-icons/ant-icons-master.zip"),
                    )
                }
            val extract =
                tasks.register<ExtractDesignIconsTask>("extractIcons") {
                    dependsOn(download)

                    inputZip.set(download.flatMap { it.outputZip })

                    outputDir.set(
                        layout.buildDirectory.dir(
                            "ant-design-icons/extracted/icons",
                        ),
                    )
                }
            val copyIcons =
                tasks.register<Copy>("copyIconsToResources") {
                    group = "design assets"
                    description = "Copies extracted SVG icons into commonMain resources"

                    // Input: the output of the extract task
                    from(layout.buildDirectory.dir("ant-design-icons/extracted/icons"))

                    eachFile {
                        path =
                            this.path
                                .removeSuffix(".svg")
                                .split("/")
                                .reversed()
                                .joinToString("_")
                                .suffix("svg")
                    }
                    // Output: your commonMain resources folder
                    into(layout.projectDirectory.dir("src/commonMain/composeResources/files"))
                    includeEmptyDirs = false // prevents Gradle from creating empty dirs

                    include("**/*.svg")
                }
            tasks.named("generateIcons") {
                dependsOn(copyIcons)
            }
            Unit
        }
}

@DisableCachingByDefault()
abstract class DownloadAntDesignIconsTask : DefaultTask() {
    @get:OutputFile
    abstract val outputZip: RegularFileProperty

    @TaskAction
    fun download() {
        val url =
            URL(
                "https://github.com/ant-design/ant-design-icons/archive/refs/heads/master.zip",
            )

        val target = outputZip.get().asFile
        target.parentFile.mkdirs()

        logger.lifecycle("Downloading Ant Design Icons (master)")

        url.openStream().use { input ->
            target.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}

@DisableCachingByDefault()
abstract class ExtractDesignIconsTask : DefaultTask() {
    @get:InputFile
    abstract val inputZip: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun extract() {
        val zipFile = inputZip.get().asFile
        val outDir = outputDir.get().asFile

        val middle = "icons-svg/svg/"

        ZipFile(zipFile).use { zip ->
            zip
                .entries()
                .asSequence()
                .filter { !it.isDirectory }
                .filter { it.name.contains(middle) }
                .filter { it.name.endsWith(".svg") }
                .forEach { entry ->
                    val relativePath = entry.name.substringAfter(middle)
                    val target = outDir.resolve(relativePath)

                    target.mkdirs()
                    target.delete()
                    target.createNewFile()

                    zip.getInputStream(entry).use { input ->
                        target.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
        }
    }
}
