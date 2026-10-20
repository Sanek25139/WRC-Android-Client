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
public val play_pause: ImageVector
  get() {
    if (_play_pause != null) {
      return _play_pause!!
    }
    _play_pause =
      ImageVector.Builder(
          name = "play_pause",
          defaultWidth = 100.dp,
          defaultHeight = 100.dp,
          viewportWidth = 100f,
          viewportHeight = 100f,
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
            moveTo(20.83f, 65.42f)
            verticalLineTo(34.58f)
            lineTo(43.75f, 50f)
            lineTo(20.83f, 65.42f)
            close()
            moveToRelative(35.42f, 1.25f)
            verticalLineTo(33.33f)
            horizontalLineTo(62.5f)
            verticalLineTo(66.67f)
            horizontalLineTo(56.25f)
            close()
            moveToRelative(16.67f, 0f)
            verticalLineTo(33.33f)
            horizontalLineToRelative(6.25f)
            verticalLineTo(66.67f)
            horizontalLineTo(72.92f)
            close()
          }
        }
        .build()
    return _play_pause!!
  }

private var _play_pause: ImageVector? = null
