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
public val arrow_back: ImageVector
  get() {
    if (_arrow_back != null) {
      return _arrow_back!!
    }
    _arrow_back =
      ImageVector.Builder(
          name = "arrow_back",
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
            moveTo(9.48f, 16.17f)
            lineToRelative(7.19f, 7.19f)
            lineTo(15f, 25f)
            lineTo(5f, 15f)
            lineTo(15f, 5f)
            lineToRelative(1.66f, 1.64f)
            lineTo(9.48f, 13.83f)
            horizontalLineTo(25f)
            verticalLineToRelative(2.34f)
            horizontalLineTo(9.48f)
            close()
          }
        }
        .build()
    return _arrow_back!!
  }

private var _arrow_back: ImageVector? = null
