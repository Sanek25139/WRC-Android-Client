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
public val volume_up: ImageVector
  get() {
    if (_volume_up != null) {
      return _volume_up!!
    }
    _volume_up =
      ImageVector.Builder(
          name = "volume_up",
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
            moveTo(23.33f, 34.54f)
            verticalLineTo(31.68f)
            quadToRelative(3.94f, -1.14f, 6.42f, -4.38f)
            reflectiveQuadToRelative(2.47f, -7.35f)
            reflectiveQuadTo(29.76f, 12.6f)
            reflectiveQuadTo(23.33f, 8.24f)
            verticalLineTo(5.38f)
            quadToRelative(5.17f, 1.17f, 8.42f, 5.23f)
            reflectiveQuadTo(35f, 19.96f)
            reflectiveQuadToRelative(-3.25f, 9.35f)
            reflectiveQuadToRelative(-8.42f, 5.23f)
            close()
            moveTo(5f, 25f)
            verticalLineTo(15f)
            horizontalLineToRelative(6.67f)
            lineTo(20f, 6.67f)
            verticalLineTo(33.33f)
            lineTo(11.67f, 25f)
            horizontalLineTo(5f)
            close()
            moveToRelative(17.78f, 1.89f)
            verticalLineTo(13.06f)
            quadToRelative(2.18f, 0.78f, 3.45f, 2.69f)
            reflectiveQuadTo(27.5f, 20f)
            quadToRelative(0f, 2.29f, -1.28f, 4.2f)
            reflectiveQuadToRelative(-3.44f, 2.69f)
            close()
            moveTo(17.22f, 13.58f)
            lineToRelative(-4.33f, 4.19f)
            horizontalLineTo(7.78f)
            verticalLineToRelative(4.44f)
            horizontalLineToRelative(5.11f)
            lineToRelative(4.33f, 4.22f)
            verticalLineTo(13.58f)
            close()
            moveToRelative(-4f, 6.42f)
            close()
          }
        }
        .build()
    return _volume_up!!
  }

private var _volume_up: ImageVector? = null
