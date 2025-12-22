package com.yamal.designSystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun YamalColorsDarkPreview() {
    YamalTheme(isDark = true) {
        Surface {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text("Yamal Design System - Dark", style = YamalTheme.typography.h4)
                Spacer(Modifier.height(16.dp))
                YamalDesignSystemSection(YamalTheme.colors)
            }
        }
    }
}

@Preview
@Composable
private fun YamalColorsLightPreview() {
    YamalTheme(isDark = false) {
        Surface {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text("Yamal Design System - Light", style = YamalTheme.typography.h4)
                Spacer(Modifier.height(16.dp))
                YamalDesignSystemSection(YamalTheme.colors)
            }
        }
    }
}

@Composable
private fun YamalDesignSystemSection(yamal: YamalColors) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Brand Palette
        SectionHeader("Brand Palette (color1-10)")
        ColorRow(
            listOf(
                yamal.paletteColors.color1,
                yamal.paletteColors.color2,
                yamal.paletteColors.color3,
                yamal.paletteColors.color4,
                yamal.paletteColors.color5,
                yamal.paletteColors.color6,
                yamal.paletteColors.color7,
                yamal.paletteColors.color8,
                yamal.paletteColors.color9,
                yamal.paletteColors.color10,
            ),
            labels = (1..10).map { "$it" },
        )

        // Functional Colors
        SectionHeader("Functional Colors")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FunctionalColorGroup(
                name = "Success",
                main = yamal.functionalColors.success,
                bg = yamal.functionalColors.successBg,
                border = yamal.functionalColors.successBorder,
            )
            FunctionalColorGroup(
                name = "Warning",
                main = yamal.functionalColors.warning,
                bg = yamal.functionalColors.warningBg,
                border = yamal.functionalColors.warningBorder,
            )
            FunctionalColorGroup(
                name = "Error",
                main = yamal.functionalColors.error,
                bg = yamal.functionalColors.errorBg,
                border = yamal.functionalColors.errorBorder,
            )
            FunctionalColorGroup(
                name = "Info",
                main = yamal.functionalColors.info,
                bg = yamal.functionalColors.infoBg,
                border = yamal.functionalColors.infoBorder,
            )
        }

        // Link Colors
        SectionHeader("Link Colors")
        ColorRow(
            listOf(
                yamal.functionalColors.link,
                yamal.functionalColors.linkHover,
                yamal.functionalColors.linkActive,
            ),
            labels = listOf("Link", "Hover", "Active"),
        )

        // Neutral Colors
        SectionHeader("Neutral Colors")
        ColorRow(
            listOf(
                yamal.neutralColors.title,
                yamal.neutralColors.primaryText,
                yamal.neutralColors.secondaryText,
                yamal.neutralColors.disableText,
            ),
            labels = listOf("Title", "Primary", "Secondary", "Disabled"),
        )
        ColorRow(
            listOf(
                yamal.neutralColors.border,
                yamal.neutralColors.divider,
                yamal.neutralColors.background,
                yamal.neutralColors.containerBg,
            ),
            labels = listOf("Border", "Divider", "Bg", "Container"),
        )
        ColorRow(
            listOf(
                yamal.neutralColors.fill,
                yamal.neutralColors.fillSecondary,
                yamal.neutralColors.fillTertiary,
                yamal.neutralColors.fillQuaternary,
            ),
            labels = listOf("Fill", "Fill 2", "Fill 3", "Fill 4"),
        )

        // Preset Colors
        SectionHeader("Preset Colors")
        PresetColorsGrid(yamal.presetColors)

        // Material Colors
        SectionHeader("Material Theme Colors")
        MaterialColorsSection(MaterialTheme.colors)
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = YamalTheme.typography.bodyMedium,
        color = YamalTheme.colors.neutralColors.primaryText,
    )
}

@Composable
private fun FunctionalColorGroup(
    name: String,
    main: Color,
    bg: Color,
    border: Color,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp),
    ) {
        Text(name, fontSize = 10.sp)
        Spacer(Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            ColorBox(bg, "Bg", size = 20)
            ColorBox(border, "Bdr", size = 20)
            ColorBox(main, "Main", size = 20)
        }
    }
}

@Composable
private fun PresetColorsGrid(presetColors: YamalColors.PresetColors) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PresetColorItem("Red", presetColors.red)
            PresetColorItem("Volcano", presetColors.volcano)
            PresetColorItem("Orange", presetColors.orange)
            PresetColorItem("Gold", presetColors.gold)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PresetColorItem("Yellow", presetColors.yellow)
            PresetColorItem("Lime", presetColors.lime)
            PresetColorItem("Green", presetColors.green)
            PresetColorItem("Cyan", presetColors.cyan)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PresetColorItem("Blue", presetColors.blue)
            PresetColorItem("Geekblue", presetColors.geekblue)
            PresetColorItem("Purple", presetColors.purple)
            PresetColorItem("Magenta", presetColors.magenta)
        }
    }
}

@Composable
private fun PresetColorItem(
    name: String,
    palette: YamalColors.PresetColors.ColorPalette,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp),
    ) {
        Text(name, fontSize = 9.sp, maxLines = 1)
        Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
            Box(
                Modifier
                    .size(20.dp)
                    .background(palette.bg, RoundedCornerShape(2.dp)),
            )
            Box(
                Modifier
                    .size(20.dp)
                    .background(palette.border, RoundedCornerShape(2.dp)),
            )
            Box(
                Modifier
                    .size(20.dp)
                    .background(palette.color, RoundedCornerShape(2.dp)),
            )
        }
    }
}

@Composable
private fun MaterialColorsSection(colors: Colors) {
    ColorRow(
        listOf(
            colors.primary,
            colors.primaryVariant,
            colors.secondary,
            colors.secondaryVariant,
            colors.background,
            colors.surface,
            colors.error,
        ),
        labels = listOf("Primary", "PrimaryV", "Secondary", "SecondaryV", "Bg", "Surface", "Error"),
    )
    ColorRow(
        listOf(
            colors.onPrimary,
            colors.onSecondary,
            colors.onBackground,
            colors.onSurface,
            colors.onError,
        ),
        labels = listOf("OnPrimary", "OnSecondary", "OnBg", "OnSurface", "OnError"),
    )
}

@Composable
private fun ColorRow(
    colors: List<Color>,
    labels: List<String>,
) {
    FlowRow(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        colors.forEachIndexed { index, color ->
            ColorBox(color, labels.getOrNull(index) ?: "")
        }
    }
}

@Composable
private fun ColorBox(
    color: Color,
    label: String,
    size: Int = 40,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width((size + 16).dp),
    ) {
        Box(
            Modifier
                .size(size.dp)
                .background(color, shape = RoundedCornerShape(Dimension.BorderRadius.sm))
                .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(Dimension.BorderRadius.sm)),
        )
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 9.sp,
                maxLines = 1,
            )
        }
    }
}
