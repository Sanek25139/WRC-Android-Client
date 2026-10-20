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
public val gamepad: ImageVector
  get() {
    if (_gamepad != null) {
      return _gamepad!!
    }
    _gamepad =
      ImageVector.Builder(
          name = "gamepad",
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
            moveTo(20f, 13.31f)
            close()
            moveTo(26.69f, 20f)
            close()
            moveTo(13.33f, 20f)
            close()
            moveTo(20f, 26.67f)
            close()
            moveToRelative(0f, -9.47f)
            lineTo(15.25f, 12.42f)
            verticalLineTo(3.33f)
            horizontalLineToRelative(9.53f)
            verticalLineToRelative(9.08f)
            lineTo(20f, 17.19f)
            close()
            moveToRelative(7.58f, 7.56f)
            lineTo(22.81f, 20f)
            lineToRelative(4.78f, -4.78f)
            horizontalLineToRelative(9.08f)
            verticalLineToRelative(9.53f)
            horizontalLineTo(27.58f)
            close()
            moveToRelative(-24.25f, 0f)
            verticalLineTo(15.22f)
            horizontalLineToRelative(9.08f)
            lineTo(17.19f, 20f)
            lineToRelative(-4.78f, 4.75f)
            horizontalLineTo(3.33f)
            close()
            moveTo(15.25f, 36.67f)
            verticalLineTo(27.58f)
            lineTo(20f, 22.81f)
            lineToRelative(4.78f, 4.78f)
            verticalLineToRelative(9.08f)
            horizontalLineTo(15.25f)
            close()
            moveTo(20f, 13.31f)
            lineToRelative(2f, -2f)
            verticalLineTo(6.11f)
            horizontalLineTo(18.03f)
            verticalLineToRelative(5.19f)
            lineToRelative(1.97f, 2f)
            close()
            moveTo(6.11f, 21.97f)
            horizontalLineToRelative(5.22f)
            lineToRelative(2f, -1.97f)
            lineToRelative(-2f, -2f)
            horizontalLineTo(6.11f)
            verticalLineToRelative(3.97f)
            close()
            moveTo(18.03f, 33.89f)
            horizontalLineTo(22f)
            verticalLineTo(28.67f)
            lineToRelative(-2f, -2f)
            lineToRelative(-1.97f, 2f)
            verticalLineToRelative(5.22f)
            close()
            moveTo(28.69f, 21.97f)
            horizontalLineToRelative(5.19f)
            verticalLineTo(18f)
            horizontalLineTo(28.69f)
            lineToRelative(-2f, 2f)
            lineToRelative(2f, 1.97f)
            close()
          }
        }
        .build()
    return _gamepad!!
  }

private var _gamepad: ImageVector? = null
