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
public val save: ImageVector
  get() {
    if (_save != null) {
      return _save!!
    }
    _save =
      ImageVector.Builder(
          name = "save",
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
            moveTo(26.25f, 8.73f)
            verticalLineTo(23.91f)
            quadToRelative(0f, 0.96f, -0.69f, 1.65f)
            reflectiveQuadToRelative(-1.65f, 0.69f)
            horizontalLineTo(6.09f)
            quadToRelative(-0.96f, 0f, -1.65f, -0.69f)
            reflectiveQuadTo(3.75f, 23.91f)
            verticalLineTo(6.09f)
            quadToRelative(0f, -0.96f, 0.69f, -1.65f)
            reflectiveQuadTo(6.09f, 3.75f)
            horizontalLineTo(21.27f)
            lineToRelative(4.98f, 4.98f)
            close()
            moveTo(23.91f, 9.73f)
            lineTo(20.27f, 6.09f)
            horizontalLineTo(6.09f)
            verticalLineTo(23.91f)
            horizontalLineTo(23.91f)
            verticalLineTo(9.73f)
            close()
            moveTo(17.57f, 21.4f)
            quadToRelative(1.06f, -1.06f, 1.06f, -2.57f)
            reflectiveQuadTo(17.57f, 16.27f)
            reflectiveQuadTo(15f, 15.21f)
            reflectiveQuadToRelative(-2.57f, 1.06f)
            reflectiveQuadToRelative(-1.06f, 2.57f)
            reflectiveQuadToRelative(1.06f, 2.57f)
            reflectiveQuadTo(15f, 22.46f)
            reflectiveQuadTo(17.57f, 21.4f)
            close()
            moveTo(7.45f, 12.31f)
            horizontalLineTo(18.68f)
            verticalLineTo(7.45f)
            horizontalLineTo(7.45f)
            verticalLineToRelative(4.87f)
            close()
            moveTo(6.09f, 9.73f)
            verticalLineTo(23.91f)
            verticalLineTo(6.09f)
            verticalLineTo(9.73f)
            close()
          }
        }
        .build()
    return _save!!
  }

private var _save: ImageVector? = null
