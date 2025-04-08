package ap.mobile.composablemap.ui.theme

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.scale
import kotlinx.coroutines.time.delay

@Composable
fun Modifier.doublePulseEffect(
  targetScale: Float = 1.5f,
  initialScale: Float = 1f,
  brush: Brush = SolidColor(Color.Black.copy(alpha = 0.32f)),
  shape: Shape = CircleShape,
  duration: Int = 1200
) : Modifier {
  return this.pulseEffect(
    targetScale, initialScale, brush, shape, tween(duration, easing = FastOutSlowInEasing)
  ).pulseEffect(
    targetScale, initialScale, brush, shape, tween((duration * .7f).toInt(),
      delayMillis = (duration * .3f).toInt(), easing = LinearEasing
    )
  )
}

@Composable
fun Modifier.pulseEffect(
  targetScale: Float = 1.5f,
  initialScale: Float = 1f,
  brush: Brush = SolidColor(Color.Black.copy(alpha = 0.32f)),
  shape: Shape = CircleShape,
  animationSpec: DurationBasedAnimationSpec<Float> = tween(1200)
) : Modifier {
  val pulseTransition = rememberInfiniteTransition("PulseTransition")
  val pulseScale by pulseTransition.animateFloat(
    initialValue = initialScale,
    targetValue = targetScale,
    animationSpec = infiniteRepeatable(animation = animationSpec),
    label = "PulseScale"
  )
  val pulseAlpha by pulseTransition.animateFloat(
    initialValue = 1f,
    targetValue =  0f,
    animationSpec = infiniteRepeatable(animation = animationSpec),
    label = "PulseAlpha"
  )
  return this.drawBehind {
    val outline = shape.createOutline(size, layoutDirection, this)
    scale(pulseScale) {
      drawOutline(outline, brush, pulseAlpha)
    }
  }
}