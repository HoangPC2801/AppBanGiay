package com.example.appbangiay.ui.intro

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.appbangiay.R
import com.example.appbangiay.ui.theme.MauXanhChinh

data class IntroPage(
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String
)

val introPages = listOf(
    IntroPage(
        imageRes = R.drawable.logo_nike,
        title = "Just Do It - Chinh phục mọi giới hạn",
        description = "Vượt qua mọi thách thức và đạt được mục tiêu của bạn với những đôi giày Nike được thiết kế cho hiệu suất tối ưu."
    ),
    IntroPage(
        imageRes = R.drawable.logo_adidas,
        title = "Khám phá phong cách của bạn",
        description = "Tìm kiếm đôi giày thể thao hoàn hảo cho mọi hoạt động, từ tập luyện đến dạo phố. Adidas đồng hành cùng bạn trên mọi nẻo đường."
    ),
    IntroPage(
        imageRes = R.drawable.logo_bitis,
        title = "Biti's - Nâng niu bàn chân Việt",
        description = "Trải nghiệm sự kết hợp hoàn hảo giữa chất lượng bền bỉ và thiết kế hiện đại, tự hào đồng hành cùng người Việt."
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntroScreen(onFinishIntro: () -> Unit) {
    val primaryBlue = MauXanhChinh
    val inactiveGray = Color(0xFFD9D9D9)

    val pagerState = rememberPagerState(pageCount = { introPages.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(220.dp).align(Alignment.TopCenter)) {
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

        Canvas(modifier = Modifier.fillMaxWidth().height(180.dp).align(Alignment.BottomCenter)) {
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

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                val page = introPages[pageIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = page.imageRes),
                        contentDescription = "Intro Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = page.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = page.description,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(introPages.size) { index ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(
                                    if (pagerState.currentPage == index) primaryBlue else inactiveGray
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (pagerState.currentPage == introPages.size - 1) {
                            onFinishIntro()
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (pagerState.currentPage == introPages.size - 1) "Bắt đầu" else "Tiếp Tục",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Bỏ qua",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .clickable { onFinishIntro() }
                        .padding(8.dp)
                )
            }
        }
    }
}