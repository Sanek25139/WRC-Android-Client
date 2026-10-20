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
public val menu: ImageVector
  get() {
    if (_menu != null) {
      return _menu!!
    }
    _menu =
      ImageVector.Builder(
          name = "menu",
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
            moveTo(3.75f, 22.5f)
            verticalLineTo(20.16f)
            horizontalLineToRelative(22.5f)
            verticalLineTo(22.5f)
            horizontalLineTo(3.75f)
            close()
            moveToRelative(0f, -6.33f)
            verticalLineTo(13.83f)
            horizontalLineToRelative(22.5f)
            verticalLineToRelative(2.34f)
            horizontalLineTo(3.75f)
            close()
            moveToRelative(0f, -6.33f)
            verticalLineTo(7.5f)
            horizontalLineToRelative(22.5f)
            verticalLineTo(9.84f)
            horizontalLineTo(3.75f)
            close()
          }
        }
        .build()
    return _menu!!
  }

private var _menu: ImageVector? = null
