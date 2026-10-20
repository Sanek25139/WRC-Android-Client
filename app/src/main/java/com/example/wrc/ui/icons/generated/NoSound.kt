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
public val no_sound: ImageVector
  get() {
    if (_no_sound != null) {
      return _no_sound!!
    }
    _no_sound =
      ImageVector.Builder(
          name = "no_sound",
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
            moveTo(25.53f, 26.58f)
            lineTo(23.56f, 24.61f)
            lineToRelative(4.61f, -4.58f)
            lineTo(23.56f, 15.44f)
            lineToRelative(1.97f, -1.97f)
            lineToRelative(4.58f, 4.61f)
            lineToRelative(4.58f, -4.61f)
            lineToRelative(1.97f, 1.97f)
            lineToRelative(-4.61f, 4.58f)
            lineToRelative(4.61f, 4.58f)
            lineToRelative(-1.97f, 1.97f)
            lineTo(30.11f, 21.97f)
            lineToRelative(-4.58f, 4.61f)
            close()
            moveTo(5f, 25f)
            verticalLineTo(15f)
            horizontalLineToRelative(6.67f)
            lineTo(20f, 6.67f)
            verticalLineTo(33.33f)
            lineTo(11.67f, 25f)
            horizontalLineTo(5f)
            close()
            moveTo(17.22f, 13.58f)
            lineToRelative(-4.33f, 4.19f)
            horizontalLineTo(7.78f)
            verticalLineToRelative(4.44f)
            horizontalLineToRelative(5.11f)
            lineToRelative(4.33f, 4.22f)
            verticalLineTo(13.58f)
            close()
            moveToRelative(-4.42f, 6.39f)
            close()
          }
        }
        .build()
    return _no_sound!!
  }

private var _no_sound: ImageVector? = null
