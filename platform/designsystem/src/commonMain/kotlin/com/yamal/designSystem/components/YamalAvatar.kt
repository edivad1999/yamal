package com.yamal.designSystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.yamal.designSystem.theme.Dimension
import com.yamal.designSystem.theme.YamalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Avatar shape following Ant Design guidelines.
 */
@Immutable
enum class YamalAvatarShape {
    Circle,
    Square,
}

/**
 * Avatar size following Ant Design guidelines.
 */
@Immutable
enum class YamalAvatarSize {
    Large,
    Default,
    Small,
}

/**
 * An avatar component following Ant Design guidelines.
 *
 * @param modifier Modifier for the avatar
 * @param size Avatar size (Large, Default, Small) or custom Dp
 * @param shape Avatar shape (Circle or Square)
 * @param src Image URL to load
 * @param text Text to display (first character shown)
 * @param icon Fallback icon content
 * @param backgroundColor Custom background color
 */
@Composable
fun YamalAvatar(
    modifier: Modifier = Modifier,
    size: YamalAvatarSize = YamalAvatarSize.Default,
    customSize: Dp? = null,
    shape: YamalAvatarShape = YamalAvatarShape.Circle,
    src: String? = null,
    text: String? = null,
    icon: (@Composable () -> Unit)? = null,
    backgroundColor: Color? = null,
) {
    val colors = YamalTheme.colors
    val typography = YamalTheme.typography

    val avatarSize =
        customSize ?: when (size) {
            YamalAvatarSize.Large -> Dimension.AvatarSize.large
            YamalAvatarSize.Default -> Dimension.AvatarSize.default
            YamalAvatarSize.Small -> Dimension.AvatarSize.small
        }

    val avatarShape: Shape =
        when (shape) {
            YamalAvatarShape.Circle -> CircleShape
            YamalAvatarShape.Square -> RoundedCornerShape(Dimension.BorderRadius.sm)
        }

    val bgColor = backgroundColor ?: colors.neutralColors.divider

    val fontSize =
        when {
            customSize != null -> (customSize.value * 0.4f).sp
            size == YamalAvatarSize.Large -> 18.sp
            size == YamalAvatarSize.Default -> 14.sp
            else -> 12.sp
        }

    Surface(
        modifier = modifier.size(avatarSize),
        shape = avatarShape,
        color = bgColor,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            when {
                src != null -> {
                    AsyncImage(
                        model = src,
                        contentDescription = text ?: "Avatar",
                        modifier =
                            Modifier
                                .size(avatarSize)
                                .clip(avatarShape),
                        contentScale = ContentScale.Crop,
                    )
                }

                text != null -> {
                    Text(
                        text = text.take(1).uppercase(),
                        fontSize = fontSize,
                        color = colors.neutralColors.background,
                        textAlign = TextAlign.Center,
                    )
                }

                icon != null -> {
                    icon()
                }
            }
        }
    }
}

/**
 * An avatar group component following Ant Design Avatar.Group.
 *
 * @param modifier Modifier for the group
 * @param maxCount Maximum avatars to display before showing "+N"
 * @param size Size for all avatars in the group
 * @param shape Shape for all avatars in the group
 * @param avatars List of avatar content
 */
@Composable
fun YamalAvatarGroup(
    avatars: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    maxCount: Int = 5,
    size: YamalAvatarSize = YamalAvatarSize.Default,
    shape: YamalAvatarShape = YamalAvatarShape.Circle,
) {
    val colors = YamalTheme.colors

    val avatarSize =
        when (size) {
            YamalAvatarSize.Large -> Dimension.AvatarSize.large
            YamalAvatarSize.Default -> Dimension.AvatarSize.default
            YamalAvatarSize.Small -> Dimension.AvatarSize.small
        }

    val overlapOffset = avatarSize * 0.3f
    val visibleAvatars = avatars.take(maxCount)
    val remainingCount = avatars.size - maxCount

    Row(modifier = modifier) {
        visibleAvatars.forEachIndexed { index, avatar ->
            Box(
                modifier = Modifier.offset(x = -(overlapOffset * index)),
            ) {
                avatar()
            }
        }

        if (remainingCount > 0) {
            Box(
                modifier = Modifier.offset(x = -(overlapOffset * visibleAvatars.size)),
            ) {
                YamalAvatar(
                    size = size,
                    shape = shape,
                    text = "+$remainingCount",
                    backgroundColor = colors.neutralColors.secondaryText,
                )
            }
        }
    }
}

// Previews

@Preview
@Composable
private fun YamalAvatarSizesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                YamalAvatar(size = YamalAvatarSize.Large, text = "L")
                YamalAvatar(size = YamalAvatarSize.Default, text = "D")
                YamalAvatar(size = YamalAvatarSize.Small, text = "S")
            }
        }
    }
}

@Preview
@Composable
private fun YamalAvatarShapesPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                YamalAvatar(shape = YamalAvatarShape.Circle, text = "C")
                YamalAvatar(shape = YamalAvatarShape.Square, text = "S")
            }
        }
    }
}

@Preview
@Composable
private fun YamalAvatarGroupPreview() {
    YamalTheme {
        Surface(color = YamalTheme.colors.neutralColors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                YamalAvatarGroup(
                    avatars =
                        listOf(
                            { YamalAvatar(text = "A", backgroundColor = Color(0xFFf56a00)) },
                            { YamalAvatar(text = "B", backgroundColor = Color(0xFF7265e6)) },
                            { YamalAvatar(text = "C", backgroundColor = Color(0xFFffbf00)) },
                        ),
                )

                YamalAvatarGroup(
                    avatars =
                        listOf(
                            { YamalAvatar(text = "A", backgroundColor = Color(0xFFf56a00)) },
                            { YamalAvatar(text = "B", backgroundColor = Color(0xFF7265e6)) },
                            { YamalAvatar(text = "C", backgroundColor = Color(0xFFffbf00)) },
                            { YamalAvatar(text = "D", backgroundColor = Color(0xFF00a2ae)) },
                            { YamalAvatar(text = "E", backgroundColor = Color(0xFF87d068)) },
                            { YamalAvatar(text = "F", backgroundColor = Color(0xFFfa8c16)) },
                        ),
                    maxCount = 4,
                )
            }
        }
    }
}
