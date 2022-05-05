import org.jetbrains.compose.compose
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.*
import kotlin.math.roundToInt

plugins {
    kotlin("multiplatform") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.0"
}

group = "com.github.KamilKurde"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    js(IR) {
        browser {
        }
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)
            }
        }
    }
}


afterEvaluate {
    rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
        versions.webpackDevServer.version = "4.0.0"
    }
}

abstract class Minify : DefaultTask() {
    @InputDirectory
    var inputDirectory: File? = null

    @OutputDirectory
    var outputDirectory: File? = null

    private fun String.replaceMap(vararg replacements: Pair<String, String>): String {
        var text = this
        replacements.forEach {
            text = text.replace(it.first, it.second)
        }
        return text
    }

    private fun minify(input: File, output: File): Long {
        val content = URLEncoder.encode("input", "UTF-8") + "=" + URLEncoder.encode(input.readText(), "UTF-8")
        val outputFile = File(output, input.name)
        val initialLength = input.length()
        println("Sending query for ${input.name}, initial file size: $initialLength")
        val url = when (input.extension) {
            "js" -> URL("https://www.toptal.com/developers/javascript-minifier/raw")
            "css" -> URL("https://www.toptal.com/developers/cssminifier/raw")
            else -> throw IllegalArgumentException("Unsupported file extension: ${input.extension}")
        }
        val request = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            setRequestProperty("charset", "utf-8")
            setRequestProperty("Content-Length", content.length.toString())
            OutputStreamWriter(outputStream).apply {
                write(content.replace("\'", "'"))
                flush()
            }
        }

        if (request.responseCode == 200) {
            println("Response received, converting")
            outputFile.writeText(
                InputStreamReader(request.inputStream)
                    .readText()
                    .replaceMap(
                        "Ä…" to "ą",
                        "Ä‡" to "ć",
                        "Ä™" to "ę",
                        "Ĺ‚" to "ł",
                        "Ĺ�" to "Ł",
                        "Ĺ„" to "ń",
                        "Ăł" to "ó",
                        "Ĺ›" to "ś",
                        "Ĺš" to "Ś",
                        "ĹĽ" to "ż",
                        "Ĺ‚" to "Ź",
                        "Ĺş" to "ź"
                    )
            )
            val outputLength = outputFile.length()
            val difference = (initialLength - outputLength).toFloat() / initialLength
            val rounded = (difference * 100 * 100).roundToInt() / 100
            println("Response converted, output size: $outputLength, size reduced by $rounded%")
            return outputLength
        } else {
            println("Response code: ${request.responseCode} ${request.responseMessage}")
            input.copyTo(outputFile)
            return initialLength
        }
    }

    @TaskAction
    fun minifyOutput() {
        require(inputDirectory != null) { "Input directory is not specified" }
        require(inputDirectory?.isDirectory ?: false) { "Input directory is not a directory" }
        require(outputDirectory != null) { "Output directory is not specified" }
        require(outputDirectory?.isDirectory ?: false) { "Output directory is not a directory" }
        val files = inputDirectory!!.listFiles()!!.filter { it.isFile && it.extension in listOf("js", "css") }
        val inputSize = files.sumOf { it.length() }
        val outputSize = files.sumOf { minify(it, outputDirectory!!) }
        val difference = (inputSize - outputSize).toFloat() / inputSize
        println("All files converted, input size: $inputSize, output size: $outputSize, size reduced by ${difference}%")
    }
}

tasks.register<Minify>("buildMinified") {
    dependsOn("jsBrowserProductionWebpack")
    inputDirectory = File(projectDir.absolutePath + File.separator + "build" + File.separator + "distributions")
    outputDirectory = File(projectDir.absolutePath + File.separator + "distributions")
}