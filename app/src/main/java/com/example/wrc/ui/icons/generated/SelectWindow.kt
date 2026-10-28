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
public val select_window: ImageVector
  get() {
    if (_select_window != null) {
      return _select_window!!
    }
    _select_window =
      ImageVector.Builder(
          name = "select_window",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
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
            moveTo(4f, 22f)
            quadTo(3.18f, 22f, 2.59f, 21.41f)
            reflectiveQuadTo(2f, 20f)
            verticalLineTo(11f)
            quadTo(2f, 10.17f, 2.59f, 9.59f)
            reflectiveQuadTo(4f, 9f)
            horizontalLineTo(6f)
            verticalLineTo(4f)
            quadTo(6f, 3.17f, 6.59f, 2.59f)
            reflectiveQuadTo(8f, 2f)
            horizontalLineTo(20f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            reflectiveQuadTo(22f, 4f)
            verticalLineToRelative(9f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(20f, 15f)
            horizontalLineTo(18f)
            verticalLineToRelative(5f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(16f, 22f)
            horizontalLineTo(4f)
            close()
            moveTo(4f, 20f)
            horizontalLineTo(16f)
            verticalLineTo(13f)
            horizontalLineTo(4f)
            verticalLineToRelative(7f)
            close()
            moveTo(18f, 13f)
            horizontalLineToRelative(2f)
            verticalLineTo(6f)
            horizontalLineTo(8f)
            verticalLineTo(9f)
            horizontalLineToRelative(8f)
            quadToRelative(0.82f, 0f, 1.41f, 0.59f)
            reflectiveQuadTo(18f, 11f)
            verticalLineToRelative(2f)
            close()
          }
        }
        .build()
    return _select_window!!
  }

private var _select_window: ImageVector? = null
