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
public val add: ImageVector
  get() {
    if (_add != null) {
      return _add!!
    }
    _add =
      ImageVector.Builder(
          name = "add",
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
            moveTo(13.83f, 16.17f)
            horizontalLineTo(6.25f)
            verticalLineTo(13.83f)
            horizontalLineToRelative(7.58f)
            verticalLineTo(6.25f)
            horizontalLineToRelative(2.34f)
            verticalLineToRelative(7.58f)
            horizontalLineToRelative(7.58f)
            verticalLineToRelative(2.34f)
            horizontalLineTo(16.17f)
            verticalLineToRelative(7.58f)
            horizontalLineTo(13.83f)
            verticalLineTo(16.17f)
            close()
          }
        }
        .build()
    return _add!!
  }

private var _add: ImageVector? = null
