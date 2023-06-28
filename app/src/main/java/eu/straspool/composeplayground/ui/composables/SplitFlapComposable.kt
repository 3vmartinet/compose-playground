package eu.straspool.composeplayground.ui.composables

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp

// https://medium.com/bestsecret-tech/creating-a-split-flap-display-in-compose-7f3f924a8fe6
@Composable
fun SplitFlapComposable(
    modifier: Modifier = Modifier,
    character: Char,
    color: Color = Color.White,
) {
    var pastChar by remember { mutableStateOf(' ') }
    var finalChar by remember { mutableStateOf(character) }
    var indicatorRotation by remember { mutableStateOf(0f) }
    val animationDuration = 500
    val animationSpec =
        tween<Float>(animationDuration, easing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.8f))

    LaunchedEffect(character) {
        finalChar = character

        animate(
            initialValue = 0f,
            targetValue = -180f,
            animationSpec = animationSpec
        ) { value, _ ->
            indicatorRotation = value
            if (value <= -175f) {
                pastChar = character
            }
        }
    }

    Box(
        modifier
            .background(Color(0xFF222222))
    ) {
        SplitFlapPiece(displayText = finalChar, color = Color.White, modifier = Modifier
            .graphicsLayer {
                clip = true
                shape = object : Shape {
                    override fun createOutline(
                        size: Size,
                        layoutDirection: LayoutDirection,
                        density: Density,
                    ): Outline {
                        return Outline.Rectangle(
                            size
                                .copy(height = size.height / 2 - 2, width = size.width)
                                .toRect()
                        )
                    }
                }
            }
            .background(Color.DarkGray)
            .border(BorderStroke(Dp.Hairline, Color(0xFF222222)))
        )

        SplitFlapPiece(displayText = pastChar, color = Color.White, modifier = Modifier
            .graphicsLayer {
                clip = true
                rotationX = -180f
                rotationY = 180f
                rotationZ = 180f
                shape = object : Shape {
                    override fun createOutline(
                        size: Size,
                        layoutDirection: LayoutDirection,
                        density: Density,
                    ): Outline {
                        return Outline.Rectangle(
                            size
                                .copy(height = size.height / 2, width = size.width)
                                .toRect()
                                .translate(0f, size.height / 2)
                        )
                    }
                }
            }
            .background(Color.DarkGray)
            .border(BorderStroke(Dp.Hairline, Color(0xFF222222)))
        )

        if (indicatorRotation >= -90f) {
            SplitFlapPiece(displayText = pastChar, color = color, modifier = Modifier
                .graphicsLayer {
                    rotationX = indicatorRotation
                    clip = true
                    shape = object : Shape {
                        override fun createOutline(
                            size: Size,
                            layoutDirection: LayoutDirection,
                            density: Density,
                        ): Outline {
                            return Outline.Rectangle(
                                size
                                    .copy(height = size.height / 2 - 2, width = size.width)
                                    .toRect()
                            )
                        }
                    }
                }
                .background(Color.DarkGray)
                .border(BorderStroke(Dp.Hairline, Color(0xFF222222)))
            )
        } else {
            SplitFlapPiece(displayText = finalChar, color = color, modifier = Modifier
                .graphicsLayer {
                    clip = true
                    rotationX = indicatorRotation
                    rotationY = 180f
                    rotationZ = 180f
                    shape = object : Shape {
                        override fun createOutline(
                            size: Size,
                            layoutDirection: LayoutDirection,
                            density: Density,
                        ): Outline {
                            return Outline.Rectangle(
                                size
                                    .copy(height = size.height / 2, width = size.width)
                                    .toRect()
                                    .translate(0f, size.height / 2)
                            )
                        }
                    }
                }
                .background(Color.DarkGray)
                .border(BorderStroke(Dp.Hairline, Color(0xFF222222)))
            )
        }
    }
}

@Composable
private fun SplitFlapPiece(displayText: Char, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .then(modifier)
    ) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {}
        Text(
            displayText.toString(),
            color = color,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 190.sp
        )
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {}
    }
}

@Preview
@Composable
fun Preview() {
    SplitFlapComposable(character = '1', color = Color.Black)
}
