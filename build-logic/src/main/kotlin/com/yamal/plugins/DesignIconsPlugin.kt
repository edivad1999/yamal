package com.yamal.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register
import org.gradle.work.DisableCachingByDefault
import java.io.File
import java.net.URL
import java.util.Locale
import java.util.zip.ZipFile

class DesignIconsPlugin : Plugin<Project> {
    override fun apply(project: Project) =
        with(project) {
            tasks.register("generateIcons") {
                group = "design assets"
                description = "Regenerates Design icons from SVG to XML vector drawables"
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
            val convertToXml =
                tasks.register<ConvertSvgToXmlTask>("convertSvgToXml") {
                    group = "design assets"
                    description = "Converts SVG icons to Android XML vector drawables"

                    dependsOn(extract)

                    inputDir.set(extract.flatMap { it.outputDir })
                    outputDir.set(layout.projectDirectory.dir("src/commonMain/composeResources/drawable"))
                }
            val generateTypeSafeIcons =
                tasks.register<GenerateTypeSafeIconsTask>("generateTypeSafeIcons") {
                    group = "design assets"
                    description = "Generates type-safe icon extensions"

                    dependsOn(convertToXml)

                    inputDir.set(layout.projectDirectory.dir("src/commonMain/composeResources/drawable"))
                    outputFile.set(layout.projectDirectory.file("src/commonMain/kotlin/com/yamal/designSystem/icons/TypeSafeIcons.kt"))
                }

            tasks.named("generateIcons") {
                dependsOn(generateTypeSafeIcons)
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

@DisableCachingByDefault()
abstract class ConvertSvgToXmlTask : DefaultTask() {
    @get:InputDirectory
    abstract val inputDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun convert() {
        val inDir = inputDir.get().asFile
        val outDir = outputDir.get().asFile
        outDir.mkdirs()

        // Clear existing files
        outDir.listFiles()?.forEach { it.delete() }

        var convertedCount = 0

        inDir.walkTopDown()
            .filter { it.isFile && it.extension == "svg" }
            .forEach { svgFile ->
                val category = svgFile.parentFile.name // filled, outlined, twotone
                val iconName = svgFile.nameWithoutExtension

                // Create resource-safe name: ic_iconname_category
                // Resource names must be lowercase and use underscores
                val resourceName = "ic_${iconName.lowercase().replace("-", "_")}_$category"
                val xmlFile = File(outDir, "$resourceName.xml")

                val xmlContent = convertSvgToXml(svgFile)
                if (xmlContent != null) {
                    xmlFile.writeText(xmlContent)
                    convertedCount++
                } else {
                    logger.warn("Failed to convert: ${svgFile.name}")
                }
            }

        logger.lifecycle("Converted $convertedCount SVG icons to XML vector drawables")
    }

    private fun convertSvgToXml(svgFile: File): String? {
        val content = svgFile.readText()

        // Extract viewBox dimensions
        val viewBoxRegex = """viewBox="([^"]+)"""".toRegex()
        val viewBoxMatch = viewBoxRegex.find(content)
        val viewBox = viewBoxMatch?.groupValues?.get(1)?.split(" ") ?: return null

        if (viewBox.size < 4) return null

        val viewportWidth = viewBox[2]
        val viewportHeight = viewBox[3]

        // Extract all path data
        val pathRegex = """<path[^>]*d="([^"]+)"[^>]*/>""".toRegex()
        val paths = pathRegex.findAll(content).map { it.groupValues[1] }.toList()

        if (paths.isEmpty()) return null

        return buildString {
            appendLine("""<vector xmlns:android="http://schemas.android.com/apk/res/android"""")
            appendLine("""    android:width="24dp"""")
            appendLine("""    android:height="24dp"""")
            appendLine("""    android:viewportWidth="$viewportWidth"""")
            appendLine("""    android:viewportHeight="$viewportHeight">""")

            paths.forEach { pathData ->
                appendLine("""    <path""")
                appendLine("""        android:fillColor="#000000"""")
                appendLine("""        android:pathData="$pathData"/>""")
            }

            appendLine("</vector>")
        }
    }
}

@DisableCachingByDefault()
abstract class GenerateTypeSafeIconsTask : DefaultTask() {
    @get:InputDirectory
    abstract val inputDir: DirectoryProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun generate() {
        val dir = inputDir.get().asFile
        val outFile = outputFile.get().asFile
        outFile.parentFile.mkdirs()

        // Map to categories: Filled, Outlined, Twotone
        val iconsByCategory = mutableMapOf<String, MutableList<Pair<String, String>>>()

        dir.listFiles { f -> f.extension == "xml" }?.forEach { file ->
            val name = file.nameWithoutExtension // ic_setting_filled
            if (!name.startsWith("ic_")) return@forEach

            val parts = name.removePrefix("ic_").split("_")
            if (parts.size < 2) return@forEach

            val category = parts.last().replaceFirstChar { it.titlecase(Locale.ROOT) } // Filled / Outlined / Twotone
            val iconName = parts.dropLast(1).joinToString("_") // setting, account_book

            // Convert to PascalCase property name
            val propertyName = iconName.split("_").joinToString("") { it.replaceFirstChar(Char::uppercase) }

            iconsByCategory.getOrPut(category) { mutableListOf() }.add(propertyName to name)
        }

        // Generate Kotlin file
        outFile.writeText(
            buildString {
                appendLine("@file:Suppress(\"ktlint\", \"unused\")")
                appendLine("package com.yamal.designSystem.icons")
                appendLine()
                appendLine("import org.jetbrains.compose.resources.DrawableResource")
                appendLine("import yamal.platform.designsystem.generated.resources.Res")
                appendLine("import yamal.platform.designsystem.generated.resources.*")
                appendLine()
                appendLine("class IconPainter(val drawable: DrawableResource)")
                appendLine()
                appendLine("object Icons {")

                listOf("Filled", "Outlined", "Twotone").forEach { category ->
                    appendLine("    object $category {")

                    iconsByCategory[category]?.sortedBy { it.first }?.forEach { (propName, resourceName) ->
                        appendLine(
                            "        val $propName: IconPainter get() = IconPainter(Res.drawable.$resourceName)",
                        )
                    }

                    appendLine("    }")
                }

                appendLine("}")
            },
        )
    }
}
