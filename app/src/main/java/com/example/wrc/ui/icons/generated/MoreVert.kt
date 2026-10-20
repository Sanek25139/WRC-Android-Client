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
public val more_vert: ImageVector
  get() {
    if (_more_vert != null) {
      return _more_vert!!
    }
    _more_vert =
      ImageVector.Builder(
          name = "more_vert",
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
            moveTo(15f, 25f)
            quadToRelative(-0.93f, 0f, -1.59f, -0.66f)
            reflectiveQuadTo(12.75f, 22.75f)
            reflectiveQuadToRelative(0.66f, -1.59f)
            reflectiveQuadTo(15f, 20.5f)
            reflectiveQuadToRelative(1.59f, 0.66f)
            reflectiveQuadToRelative(0.66f, 1.59f)
            reflectiveQuadToRelative(-0.66f, 1.59f)
            reflectiveQuadTo(15f, 25f)
            close()
            moveToRelative(0f, -7.75f)
            quadToRelative(-0.93f, 0f, -1.59f, -0.66f)
            reflectiveQuadTo(12.75f, 15f)
            reflectiveQuadToRelative(0.66f, -1.59f)
            reflectiveQuadTo(15f, 12.75f)
            reflectiveQuadToRelative(1.59f, 0.66f)
            reflectiveQuadTo(17.25f, 15f)
            reflectiveQuadToRelative(-0.66f, 1.59f)
            reflectiveQuadTo(15f, 17.25f)
            close()
            moveTo(15f, 9.5f)
            quadToRelative(-0.93f, 0f, -1.59f, -0.66f)
            reflectiveQuadTo(12.75f, 7.25f)
            reflectiveQuadTo(13.41f, 5.66f)
            reflectiveQuadTo(15f, 5f)
            reflectiveQuadToRelative(1.59f, 0.66f)
            reflectiveQuadToRelative(0.66f, 1.59f)
            reflectiveQuadTo(16.59f, 8.84f)
            reflectiveQuadTo(15f, 9.5f)
            close()
          }
        }
        .build()
    return _more_vert!!
  }

private var _more_vert: ImageVector? = null
