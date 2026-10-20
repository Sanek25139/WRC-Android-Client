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
public val volume_down: ImageVector
  get() {
    if (_volume_down != null) {
      return _volume_down!!
    }
    _volume_down =
      ImageVector.Builder(
          name = "volume_down",
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
            moveTo(8.33f, 25f)
            verticalLineTo(15f)
            horizontalLineTo(15f)
            lineTo(23.33f, 6.67f)
            verticalLineTo(33.33f)
            lineTo(15f, 25f)
            horizontalLineTo(8.33f)
            close()
            moveToRelative(17.78f, 1.89f)
            verticalLineTo(13.06f)
            quadToRelative(2.13f, 0.76f, 3.42f, 2.68f)
            reflectiveQuadTo(30.83f, 20f)
            quadToRelative(0f, 2.38f, -1.3f, 4.25f)
            reflectiveQuadToRelative(-3.42f, 2.64f)
            close()
            moveTo(20.56f, 13.58f)
            lineToRelative(-4.33f, 4.19f)
            horizontalLineTo(11.11f)
            verticalLineToRelative(4.44f)
            horizontalLineToRelative(5.11f)
            lineToRelative(4.33f, 4.22f)
            verticalLineTo(13.58f)
            close()
            moveTo(15.78f, 20f)
            close()
          }
        }
        .build()
    return _volume_down!!
  }

private var _volume_down: ImageVector? = null
