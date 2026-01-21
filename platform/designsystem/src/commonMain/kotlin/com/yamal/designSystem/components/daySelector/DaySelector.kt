package com.yamal.designSystem.components.daySelector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.surface.Surface
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Data class representing a day in the selector.
 *
 * @param dayOfWeek Three-letter day abbreviation (e.g., "MON", "TUE")
 * @param dayOfMonth Numeric day of the month
 */
data class Day(
    val dayOfWeek: String,
    val dayOfMonth: Int,
)

/**
 * Day selector component for choosing a specific day of the week.
 *
 * Displays a horizontal row of days with day abbreviation and date number.
 * Typically used for release schedules or calendar-based views.
 *
 * Example usage:
 * ```
 * val days = listOf(
 *     Day("MON", 12),
 *     Day("TUE", 13),
 *     Day("WED", 14)
 * )
 * DaySelector(
 *     days = days,
 *     selectedDayIndex = 1,
 *     onDaySelected = { index -> /* handle selection */ }
 * )
 * ```
 *
 * @param days List of days to display
 * @param selectedDayIndex Index of the currently selected day
 * @param modifier Modifier for the selector
 * @param onDaySelected Callback when a day is selected
 */
@Composable
fun DaySelector(
    days: List<Day>,
    selectedDayIndex: Int,
    modifier: Modifier = Modifier,
    onDaySelected: (Int) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        days.forEachIndexed { index, day ->
            DayItem(
                day = day,
                isSelected = index == selectedDayIndex,
                onClick = { onDaySelected(index) },
            )
        }
    }
}

@Composable
private fun DayItem(
    day: Day,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .clip(CircleShape)
                .clickable(onClick = onClick)
                .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // Day of week label
        Text(
            text = day.dayOfWeek,
            style = YamalTheme.typography.caption,
            color =
                if (isSelected) {
                    YamalTheme.colors.primary
                } else {
                    YamalTheme.colors.textSecondary
                },
        )

        // Day of month circle
        Box(
            modifier =
                Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            YamalTheme.colors.primary
                        } else {
                            androidx.compose.ui.graphics.Color.Transparent
                        },
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                style = YamalTheme.typography.titleSmall,
                color =
                    if (isSelected) {
                        YamalTheme.colors.textLightSolid
                    } else {
                        YamalTheme.colors.textSecondary
                    },
            )
        }
    }
}

// Previews

@Preview
@Composable
private fun DaySelectorPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            var selectedIndex by remember { mutableStateOf(1) }

            val days =
                listOf(
                    Day("MON", 12),
                    Day("TUE", 13),
                    Day("WED", 14),
                    Day("THU", 15),
                    Day("FRI", 16),
                    Day("SAT", 17),
                    Day("SUN", 18),
                )

            DaySelector(
                days = days,
                selectedDayIndex = selectedIndex,
                onDaySelected = { selectedIndex = it },
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun DaySelectorCompactPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.background) {
            val days =
                listOf(
                    Day("MON", 1),
                    Day("TUE", 2),
                    Day("WED", 3),
                )

            DaySelector(
                days = days,
                selectedDayIndex = 0,
                onDaySelected = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
