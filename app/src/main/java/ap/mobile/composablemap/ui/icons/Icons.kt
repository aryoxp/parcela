package ap.mobile.composablemap.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object ParcelaIcons {

  val Package: ImageVector
    get() {
      if (_Package != null) {
        return _Package!!
      }
      _Package = ImageVector.Builder(
        name = "Package",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 16f,
        viewportHeight = 16f
      ).apply {
        path(
          fill = SolidColor(Color(0xFF000000)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.EvenOdd
        ) {
          moveTo(8.61f, 3f)
          lineToRelative(5.74f, 1.53f)
          lineTo(15f, 5f)
          verticalLineToRelative(6.74f)
          lineToRelative(-0.37f, 0.48f)
          lineToRelative(-6.13f, 1.69f)
          lineToRelative(-6.14f, -1.69f)
          lineToRelative(-0.36f, -0.48f)
          verticalLineTo(5f)
          lineToRelative(0.61f, -0.47f)
          lineTo(8.34f, 3f)
          horizontalLineToRelative(0.27f)
          close()
          moveToRelative(-0.09f, 1f)
          lineToRelative(-4f, 1f)
          lineToRelative(0.55f, 0.2f)
          lineToRelative(3.43f, 0.9f)
          lineToRelative(3f, -0.81f)
          lineToRelative(0.95f, -0.29f)
          lineToRelative(-3.93f, -1f)
          close()
          moveTo(3f, 11.36f)
          lineToRelative(5f, 1.37f)
          verticalLineTo(7f)
          lineTo(3f, 5.66f)
          verticalLineToRelative(5.7f)
          close()
          moveTo(9f, 7f)
          verticalLineToRelative(5.73f)
          lineToRelative(5f, -1.37f)
          verticalLineTo(5.63f)
          lineToRelative(-2.02f, 0.553f)
          verticalLineTo(8.75f)
          lineToRelative(-1f, 0.26f)
          verticalLineTo(6.457f)
          lineTo(9f, 7f)
          close()
        }
      }.build()
      return _Package!!
    }

  private var _Package: ImageVector? = null

  val Truck: ImageVector
    get() {
      if (_Truck != null) {
        return _Truck!!
      }
      _Truck = ImageVector.Builder(
        name = "Truck",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 16f,
        viewportHeight = 16f
      ).apply {
        path(
          fill = SolidColor(Color(0xFF000000)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(0f, 3.5f)
          arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.5f, 2f)
          horizontalLineToRelative(9f)
          arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 3.5f)
          verticalLineTo(5f)
          horizontalLineToRelative(1.02f)
          arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.17f, 0.563f)
          lineToRelative(1.481f, 1.85f)
          arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.329f, 0.938f)
          verticalLineTo(10.5f)
          arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.5f, 1.5f)
          horizontalLineTo(14f)
          arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, -4f, 0f)
          horizontalLineTo(5f)
          arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, -3.998f, -0.085f)
          arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 10.5f)
          close()
          moveToRelative(1.294f, 7.456f)
          arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.732f, 11f)
          horizontalLineToRelative(5.536f)
          arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.732f, -0.732f)
          verticalLineTo(3.5f)
          arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.5f, -0.5f)
          horizontalLineToRelative(-9f)
          arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.5f, 0.5f)
          verticalLineToRelative(7f)
          arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.294f, 0.456f)
          moveTo(12f, 10f)
          arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.732f, 1f)
          horizontalLineToRelative(0.768f)
          arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.5f, -0.5f)
          verticalLineTo(8.35f)
          arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.11f, -0.312f)
          lineToRelative(-1.48f, -1.85f)
          arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13.02f, 6f)
          horizontalLineTo(12f)
          close()
          moveToRelative(-9f, 1f)
          arcToRelative(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 0f, 2f)
          arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -2f)
          moveToRelative(9f, 0f)
          arcToRelative(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 0f, 2f)
          arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -2f)
        }
      }.build()
      return _Truck!!
    }

  private var _Truck: ImageVector? = null

  val Explore: ImageVector
    get() {
      if (_Explore != null) {
        return _Explore!!
      }
      _Explore = ImageVector.Builder(
        name = "Explore",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
      ).apply {
        path(
          fill = SolidColor(Color.Black),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(260f, 700f)
          lineToRelative(300f, -140f)
          lineToRelative(140f, -300f)
          lineToRelative(-300f, 140f)
          close()
          moveToRelative(220f, -180f)
          quadToRelative(-17f, 0f, -28.5f, -11.5f)
          reflectiveQuadTo(440f, 480f)
          reflectiveQuadToRelative(11.5f, -28.5f)
          reflectiveQuadTo(480f, 440f)
          reflectiveQuadToRelative(28.5f, 11.5f)
          reflectiveQuadTo(520f, 480f)
          reflectiveQuadToRelative(-11.5f, 28.5f)
          reflectiveQuadTo(480f, 520f)
          moveToRelative(0f, 360f)
          quadToRelative(-83f, 0f, -156f, -31.5f)
          reflectiveQuadTo(197f, 763f)
          reflectiveQuadToRelative(-85.5f, -127f)
          reflectiveQuadTo(80f, 480f)
          reflectiveQuadToRelative(31.5f, -156f)
          reflectiveQuadTo(197f, 197f)
          reflectiveQuadToRelative(127f, -85.5f)
          reflectiveQuadTo(480f, 80f)
          reflectiveQuadToRelative(156f, 31.5f)
          reflectiveQuadTo(763f, 197f)
          reflectiveQuadToRelative(85.5f, 127f)
          reflectiveQuadTo(880f, 480f)
          reflectiveQuadToRelative(-31.5f, 156f)
          reflectiveQuadTo(763f, 763f)
          reflectiveQuadToRelative(-127f, 85.5f)
          reflectiveQuadTo(480f, 880f)
          moveToRelative(0f, -80f)
          quadToRelative(134f, 0f, 227f, -93f)
          reflectiveQuadToRelative(93f, -227f)
          reflectiveQuadToRelative(-93f, -227f)
          reflectiveQuadToRelative(-227f, -93f)
          reflectiveQuadToRelative(-227f, 93f)
          reflectiveQuadToRelative(-93f, 227f)
          reflectiveQuadToRelative(93f, 227f)
          reflectiveQuadToRelative(227f, 93f)
          moveToRelative(0f, -320f)
        }
      }.build()
      return _Explore!!
    }

  private var _Explore: ImageVector? = null

  val BoxSeam: ImageVector
    get() {
      if (_BoxSeam != null) {
        return _BoxSeam!!
      }
      _BoxSeam = ImageVector.Builder(
        name = "BoxSeam",
        defaultWidth = 20.dp,
        defaultHeight = 20.dp,
        viewportWidth = 16f,
        viewportHeight = 16f
      ).apply {
        path(
          fill = SolidColor(Color(0xFF000000)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(8.186f, 1.113f)
          arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.372f, 0f)
          lineTo(1.846f, 3.5f)
          lineToRelative(2.404f, 0.961f)
          lineTo(10.404f, 2f)
          close()
          moveToRelative(3.564f, 1.426f)
          lineTo(5.596f, 5f)
          lineTo(8f, 5.961f)
          lineTo(14.154f, 3.5f)
          close()
          moveToRelative(3.25f, 1.7f)
          lineToRelative(-6.5f, 2.6f)
          verticalLineToRelative(7.922f)
          lineToRelative(6.5f, -2.6f)
          verticalLineTo(4.24f)
          close()
          moveTo(7.5f, 14.762f)
          verticalLineTo(6.838f)
          lineTo(1f, 4.239f)
          verticalLineToRelative(7.923f)
          close()
          moveTo(7.443f, 0.184f)
          arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.114f, 0f)
          lineToRelative(7.129f, 2.852f)
          arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16f, 3.5f)
          verticalLineToRelative(8.662f)
          arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.629f, 0.928f)
          lineToRelative(-7.185f, 2.874f)
          arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.372f, 0f)
          lineTo(0.63f, 13.09f)
          arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.63f, -0.928f)
          verticalLineTo(3.5f)
          arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.314f, -0.464f)
          close()
        }
      }.build()
      return _BoxSeam!!
    }

  private var _BoxSeam: ImageVector? = null

  val Server: ImageVector
    get() {
      if (_Server != null) {
        return _Server!!
      }
      _Server = ImageVector.Builder(
        name = "Server",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
      ).apply {
        path(
          fill = null,
          fillAlpha = 1.0f,
          stroke = SolidColor(Color(0xFF0F172A)),
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.5f,
          strokeLineCap = StrokeCap.Round,
          strokeLineJoin = StrokeJoin.Round,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(21.75f, 17.25f)
          verticalLineTo(17.0223f)
          curveTo(21.75f, 16.6753f, 21.7099f, 16.3294f, 21.6304f, 15.9916f)
          lineTo(19.3622f, 6.35199f)
          curveTo(19.0035f, 4.8274f, 17.6431f, 3.75f, 16.077f, 3.75f)
          horizontalLineTo(7.92305f)
          curveTo(6.3569f, 3.75f, 4.9965f, 4.8274f, 4.6378f, 6.352f)
          lineTo(2.36962f, 15.9916f)
          curveTo(2.2901f, 16.3294f, 2.25f, 16.6753f, 2.25f, 17.0223f)
          verticalLineTo(17.25f)
          moveTo(21.75f, 17.25f)
          curveTo(21.75f, 18.9069f, 20.4069f, 20.25f, 18.75f, 20.25f)
          horizontalLineTo(5.25f)
          curveTo(3.5932f, 20.25f, 2.25f, 18.9069f, 2.25f, 17.25f)
          moveTo(21.75f, 17.25f)
          curveTo(21.75f, 15.5931f, 20.4069f, 14.25f, 18.75f, 14.25f)
          horizontalLineTo(5.25f)
          curveTo(3.5932f, 14.25f, 2.25f, 15.5931f, 2.25f, 17.25f)
          moveTo(18.75f, 17.25f)
          horizontalLineTo(18.7575f)
          verticalLineTo(17.2575f)
          horizontalLineTo(18.75f)
          verticalLineTo(17.25f)
          close()
          moveTo(15.75f, 17.25f)
          horizontalLineTo(15.7575f)
          verticalLineTo(17.2575f)
          horizontalLineTo(15.75f)
          verticalLineTo(17.25f)
          close()
        }
      }.build()
      return _Server!!
    }

  private var _Server: ImageVector? = null

  val Automation: ImageVector
    get() {
      if (_Automation != null) {
        return _Automation!!
      }
      _Automation = ImageVector.Builder(
        name = "Automation",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
      ).apply {
        path(
          fill = SolidColor(Color.Black),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(240f, 840f)
          quadToRelative(-66f, 0f, -113f, -47f)
          reflectiveQuadTo(80f, 680f)
          reflectiveQuadToRelative(47f, -113f)
          reflectiveQuadToRelative(113f, -47f)
          quadToRelative(22f, 0f, 42f, 5.5f)
          reflectiveQuadToRelative(37f, 15.5f)
          lineToRelative(105f, -106f)
          lineToRelative(-109f, -111f)
          quadToRelative(-18f, -18f, -26.5f, -39.5f)
          reflectiveQuadTo(280f, 241f)
          quadToRelative(0f, -47f, 33f, -84f)
          reflectiveQuadToRelative(87f, -37f)
          horizontalLineToRelative(160f)
          quadToRelative(54f, 0f, 87f, 37f)
          reflectiveQuadToRelative(33f, 84f)
          quadToRelative(0f, 22f, -8.5f, 44f)
          reflectiveQuadTo(645f, 325f)
          lineTo(536f, 435f)
          lineToRelative(104f, 106f)
          quadToRelative(17f, -11f, 37.5f, -16f)
          reflectiveQuadToRelative(42.5f, -5f)
          quadToRelative(66f, 0f, 113f, 47f)
          reflectiveQuadToRelative(47f, 113f)
          reflectiveQuadToRelative(-47f, 113f)
          reflectiveQuadToRelative(-113f, 47f)
          reflectiveQuadToRelative(-113f, -47f)
          reflectiveQuadToRelative(-47f, -113f)
          quadToRelative(0f, -22f, 6f, -43f)
          reflectiveQuadToRelative(17f, -40f)
          lineTo(480f, 492f)
          lineTo(377f, 597f)
          quadToRelative(11f, 19f, 17f, 40f)
          reflectiveQuadToRelative(6f, 43f)
          quadToRelative(0f, 66f, -47f, 113f)
          reflectiveQuadToRelative(-113f, 47f)
          moveToRelative(0f, -80f)
          quadToRelative(33f, 0f, 56.5f, -23.5f)
          reflectiveQuadTo(320f, 680f)
          reflectiveQuadToRelative(-23.5f, -56.5f)
          reflectiveQuadTo(240f, 600f)
          reflectiveQuadToRelative(-56.5f, 23.5f)
          reflectiveQuadTo(160f, 680f)
          reflectiveQuadToRelative(23.5f, 56.5f)
          reflectiveQuadTo(240f, 760f)
          moveToRelative(480f, 0f)
          quadToRelative(33f, 0f, 56.5f, -23.5f)
          reflectiveQuadTo(800f, 680f)
          reflectiveQuadToRelative(-23.5f, -56.5f)
          reflectiveQuadTo(720f, 600f)
          reflectiveQuadToRelative(-56.5f, 23.5f)
          reflectiveQuadTo(640f, 680f)
          reflectiveQuadToRelative(23.5f, 56.5f)
          reflectiveQuadTo(720f, 760f)
          moveTo(400f, 200f)
          quadToRelative(-18f, 0f, -29f, 12f)
          reflectiveQuadToRelative(-11f, 28f)
          quadToRelative(0f, 8f, 3f, 15f)
          reflectiveQuadToRelative(9f, 13f)
          lineToRelative(108f, 110f)
          lineToRelative(108f, -109f)
          quadToRelative(6f, -6f, 9f, -13.5f)
          reflectiveQuadToRelative(3f, -15.5f)
          quadToRelative(0f, -16f, -11f, -28f)
          reflectiveQuadToRelative(-29f, -12f)
          close()
          moveToRelative(320f, 480f)
        }
      }.build()
      return _Automation!!
    }

  private var _Automation: ImageVector? = null

  val CloudSync: ImageVector
    get() {
      if (_Cloud_sync != null) {
        return _Cloud_sync!!
      }
      _Cloud_sync = ImageVector.Builder(
        name = "Cloud_sync",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
      ).apply {
        path(
          fill = SolidColor(Color.Black),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(160f, 800f)
          verticalLineToRelative(-80f)
          horizontalLineToRelative(109f)
          quadToRelative(-51f, -44f, -80f, -106f)
          reflectiveQuadToRelative(-29f, -134f)
          quadToRelative(0f, -112f, 68f, -197.5f)
          reflectiveQuadTo(400f, 170f)
          verticalLineToRelative(84f)
          quadToRelative(-70f, 25f, -115f, 86.5f)
          reflectiveQuadTo(240f, 480f)
          quadToRelative(0f, 54f, 21.5f, 99.5f)
          reflectiveQuadTo(320f, 658f)
          verticalLineToRelative(-98f)
          horizontalLineToRelative(80f)
          verticalLineToRelative(240f)
          close()
          moveToRelative(440f, 0f)
          quadToRelative(-50f, 0f, -85f, -35f)
          reflectiveQuadToRelative(-35f, -85f)
          quadToRelative(0f, -48f, 33f, -82.5f)
          reflectiveQuadToRelative(81f, -36.5f)
          quadToRelative(17f, -36f, 50.5f, -58.5f)
          reflectiveQuadTo(720f, 480f)
          quadToRelative(53f, 0f, 91.5f, 34.5f)
          reflectiveQuadTo(858f, 600f)
          quadToRelative(42f, 0f, 72f, 29f)
          reflectiveQuadToRelative(30f, 70f)
          quadToRelative(0f, 42f, -29f, 71.5f)
          reflectiveQuadTo(860f, 800f)
          close()
          moveToRelative(116f, -360f)
          quadToRelative(-7f, -41f, -27f, -76f)
          reflectiveQuadToRelative(-49f, -62f)
          verticalLineToRelative(98f)
          horizontalLineToRelative(-80f)
          verticalLineToRelative(-240f)
          horizontalLineToRelative(240f)
          verticalLineToRelative(80f)
          horizontalLineTo(691f)
          quadToRelative(43f, 38f, 70.5f, 89f)
          reflectiveQuadTo(797f, 440f)
          close()
          moveTo(600f, 720f)
          horizontalLineToRelative(260f)
          quadToRelative(8f, 0f, 14f, -6f)
          reflectiveQuadToRelative(6f, -14f)
          reflectiveQuadToRelative(-6f, -14f)
          reflectiveQuadToRelative(-14f, -6f)
          horizontalLineToRelative(-70f)
          verticalLineToRelative(-50f)
          quadToRelative(0f, -29f, -20.5f, -49.5f)
          reflectiveQuadTo(720f, 560f)
          reflectiveQuadToRelative(-49.5f, 20.5f)
          reflectiveQuadTo(650f, 630f)
          verticalLineToRelative(10f)
          horizontalLineToRelative(-50f)
          quadToRelative(-17f, 0f, -28.5f, 11.5f)
          reflectiveQuadTo(560f, 680f)
          reflectiveQuadToRelative(11.5f, 28.5f)
          reflectiveQuadTo(600f, 720f)
          moveToRelative(120f, -80f)
        }
      }.build()
      return _Cloud_sync!!
    }

  private var _Cloud_sync: ImageVector? = null


}