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
public val arrow_back_ios: ImageVector
  get() {
    if (_arrow_back_ios != null) {
      return _arrow_back_ios!!
    }
    _arrow_back_ios =
      ImageVector.Builder(
          name = "arrow_back_ios",
          defaultWidth = 30.dp,
          defaultHeight = 30.dp,
          viewportWidth = 30f,
          viewportHeight = 30f,
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
            moveTo(12.5f, 27.5f)
            lineTo(0f, 15f)
            lineTo(12.5f, 2.5f)
            lineToRelative(2.1f, 2.11f)
            lineTo(4.21f, 15f)
            lineTo(14.6f, 25.39f)
            lineTo(12.5f, 27.5f)
            close()
          }
        }
        .build()
    return _arrow_back_ios!!
  }

private var _arrow_back_ios: ImageVector? = null
