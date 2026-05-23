package com.example.appbangiay.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appbangiay.ui.theme.MauXanhChinh
import androidx.compose.foundation.BorderStroke

@Composable
fun ManHinhVeHoangShoe(
    quayLai: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F7FB))
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MauXanhChinh)
                .statusBarsPadding()
        ) {
            IconButton(
                onClick = quayLai,
                modifier = Modifier
                    .padding(start = 18.dp, top = 16.dp)
                    .align(Alignment.TopStart)
                    .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(50))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.White
                )
            }

            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(
                    1.dp,
                    Color(0xFFE5E5E5)
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.12f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.size(90.dp),
                        shape = RoundedCornerShape(22.dp),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFE5E5E5)
                        ),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                tint = MauXanhChinh,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(18.dp))

                    Column {
                        Text(
                            text = "HOANGSHOES",
                            color = Color.White,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Nền tảng mua sắm giày chính hãng uy tín hàng đầu Việt Nam.",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 26.sp
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = MauXanhChinh
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Cam kết của HoangShoes",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF10233F)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Uy tín không đến từ lời nói, mà đến từ trải nghiệm thật của khách hàng qua từng đơn hàng.",
                fontSize = 18.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CamKetCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Shield,
                    title = "100%",
                    subtitle = "Chính hãng"
                )

                CamKetCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Star,
                    title = "Uy tín",
                    subtitle = "Được tin chọn"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CamKetCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.SwapHoriz,
                    title = "Linh hoạt",
                    subtitle = "Hỗ trợ đổi hàng"
                )

                CamKetCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.FlashOn,
                    title = "Nhanh",
                    subtitle = "Xử lý đơn hàng"
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(
                    1.dp,
                    Color(0xFFE5E5E5)
                ),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier.size(76.dp),
                        shape = RoundedCornerShape(22.dp),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFE5E5E5)
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFEAF3FF)
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = MauXanhChinh,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = "HoangShoes trở thành lựa chọn hàng đầu của khách hàng khi tìm kiếm giày chính hãng tại Việt Nam.",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10233F),
                        textAlign = TextAlign.Center,
                        lineHeight = 34.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Sự tin tưởng của khách hàng là nền tảng lớn nhất để HoangShoes không ngừng hoàn thiện sản phẩm, dịch vụ và công nghệ. Mỗi bước phát triển đều hướng đến một mục tiêu rõ ràng: mang đến trải nghiệm mua sắm tốt hơn, an tâm hơn và xứng đáng hơn cho khách hàng.",
                        fontSize = 17.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 30.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CamKetCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = modifier.height(145.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFE5E5E5)
        ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MauXanhChinh,
                modifier = Modifier.size(34.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF10233F)
            )

            Text(
                text = subtitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}