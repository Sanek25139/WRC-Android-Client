package com.example.wrc.ui.icons.generated

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val fast_forward: ImageVector
  get() {
    if (_fast_forward != null) {
      return _fast_forward!!
    }
    _fast_forward =
      ImageVector.Builder(
          name = "fast_forward",
          defaultWidth = 40.dp,
          defaultHeight = 40.dp,
          viewportWidth = 40f,
          viewportHeight = 40f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(4.28f, 30f)
            verticalLineTo(10f)
            lineTo(18.89f, 20f)
            lineTo(4.28f, 30f)
            close()
            moveToRelative(16.86f, 0f)
            verticalLineTo(10f)
            lineTo(35.75f, 20f)
            lineTo(21.14f, 30f)
            close()
            moveTo(7.06f, 20f)
            close()
            moveToRelative(16.86f, 0f)
            close()
            moveTo(7.06f, 24.72f)
            lineTo(13.97f, 20f)
            lineTo(7.06f, 15.28f)
            verticalLineToRelative(9.44f)
            close()
            moveToRelative(16.86f, 0f)
            lineTo(30.83f, 20f)
            lineTo(23.92f, 15.28f)
            verticalLineToRelative(9.44f)
            close()
          }
        }
        .build()
    return _fast_forward!!
  }

private var _fast_forward: ImageVector? = null
