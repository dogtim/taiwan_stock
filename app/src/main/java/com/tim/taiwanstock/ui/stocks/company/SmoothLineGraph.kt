package com.tim.taiwanstock.ui.stocks.company

import android.graphics.PointF
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalTextApi::class)
@Composable
fun SmoothLineGraph(graphData: List<IntradayInfo>) {
    Box(
        modifier = Modifier
            .background(PurpleBackgroundColor)
            .fillMaxSize()
    ) {
        val animationProgress = remember {
            Animatable(0f)
        }

        LaunchedEffect(key1 = graphData, block = {
            animationProgress.animateTo(1f, tween(3000))
        })
        val coroutineScope = rememberCoroutineScope()
        val textMeasurer = rememberTextMeasurer()
        val paddingDp = 16.dp

        // Draw the price and lines
        Spacer(
            modifier = Modifier
                .padding(paddingDp)
                .aspectRatio(3 / 2f)
                .fillMaxSize()
                .align(Alignment.Center)
                .clickable {
                    coroutineScope.launch {
                        animationProgress.snapTo(0f)
                        animationProgress.animateTo(1f, tween(3000))
                    }
                }
                .drawWithCache {
                    val path = generateSmoothPath(graphData, size)
                    val filledPath = Path()
                    filledPath.addPath(path)
                    filledPath.relativeLineTo(0f, size.height)
                    filledPath.lineTo(0f, size.height)
                    filledPath.close()

                    onDrawBehind {
                        val barWidthPx = 1.dp.toPx()
                        drawRect(BarColor, style = Stroke(barWidthPx))

                        val verticalLines = 4
                        val verticalSize = size.width / (verticalLines + 1)
                        repeat(verticalLines) { i ->
                            val startX = verticalSize * (i + 1)
                            drawLine(
                                BarColor,
                                start = Offset(startX, 0f),
                                end = Offset(startX, size.height),
                                strokeWidth = barWidthPx
                            )
                        }
                        val horizontalLines = 3
                        val sectionSize = size.height / (horizontalLines + 1)
                        repeat(horizontalLines) { i ->
                            val startY = sectionSize * (i + 1)
                            drawLine(
                                BarColor,
                                start = Offset(0f, startY),
                                end = Offset(size.width, startY),
                                strokeWidth = barWidthPx
                            )
                        }

                        // draw line
                        clipRect(right = size.width * animationProgress.value) {
                            drawPath(path, Color.Green, style = Stroke(2.dp.toPx()))

                            drawPath(
                                filledPath,
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color.Green.copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                ),
                                style = Fill
                            )
                        }
                    }
                })

        // Draw the dates
        Spacer(
            modifier = Modifier
                .aspectRatio(3 / 2f)
                .fillMaxSize()
                .align(Alignment.Center)
                .drawWithCache {
                    onDrawBehind {
                        val textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 10.sp
                        )
                        val numberEntries = graphData.size - 1
                        val dayWidth = size.width / numberEntries
                        graphData.forEachIndexed { i, balance ->
                            if (i == graphData.size - 1) {
                                return@forEachIndexed
                            }
                            val dayOfMonth = balance.date.dayOfMonth
                            val month = balance.date.monthValue

                            val date = "$dayOfMonth / $month"

                            val balanceX = i * dayWidth + paddingDp.toPx() // - (textMeasurer.measure(date).size.width / 2)

                            drawText(
                                textMeasurer,
                                date,
                                Offset(x = balanceX, y = size.height - paddingDp.toPx()),
                                style = textStyle
                            )
                        }

                    }
                })
    }
}

fun generateSmoothPath(data: List<IntradayInfo>, size: Size): Path {
    val path = Path()
    val numberEntries = data.size - 1
    val dayWidth = size.width / numberEntries

    val max = data.maxBy { it.close }
    val min = data.minBy { it.close } // will map to x= 0, y = height
    val range = max.close - min.close
    val heightPxPerAmount = size.height / range.toFloat()

    var previousBalanceX = 0f
    var previousBalanceY = size.height
    data.forEachIndexed { i, balance ->
        if (i == 0) {
            path.moveTo(
                0f,
                size.height - (balance.close - min.close).toFloat() *
                        heightPxPerAmount
            )

        }

        val balanceX = i * dayWidth
        val balanceY = size.height - (balance.close - min.close).toFloat() *
                heightPxPerAmount
        // to do smooth curve graph - we use cubicTo, uncomment section below for non-curve
        val controlPoint1 = PointF((balanceX + previousBalanceX) / 2f, previousBalanceY)
        val controlPoint2 = PointF((balanceX + previousBalanceX) / 2f, balanceY)
//        path.cubicTo(
//            controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y,
//            balanceX, balanceY
//        )
        path.lineTo(balanceX, balanceY)
        previousBalanceX = balanceX
        previousBalanceY = balanceY
    }
    return path
}

val PurpleBackgroundColor = Color(0xff322049)
val BarColor = Color.White.copy(alpha = 0.3f)