package com.yamal.designSystem.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun YamalRating(
    rating: Float,
    maxRating: Int = 5,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Row(modifier = modifier) {
        repeat(maxRating) { index ->
            val starValue = index + 1
            Icon(
                imageVector = if (rating >= starValue) StarFilled
                else if (rating > starValue - 1) StarHalf
                else StarOutline,
                contentDescription = "Rating $starValue",
                tint = color
            )
            Spacer(modifier = Modifier.width(2.dp))
        }
        Text(
            text = rating.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}

@Preview()
@Composable
private fun YamalRatingPreview() {
    YamalTheme {
        YamalRating(
            rating = 4.5f,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
