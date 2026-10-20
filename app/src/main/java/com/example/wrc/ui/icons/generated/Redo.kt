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
public val redo: ImageVector
  get() {
    if (_redo != null) {
      return _redo!!
    }
    _redo =
      ImageVector.Builder(
          name = "redo",
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
            moveTo(16.39f, 31.67f)
            quadToRelative(-3.99f, 0f, -6.85f, -2.65f)
            reflectiveQuadTo(6.67f, 22.44f)
            reflectiveQuadTo(9.53f, 15.87f)
            reflectiveQuadToRelative(6.85f, -2.65f)
            horizontalLineTo(28.06f)
            lineTo(23.44f, 8.61f)
            lineTo(25.39f, 6.67f)
            lineToRelative(7.94f, 7.94f)
            lineToRelative(-7.94f, 7.94f)
            lineTo(23.44f, 20.61f)
            lineTo(28.06f, 16f)
            horizontalLineTo(16.36f)
            quadToRelative(-2.82f, 0f, -4.87f, 1.85f)
            reflectiveQuadToRelative(-2.05f, 4.6f)
            reflectiveQuadToRelative(2.05f, 4.6f)
            reflectiveQuadToRelative(4.87f, 1.85f)
            horizontalLineTo(28.92f)
            verticalLineToRelative(2.78f)
            horizontalLineTo(16.39f)
            close()
          }
        }
        .build()
    return _redo!!
  }

private var _redo: ImageVector? = null
