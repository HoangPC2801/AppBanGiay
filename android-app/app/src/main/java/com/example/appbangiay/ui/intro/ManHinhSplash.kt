package com.example.appbangiay.ui.intro

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.example.appbangiay.R
import com.example.appbangiay.ui.theme.MauXanhChinh
@Composable
fun SplashScreen(onNavigateNext: () -> Unit) {
    val primaryBlue = MauXanhChinh

    LaunchedEffect(key1 = true) {
        delay(2500)
        onNavigateNext()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .align(Alignment.TopCenter)
        ) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(0f, size.height * 0.6f)

                cubicTo(
                    x1 = size.width * 0.3f, y1 = size.height * 0.6f,
                    x2 = size.width * 0.5f, y2 = size.height * 0.1f,
                    x3 = size.width, y3 = size.height * 0.3f
                )

                lineTo(size.width, 0f)
                close()
            }
            drawPath(path = path, color = primaryBlue)
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .align(Alignment.BottomCenter)
        ) {
            val path = Path().apply {
                moveTo(0f, size.height)
                lineTo(0f, size.height * 0.5f)

                quadraticBezierTo(
                    x1 = size.width * 0.5f, y1 = size.height * 1f,
                    x2 = size.width, y2 = size.height * 0.5f
                )

                lineTo(size.width, size.height)
                close()
            }
            drawPath(path = path, color = primaryBlue)
        }

        Image(
            painter = painterResource(id = R.drawable.logo_hoangshoe),
            contentDescription = "Hoang Shoe Logo",
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(180.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(onNavigateNext = {})
}