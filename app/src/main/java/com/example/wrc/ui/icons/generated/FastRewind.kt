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
public val fast_rewind: ImageVector
  get() {
    if (_fast_rewind != null) {
      return _fast_rewind!!
    }
    _fast_rewind =
      ImageVector.Builder(
          name = "fast_rewind",
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
            moveTo(35.67f, 30f)
            lineTo(21.06f, 20f)
            lineTo(35.67f, 10f)
            verticalLineTo(30f)
            close()
            moveTo(18.94f, 30f)
            lineTo(4.33f, 20f)
            lineTo(18.94f, 10f)
            verticalLineTo(30f)
            close()
            moveTo(16.17f, 20f)
            close()
            moveToRelative(16.72f, 0f)
            close()
            moveTo(16.17f, 24.72f)
            verticalLineTo(15.28f)
            lineTo(9.25f, 20f)
            lineToRelative(6.92f, 4.72f)
            close()
            moveToRelative(16.72f, 0f)
            verticalLineTo(15.28f)
            lineTo(25.97f, 20f)
            lineToRelative(6.92f, 4.72f)
            close()
          }
        }
        .build()
    return _fast_rewind!!
  }

private var _fast_rewind: ImageVector? = null
