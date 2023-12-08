package core

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

sealed interface Icon {

    data class Drawable(val drawable: String) : Icon {

        @OptIn(ExperimentalResourceApi::class)
        @Composable
        override fun asPainter() = painterResource(res = drawable)
    }

    data class Vector(val imageVector: ImageVector) : Icon {

        @Composable override fun asPainter() = rememberVectorPainter(image = imageVector)
    }

    @Composable fun asPainter(): Painter
}
