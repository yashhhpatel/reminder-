package com.remindly.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.remindly.app.ui.theme.AppTheme
import com.remindly.app.ui.theme.BrandPurple
import com.remindly.app.ui.theme.BrandPurpleLight

/**
 * Home empty-state illustration: a person relaxing with a laptop, a plant, and a wall clock —
 * replaces the old generic bell-in-circle icon to match the reference app's welcome screen.
 * Hand-drawn with Canvas rather than a traced vector asset, so it stays a simple, reliably
 * renderable shape composition instead of a large hand-authored path-data blob.
 */
@Composable
fun WelcomeIllustration(modifier: Modifier = Modifier) {
    val outline = AppTheme.extendedColors.textPrimary
    val accent = BrandPurple
    val accentLight = BrandPurpleLight
    val glow = BrandPurpleLight.copy(alpha = 0.18f)

    Box(modifier = modifier.width(220.dp).height(180.dp)) {
        Canvas(modifier = Modifier.width(220.dp).height(180.dp)) {
            val w = size.width
            val h = size.height
            val stroke = Stroke(width = w * 0.014f, cap = StrokeCap.Round, join = StrokeJoin.Round)

            // Wall clock, upper-right, with a soft glow behind it.
            val clockCenter = Offset(w * 0.78f, h * 0.24f)
            val clockRadius = w * 0.11f
            drawCircle(color = glow, radius = clockRadius * 1.8f, center = clockCenter)
            drawCircle(color = outline, radius = clockRadius, center = clockCenter, style = stroke)
            drawLine(outline, clockCenter, clockCenter + Offset(0f, -clockRadius * 0.55f), stroke.width, StrokeCap.Round)
            drawLine(outline, clockCenter, clockCenter + Offset(clockRadius * 0.4f, clockRadius * 0.2f), stroke.width, StrokeCap.Round)

            // Plant, lower-left: pot + stem + two leaves.
            val potLeft = Offset(w * 0.10f, h * 0.86f)
            val potRight = Offset(w * 0.26f, h * 0.86f)
            val potTopLeft = Offset(w * 0.13f, h * 0.72f)
            val potTopRight = Offset(w * 0.23f, h * 0.72f)
            drawPath(
                path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(potLeft.x, potLeft.y)
                    lineTo(potTopLeft.x, potTopLeft.y)
                    lineTo(potTopRight.x, potTopRight.y)
                    lineTo(potRight.x, potRight.y)
                    close()
                },
                color = accent,
            )
            val stemBase = Offset(w * 0.18f, h * 0.72f)
            drawLine(accent, stemBase, stemBase + Offset(0f, -h * 0.16f), stroke.width * 0.8f, StrokeCap.Round)
            drawLeaf(stemBase + Offset(0f, -h * 0.10f), Offset(-w * 0.09f, -h * 0.10f), accentLight)
            drawLeaf(stemBase + Offset(0f, -h * 0.15f), Offset(w * 0.08f, -h * 0.08f), accentLight)

            // Couch: seat base + two armrests.
            val seatTop = h * 0.80f
            drawRoundRect(
                color = accentLight.copy(alpha = 0.35f),
                topLeft = Offset(w * 0.20f, seatTop),
                size = Size(w * 0.62f, h * 0.10f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.03f),
            )
            drawRoundRect(
                color = accentLight.copy(alpha = 0.35f),
                topLeft = Offset(w * 0.16f, h * 0.62f),
                size = Size(w * 0.08f, h * 0.28f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.03f),
            )
            drawRoundRect(
                color = accentLight.copy(alpha = 0.35f),
                topLeft = Offset(w * 0.76f, h * 0.62f),
                size = Size(w * 0.08f, h * 0.28f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.03f),
            )

            // Person: head, body, arms, legs (simple outline figure).
            val headCenter = Offset(w * 0.47f, h * 0.34f)
            val headRadius = w * 0.075f
            drawCircle(color = outline, radius = headRadius, center = headCenter, style = stroke)
            // Hair.
            drawArc(
                color = outline,
                startAngle = 190f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(headCenter.x - headRadius, headCenter.y - headRadius * 1.1f),
                size = Size(headRadius * 2, headRadius * 2),
                style = stroke,
            )

            val shoulder = headCenter + Offset(0f, headRadius * 1.1f)
            val hip = Offset(w * 0.50f, h * 0.78f)
            drawLine(outline, shoulder, hip, stroke.width * 1.3f, StrokeCap.Round)

            // Legs, bent toward the laptop.
            val kneeL = Offset(w * 0.40f, h * 0.80f)
            val footL = Offset(w * 0.36f, h * 0.92f)
            drawLine(outline, hip, kneeL, stroke.width, StrokeCap.Round)
            drawLine(outline, kneeL, footL, stroke.width, StrokeCap.Round)
            val kneeR = Offset(w * 0.60f, h * 0.80f)
            val footR = Offset(w * 0.63f, h * 0.92f)
            drawLine(outline, hip, kneeR, stroke.width, StrokeCap.Round)
            drawLine(outline, kneeR, footR, stroke.width, StrokeCap.Round)

            // Arms reaching toward the laptop.
            val handL = Offset(w * 0.40f, h * 0.66f)
            val handR = Offset(w * 0.58f, h * 0.66f)
            drawLine(outline, shoulder + Offset(-w * 0.02f, h * 0.02f), handL, stroke.width, StrokeCap.Round)
            drawLine(outline, shoulder + Offset(w * 0.02f, h * 0.02f), handR, stroke.width, StrokeCap.Round)

            // Laptop on the lap.
            drawRoundRect(
                color = accent,
                topLeft = Offset(w * 0.37f, h * 0.63f),
                size = Size(w * 0.24f, h * 0.11f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.015f),
            )
            drawCircle(color = Color.White, radius = w * 0.008f, center = Offset(w * 0.49f, h * 0.685f))

            // A couple of "activity" sparkle lines near the laptop, echoing the reference art.
            drawLine(accentLight, Offset(w * 0.70f, h * 0.55f), Offset(w * 0.75f, h * 0.50f), stroke.width * 0.6f, StrokeCap.Round)
            drawLine(accentLight, Offset(w * 0.74f, h * 0.60f), Offset(w * 0.80f, h * 0.58f), stroke.width * 0.6f, StrokeCap.Round)

            // Ground shadow line under the couch.
            drawLine(
                color = outline.copy(alpha = 0.25f),
                start = Offset(w * 0.14f, h * 0.965f),
                end = Offset(w * 0.86f, h * 0.965f),
                strokeWidth = stroke.width * 0.7f,
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(w * 0.02f, w * 0.015f)),
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawLeaf(base: Offset, tip: Offset, color: Color) {
    val control = base + Offset(tip.x * 0.5f, tip.y * 1.4f)
    val path = androidx.compose.ui.graphics.Path().apply {
        moveTo(base.x, base.y)
        quadraticBezierTo(control.x, control.y, base.x + tip.x, base.y + tip.y)
        quadraticBezierTo(base.x, base.y - tip.y * 0.15f, base.x, base.y)
        close()
    }
    drawPath(path, color)
}
