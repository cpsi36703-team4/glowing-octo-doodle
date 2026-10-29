/*
 * Note: Generated and refined with AI assistance.
 */
package com.rainyday.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rainyday.domain.model.ForecastCondition
import kotlin.math.cos
import kotlin.math.sin

val SunColor = Color(0xFFE8A90C)
val CloudColor = Color(0xFF9AA3A8)
val RainColor = Color(0xFF3D8FC4)
val StormColor = Color(0xFFC07A0A)

/**
 * Custom Compose canvas graphic rendering vector weather condition icons for financial forecasts.
 *
 * @param condition The forecast condition to render (CLEAR, FAIR, CLOUDY, RAIN, STORM).
 * @param modifier Optional Compose modifier.
 * @param size Target icon dimension (defaults to 28.dp).
 */
@Composable
fun WeatherIcon(
    condition: ForecastCondition,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f

        when (condition) {
            ForecastCondition.CLEAR -> {
                drawCircle(color = SunColor, radius = w * 0.28f, center = Offset(cx, cy))
                for (i in 0 until 8) {
                    val angle = Math.toRadians(i * 45.0)
                    val startR = w * 0.36f
                    val endR = w * 0.46f
                    val startX = cx + (cos(angle) * startR).toFloat()
                    val startY = cy + (sin(angle) * startR).toFloat()
                    val endX = cx + (cos(angle) * endR).toFloat()
                    val endY = cy + (sin(angle) * endR).toFloat()
                    drawLine(
                        color = SunColor,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = w * 0.08f,
                    )
                }
            }
            ForecastCondition.FAIR -> {
                drawCircle(color = SunColor, radius = w * 0.22f, center = Offset(cx + (w * 0.15f), cy - (h * 0.15f)))
                drawCircle(color = CloudColor, radius = w * 0.22f, center = Offset(cx - (w * 0.1f), cy + (h * 0.05f)))
                drawCircle(color = CloudColor, radius = w * 0.26f, center = Offset(cx + (w * 0.1f), cy))
                drawRoundRect(
                    color = CloudColor,
                    topLeft = Offset(cx - (w * 0.3f), cy + (h * 0.05f)),
                    size = Size(w * 0.6f, h * 0.22f),
                    cornerRadius = CornerRadius(h * 0.1f, h * 0.1f),
                )
            }
            ForecastCondition.CLOUDY -> {
                drawCircle(color = CloudColor, radius = w * 0.25f, center = Offset(cx - (w * 0.12f), cy))
                drawCircle(color = CloudColor, radius = w * 0.30f, center = Offset(cx + (w * 0.1f), cy - (h * 0.05f)))
                drawRoundRect(
                    color = CloudColor,
                    topLeft = Offset(cx - (w * 0.35f), cy + (h * 0.05f)),
                    size = Size(w * 0.7f, h * 0.25f),
                    cornerRadius = CornerRadius(h * 0.12f, h * 0.12f),
                )
            }
            ForecastCondition.RAIN -> {
                drawCircle(color = CloudColor, radius = w * 0.22f, center = Offset(cx - (w * 0.12f), cy - (h * 0.1f)))
                drawCircle(color = CloudColor, radius = w * 0.26f, center = Offset(cx + (w * 0.1f), cy - (h * 0.14f)))
                drawRoundRect(
                    color = CloudColor,
                    topLeft = Offset(cx - (w * 0.32f), cy - (h * 0.05f)),
                    size = Size(w * 0.64f, h * 0.22f),
                    cornerRadius = CornerRadius(h * 0.1f, h * 0.1f),
                )
                for (dx in listOf(-0.18f, 0.0f, 0.18f)) {
                    drawLine(
                        color = RainColor,
                        start = Offset(cx + (w * dx), cy + (h * 0.22f)),
                        end = Offset(cx + (w * (dx - 0.05f)), cy + (h * 0.38f)),
                        strokeWidth = w * 0.07f,
                    )
                }
            }
            ForecastCondition.STORM -> {
                val stormCloudColor = Color(0xFF6C7A82)
                drawCircle(color = stormCloudColor, radius = w * 0.22f, center = Offset(cx - (w * 0.12f), cy - (h * 0.12f)))
                drawCircle(color = stormCloudColor, radius = w * 0.26f, center = Offset(cx + (w * 0.1f), cy - (h * 0.16f)))
                drawRoundRect(
                    color = stormCloudColor,
                    topLeft = Offset(cx - (w * 0.32f), cy - (h * 0.07f)),
                    size = Size(w * 0.64f, h * 0.22f),
                    cornerRadius = CornerRadius(h * 0.1f, h * 0.1f),
                )
                val boltPath = Path().apply {
                    moveTo(cx + (w * 0.02f), cy + (h * 0.12f))
                    lineTo(cx - (w * 0.12f), cy + (h * 0.28f))
                    lineTo(cx, cy + (h * 0.28f))
                    lineTo(cx - (w * 0.06f), cy + (h * 0.44f))
                    lineTo(cx + (w * 0.14f), cy + (h * 0.22f))
                    lineTo(cx + (w * 0.02f), cy + (h * 0.22f))
                    close()
                }
                drawPath(path = boltPath, color = StormColor)
            }
        }
    }
}
