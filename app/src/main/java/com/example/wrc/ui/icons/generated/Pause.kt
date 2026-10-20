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
public val pause: ImageVector
  get() {
    if (_pause != null) {
      return _pause!!
    }
    _pause =
      ImageVector.Builder(
          name = "pause",
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
            moveTo(21.81f, 31.67f)
            verticalLineTo(8.33f)
            horizontalLineToRelative(9.86f)
            verticalLineTo(31.67f)
            horizontalLineTo(21.81f)
            close()
            moveToRelative(-13.47f, 0f)
            verticalLineTo(8.33f)
            horizontalLineToRelative(9.86f)
            verticalLineTo(31.67f)
            horizontalLineTo(8.33f)
            close()
            moveTo(24.58f, 28.89f)
            horizontalLineToRelative(4.31f)
            verticalLineTo(11.11f)
            horizontalLineTo(24.58f)
            verticalLineTo(28.89f)
            close()
            moveToRelative(-13.47f, 0f)
            horizontalLineToRelative(4.31f)
            verticalLineTo(11.11f)
            horizontalLineTo(11.11f)
            verticalLineTo(28.89f)
            close()
            moveToRelative(0f, -17.78f)
            verticalLineTo(28.89f)
            verticalLineTo(11.11f)
            close()
            moveToRelative(13.47f, 0f)
            verticalLineTo(28.89f)
            verticalLineTo(11.11f)
            close()
          }
        }
        .build()
    return _pause!!
  }

private var _pause: ImageVector? = null
