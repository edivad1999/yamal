package com.yamal.designSystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Dimension constants following Ant Design guidelines.
 * https://ant.design/docs/spec/layout
 */
@Immutable
object Dimension {
    /**
     * Spacing constants following Ant Design's spacing system.
     * Base unit is 8dp with smaller 4dp option.
     */
    @Immutable
    object Spacing {
        /** Extra extra small: 4dp */
        val xxs: Dp = 4.dp

        /** Extra small: 8dp */
        val xs: Dp = 8.dp

        /** Small: 12dp */
        val sm: Dp = 12.dp

        /** Medium (base): 16dp */
        val md: Dp = 16.dp

        /** Large: 24dp */
        val lg: Dp = 24.dp

        /** Extra large: 32dp */
        val xl: Dp = 32.dp

        /** Extra extra large: 48dp */
        val xxl: Dp = 48.dp
    }

    /**
     * Component sizes following Ant Design's size system.
     */
    @Immutable
    object ComponentSize {
        /** Small component height: 24dp */
        val small: Dp = 24.dp

        /** Middle/default component height: 32dp */
        val middle: Dp = 32.dp

        /** Large component height: 40dp */
        val large: Dp = 40.dp
    }

    /**
     * Border radius following Ant Design guidelines.
     */
    @Immutable
    object BorderRadius {
        /** Small radius: 2dp */
        val xs: Dp = 2.dp

        /** Small radius: 4dp */
        val sm: Dp = 4.dp

        /** Base radius: 6dp */
        val base: Dp = 6.dp

        /** Large radius: 8dp */
        val lg: Dp = 8.dp

        /** Extra large radius: 16dp */
        val xl: Dp = 16.dp
    }

    /**
     * Icon sizes following Ant Design guidelines.
     */
    @Immutable
    object IconSize {
        /** Small icon: 14dp */
        val sm: Dp = 14.dp

        /** Medium icon: 16dp */
        val md: Dp = 16.dp

        /** Large icon: 20dp */
        val lg: Dp = 20.dp

        /** Extra large icon: 24dp */
        val xl: Dp = 24.dp
    }

    /**
     * Avatar sizes following Ant Design guidelines.
     */
    @Immutable
    object AvatarSize {
        /** Small avatar: 24dp */
        val small: Dp = 24.dp

        /** Default avatar: 32dp */
        val default: Dp = 32.dp

        /** Large avatar: 40dp */
        val large: Dp = 40.dp
    }

    /**
     * Font sizes following Ant Design typography.
     * https://ant.design/docs/spec/font
     */
    @Immutable
    object FontSize {
        val h1 = 38.sp
        val h2 = 30.sp
        val h3 = 24.sp
        val h4 = 20.sp
        val h5 = 16.sp
        val body = 14.sp
        val footnote = 12.sp
    }

    /**
     * Line heights following Ant Design typography.
     */
    @Immutable
    object FontLineHeight {
        val h1 = 46.sp
        val h2 = 40.sp
        val h3 = 32.sp
        val h4 = 28.sp
        val h5 = 24.sp
        val body = 22.sp
        val footnote = 20.sp
    }
}
