#!/usr/bin/env kotlin

import java.net.URI
import java.nio.file.Path
import java.util.zip.GZIPInputStream

val projectDir: Path = Path.of(".").toAbsolutePath().normalize()
val pathSaveIcons: Path = projectDir.resolve(
        "src", "main", "java",
"com", "example", "wrc", "ui", "icons"
)

val icons = listOf(
    Icon("Home","home"),
    Icon("Add","add", size = 30),
    Icon("Autorenew","autorenew", size = 30),
    Icon("MoreVert","more_vert", size = 30),
    Icon("Settings","settings"),
    Icon("Edit","edit"),
    Icon("Delete","delete"),
    Icon("ArrowBack","arrow_back", size = 30),
    Icon("Save","save", size = 30),
    Icon("SaveAs","save_as", size = 30),
    Icon("Gamepad","gamepad", size = 40),
    Icon("Keyboard","keyboard", size = 40),
    Icon("TouchpadMouse","touchpad_mouse", size = 40),
    Icon("Touchpad","call_to_action", size = 40),
    Icon("PlayPause","play_pause", size = 100),
    Icon("SkipNext","skip_next"),
    Icon("SkipPrevious","skip_previous"),
    Icon("FastForward","fast_forward", size = 40),
    Icon("FastRewind","fast_rewind", size = 40),
    Icon("VolumeUp","volume_up", size = 40),
    Icon("VolumeDown","volume_down", size = 40),
    Icon("VolumeOff","volume_off", size = 40),
    Icon("NoSound","no_sound", size = 40),
    Icon("Pause","pause", size = 40),
    Icon("Play","play_arrow", size = 40),

    Icon("ArrowBackIOS","arrow_back_ios", size = 30),
    Icon("MimoDisconnect","mimo_disconnect", size = 30),

    Icon("Copy","content_copy", size = 40),
    Icon("CopyAll","copy_all", size = 40),
    Icon("Paste","content_paste", size = 40),
    Icon("Cut","content_cut", size = 40),
    Icon("Undo","undo", size = 40),
    Icon("Redo","redo", size = 40),

    Icon("Menu","menu", size = 30),
    Icon("TextSnippet","text_snippet", size = 30),





)

for(icon in icons){
    val connection = URI(icon.url)
        .toURL()
        .openConnection()

    connection.setRequestProperty("Accept-Encoding", "identity")


    val source = GZIPInputStream(connection.getInputStream())
        .bufferedReader()
        .readText()
        .replace(
            Regex("""^package\s+.*$""", RegexOption.MULTILINE),
            "package com.example.wrc.ui.icons.generated"
        )
    pathSaveIcons.resolve("generated","${icon.nameFile}.kt").toFile().writeText(source);
}

val genObject = """
package com.example.wrc.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector

${icons.joinToString("\n") {"import com.example.wrc.ui.icons.generated.${it.nameProperty}"}}

object AppIcons {
    ${icons.joinToString("") {"""
    val ${it.nameFile}: ImageVector
        get() = ${it.nameProperty}"""}}
}
""".trimIndent()

pathSaveIcons.resolve("AppIcons.kt").toFile().writeText(genObject);
data class Icon(
    val nameFile: String,
    val nameProperty: String,
    val style: IconStyle = IconStyle.Outlined,
    val size: Int = 24,
    val weight: Int = 400,
    val fill: Boolean = false,
    val grade: Int = 0,
    val round: Int = 50
) {
    val url: String
        get() = iconUrl(
            name = nameProperty,
            style = style,
            size = size,
            weight = weight,
            fill = fill,
            grade = grade,
            round = round
        )
}
fun iconUrl(
    name: String,
    style: IconStyle = IconStyle.Outlined,
    size: Int = 24,
    weight: Int = 400,
    fill: Boolean = false,
    grade: Int = 0,
    round: Int = 50
): String =
    "https://fonts.gstatic.com/render/v1/" +
            "Material+Symbols+${style.name}/${size}dp/$name.kt?" +
            "var=opsz,wght,FILL,GRAD,ROND@$size,$weight,${if (fill) 1 else 0},$grade,$round"
enum class IconStyle {
    Outlined,
    Rounded,
    Sharp
}