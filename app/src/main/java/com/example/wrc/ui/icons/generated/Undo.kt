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
public val undo: ImageVector
  get() {
    if (_undo != null) {
      return _undo!!
    }
    _undo =
      ImageVector.Builder(
          name = "undo",
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
            moveTo(11.08f, 31.67f)
            verticalLineTo(28.89f)
            horizontalLineTo(23.64f)
            quadToRelative(2.82f, 0f, 4.87f, -1.85f)
            reflectiveQuadToRelative(2.05f, -4.6f)
            reflectiveQuadToRelative(-2.05f, -4.6f)
            reflectiveQuadTo(23.64f, 16f)
            horizontalLineTo(11.94f)
            lineToRelative(4.61f, 4.61f)
            lineToRelative(-1.94f, 1.94f)
            lineTo(6.67f, 14.61f)
            lineTo(14.61f, 6.67f)
            lineToRelative(1.94f, 1.94f)
            lineToRelative(-4.61f, 4.61f)
            horizontalLineTo(23.61f)
            quadToRelative(3.99f, 0f, 6.85f, 2.65f)
            reflectiveQuadToRelative(2.87f, 6.57f)
            reflectiveQuadToRelative(-2.87f, 6.57f)
            reflectiveQuadToRelative(-6.85f, 2.65f)
            horizontalLineTo(11.08f)
            close()
          }
        }
        .build()
    return _undo!!
  }

private var _undo: ImageVector? = null
