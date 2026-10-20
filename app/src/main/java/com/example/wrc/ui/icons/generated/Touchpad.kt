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
public val call_to_action: ImageVector
  get() {
    if (_call_to_action != null) {
      return _call_to_action!!
    }
    _call_to_action =
      ImageVector.Builder(
          name = "call_to_action",
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
            moveTo(9.89f, 28.5f)
            horizontalLineTo(30.11f)
            verticalLineTo(23.92f)
            horizontalLineTo(9.89f)
            verticalLineTo(28.5f)
            close()
            moveTo(6.11f, 33.33f)
            quadToRelative(-1.12f, 0f, -1.95f, -0.83f)
            reflectiveQuadTo(3.33f, 30.56f)
            verticalLineTo(9.44f)
            quadToRelative(0f, -1.12f, 0.83f, -1.95f)
            reflectiveQuadTo(6.11f, 6.67f)
            horizontalLineTo(33.89f)
            quadToRelative(1.12f, 0f, 1.95f, 0.83f)
            reflectiveQuadToRelative(0.83f, 1.95f)
            verticalLineTo(30.56f)
            quadToRelative(0f, 1.12f, -0.83f, 1.95f)
            reflectiveQuadToRelative(-1.95f, 0.83f)
            horizontalLineTo(6.11f)
            close()
            moveToRelative(0f, -2.78f)
            horizontalLineTo(33.89f)
            verticalLineTo(9.44f)
            horizontalLineTo(6.11f)
            verticalLineTo(30.56f)
            close()
            moveToRelative(0f, 0f)
            verticalLineTo(9.44f)
            verticalLineTo(30.56f)
            close()
          }
        }
        .build()
    return _call_to_action!!
  }

private var _call_to_action: ImageVector? = null
