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
public val play_arrow: ImageVector
  get() {
    if (_play_arrow != null) {
      return _play_arrow!!
    }
    _play_arrow =
      ImageVector.Builder(
          name = "play_arrow",
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
            moveTo(13.33f, 31.58f)
            verticalLineTo(8.25f)
            lineTo(31.67f, 19.92f)
            lineTo(13.33f, 31.58f)
            close()
            moveTo(16.11f, 19.92f)
            close()
            moveToRelative(0f, 6.61f)
            lineTo(26.5f, 19.92f)
            lineTo(16.11f, 13.31f)
            verticalLineTo(26.53f)
            close()
          }
        }
        .build()
    return _play_arrow!!
  }

private var _play_arrow: ImageVector? = null
