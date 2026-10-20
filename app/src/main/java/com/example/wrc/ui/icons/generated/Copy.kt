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
public val content_copy: ImageVector
  get() {
    if (_content_copy != null) {
      return _content_copy!!
    }
    _content_copy =
      ImageVector.Builder(
          name = "content_copy",
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
            moveTo(13.33f, 31.11f)
            quadToRelative(-1.12f, 0f, -1.95f, -0.83f)
            reflectiveQuadTo(10.56f, 28.33f)
            verticalLineTo(6.11f)
            quadToRelative(0f, -1.12f, 0.83f, -1.95f)
            reflectiveQuadTo(13.33f, 3.33f)
            horizontalLineTo(30.56f)
            quadToRelative(1.12f, 0f, 1.95f, 0.83f)
            reflectiveQuadToRelative(0.83f, 1.95f)
            verticalLineTo(28.33f)
            quadToRelative(0f, 1.12f, -0.83f, 1.95f)
            reflectiveQuadToRelative(-1.95f, 0.83f)
            horizontalLineTo(13.33f)
            close()
            moveToRelative(0f, -2.78f)
            horizontalLineTo(30.56f)
            verticalLineTo(6.11f)
            horizontalLineTo(13.33f)
            verticalLineTo(28.33f)
            close()
            moveTo(7.78f, 36.67f)
            quadToRelative(-1.12f, 0f, -1.95f, -0.83f)
            reflectiveQuadTo(5f, 33.89f)
            verticalLineToRelative(-25f)
            horizontalLineTo(7.78f)
            verticalLineToRelative(25f)
            horizontalLineToRelative(20f)
            verticalLineToRelative(2.78f)
            horizontalLineToRelative(-20f)
            close()
            moveToRelative(5.56f, -8.33f)
            verticalLineTo(6.11f)
            verticalLineTo(28.33f)
            close()
          }
        }
        .build()
    return _content_copy!!
  }

private var _content_copy: ImageVector? = null
