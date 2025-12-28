package com.yamal.designSystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Dimension constants following Ant Design Mobile guidelines.
 * https://github.com/ant-design/ant-design-mobile/tree/master/src/global
 */
@Immutable
object Dimension {
    /**
     * Spacing constants following a 4dp base grid system.
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
     * Component sizes for interactive elements.
     */
    @Immutable
    object ComponentSize {
        /** Mini component height: 24dp */
        val mini: Dp = 24.dp

        /** Small component height: 32dp */
        val small: Dp = 32.dp

        /** Middle/default component height: 40dp */
        val middle: Dp = 40.dp

        /** Large component height: 48dp */
        val large: Dp = 48.dp
    }

    /**
     * Border radius following Ant Design Mobile guidelines.
     * --adm-radius-s: 4px
     * --adm-radius-m: 8px
     * --adm-radius-l: 12px
     */
    @Immutable
    object BorderRadius {
        /** Small radius: 4dp (--adm-radius-s) */
        val s: Dp = 4.dp

        /** Medium radius: 8dp (--adm-radius-m) */
        val m: Dp = 8.dp

        /** Large radius: 12dp (--adm-radius-l) */
        val l: Dp = 12.dp
    }

    /**
     * Icon sizes.
     */
    @Immutable
    object IconSize {
        /** Extra small icon: 14dp */
        val xs: Dp = 14.dp

        /** Small icon: 17dp */
        val s: Dp = 17.dp

        /** Medium icon: 21dp */
        val m: Dp = 21.dp

        /** Large icon: 36dp */
        val l: Dp = 36.dp
    }

    /**
     * Avatar sizes.
     */
    @Immutable
    object AvatarSize {
        /** Small avatar: 32dp */
        val small: Dp = 32.dp

        /** Default avatar: 48dp */
        val default: Dp = 48.dp

        /** Large avatar: 64dp */
        val large: Dp = 64.dp
    }

    /**
     * Font sizes following Ant Design Mobile typography.
     * ADM uses a 10-level scale from 9sp to 18sp.
     *
     * --adm-font-size-1: 9px
     * --adm-font-size-2: 10px
     * --adm-font-size-3: 11px
     * --adm-font-size-4: 12px
     * --adm-font-size-5: 13px (main)
     * --adm-font-size-6: 14px
     * --adm-font-size-7: 15px
     * --adm-font-size-8: 16px
     * --adm-font-size-9: 17px
     * --adm-font-size-10: 18px
     */
    @Immutable
    object FontSize {
        /** 9sp - smallest size */
        val size1: TextUnit = 9.sp

        /** 10sp */
        val size2: TextUnit = 10.sp

        /** 11sp */
        val size3: TextUnit = 11.sp

        /** 12sp - caption/footnote */
        val size4: TextUnit = 12.sp

        /** 13sp - main/body text (--adm-font-size-main) */
        val size5: TextUnit = 13.sp

        /** 14sp */
        val size6: TextUnit = 14.sp

        /** 15sp */
        val size7: TextUnit = 15.sp

        /** 16sp - subtitle/title */
        val size8: TextUnit = 16.sp

        /** 17sp */
        val size9: TextUnit = 17.sp

        /** 18sp - largest standard size */
        val size10: TextUnit = 18.sp

        /** Main body text size (same as size5) */
        val main: TextUnit = size5

        /** Large display: 24sp */
        val displayLarge: TextUnit = 24.sp

        /** Medium display: 20sp */
        val displayMedium: TextUnit = 20.sp
    }

    /**
     * Line heights following mobile design best practices.
     * Using ~1.4-1.5 ratio for body text and tighter ratios for headings.
     */
    @Immutable
    object FontLineHeight {
        /** For 9sp text */
        val size1: TextUnit = 13.sp

        /** For 10sp text */
        val size2: TextUnit = 14.sp

        /** For 11sp text */
        val size3: TextUnit = 15.sp

        /** For 12sp text */
        val size4: TextUnit = 17.sp

        /** For 13sp text (main) */
        val size5: TextUnit = 18.sp

        /** For 14sp text */
        val size6: TextUnit = 20.sp

        /** For 15sp text */
        val size7: TextUnit = 21.sp

        /** For 16sp text */
        val size8: TextUnit = 22.sp

        /** For 17sp text */
        val size9: TextUnit = 24.sp

        /** For 18sp text */
        val size10: TextUnit = 25.sp

        /** Main line height (for 13sp) */
        val main: TextUnit = size5

        val displayLarge: TextUnit = 32.sp
        val displayMedium: TextUnit = 28.sp
    }
}
