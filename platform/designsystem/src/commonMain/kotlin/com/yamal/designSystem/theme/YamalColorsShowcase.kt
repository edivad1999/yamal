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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
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
                Text("Yamal Design System - Dark", style = YamalTheme.typography.title)
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
                Text("Yamal Design System - Light", style = YamalTheme.typography.title)
                Spacer(Modifier.height(16.dp))
                YamalDesignSystemSection(YamalTheme.colors)
            }
        }
    }
}

@Composable
private fun YamalDesignSystemSection(yamal: YamalColors) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Semantic Colors
        SectionHeader("Semantic Colors")
        ColorRow(
            listOf(
                yamal.primary,
                yamal.success,
                yamal.warning,
                yamal.danger,
            ),
            labels = listOf("Primary", "Success", "Warning", "Danger"),
        )

        // Additional Colors
        SectionHeader("Additional Colors")
        ColorRow(
            listOf(
                yamal.yellow,
                yamal.orange,
                yamal.wathet,
            ),
            labels = listOf("Yellow", "Orange", "Wathet"),
        )

        // Text Colors
        SectionHeader("Text Colors")
        ColorRow(
            listOf(
                yamal.text,
                yamal.textSecondary,
                yamal.weak,
                yamal.light,
            ),
            labels = listOf("Text", "Secondary", "Weak", "Light"),
        )

        // Surface Colors
        SectionHeader("Surface Colors")
        ColorRow(
            listOf(
                yamal.background,
                yamal.box,
                yamal.border,
                yamal.white,
            ),
            labels = listOf("Bg", "Box", "Border", "White"),
        )

        // Solid Text Colors
        SectionHeader("Solid Text Colors")
        ColorRow(
            listOf(
                yamal.textLightSolid,
                yamal.textDarkSolid,
            ),
            labels = listOf("Light Solid", "Dark Solid"),
        )

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
        color = YamalTheme.colors.text,
    )
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
                .background(color, shape = RoundedCornerShape(4.dp))
                .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(4.dp)),
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
