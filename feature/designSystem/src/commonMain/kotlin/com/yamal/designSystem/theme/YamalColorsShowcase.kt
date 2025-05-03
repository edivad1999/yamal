package com.yamal.designSystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview()
@Composable
private fun YamalColorsPreview() {
    YamalTheme(true) {
        Surface {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text("Yamal Design System")
                Spacer(Modifier.height(8.dp))
                YamalDesignSystemSection(YamalTheme.colors)

                Spacer(Modifier.height(16.dp))

                Text("Material Colors")
                Spacer(Modifier.height(8.dp))
                MaterialColorsSection(MaterialTheme.colors)
            }
        }
    }
}

@Preview()
@Composable
private fun YamalColorsPreviewLightPreview() {
    YamalTheme(false) {
        Surface {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text("Yamal Design System")
                Spacer(Modifier.height(8.dp))
                YamalDesignSystemSection(YamalTheme.colors)

                Spacer(Modifier.height(16.dp))

                Text("Material Colors")
                Spacer(Modifier.height(8.dp))
                MaterialColorsSection(MaterialTheme.colors)
            }
        }
    }
}

@Composable
private fun YamalDesignSystemSection(yamal: YamalColors) {
    Column {
        Text("Palette Colors")
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
            labels = (1..10).map { "C$it" },
        )

        Spacer(Modifier.height(8.dp))

        Text("Functional Colors")
        ColorRow(
            listOf(
                yamal.functionalColors.success,
                yamal.functionalColors.warning,
                yamal.functionalColors.error,
            ),
            labels = listOf("Success", "Warning", "Error"),
        )

        Spacer(Modifier.height(8.dp))

        Text("Neutral Colors")
        ColorRow(
            listOf(
                yamal.neutralColors.title,
                yamal.neutralColors.primaryText,
                yamal.neutralColors.secondaryText,
                yamal.neutralColors.disableText,
                yamal.neutralColors.border,
                yamal.neutralColors.divider,
                yamal.neutralColors.background,
                yamal.neutralColors.tableHeader,
            ),
            labels =
                listOf(
                    "Title",
                    "Primary",
                    "Secondary",
                    "Disabled",
                    "Border",
                    "Divider",
                    "Bg",
                    "Table",
                ),
        )
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
            colors.onPrimary,
            colors.onSecondary,
            colors.onBackground,
            colors.onSurface,
            colors.onError,
        ),
        labels =
            listOf(
                "Primary",
                "PrimaryV",
                "Secondary",
                "SecondaryV",
                "Bg",
                "Surface",
                "Error",
                "OnPrimary",
                "OnSecondary",
                "OnBg",
                "OnSurface",
                "OnError",
            ),
    )
}

@Composable
private fun ColorRow(
    colors: List<Color>,
    labels: List<String>,
) {
    FlowRow(
        modifier = Modifier.padding(8.dp),
    ) {
        colors.forEachIndexed { index, color ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(56.dp),
            ) {
                Box(
                    Modifier
                        .size(48.dp)
                        .background(color, shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                )
                Text(
                    text = labels.getOrNull(index) ?: "",
                    fontSize = 10.sp,
                    maxLines = 1,
                )
            }
        }
    }
}
