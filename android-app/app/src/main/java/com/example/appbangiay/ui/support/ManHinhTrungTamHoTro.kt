package com.example.appbangiay.ui.support

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun ManHinhTrungTamHoTro(
    quayLai: () -> Unit
) {
    var cau1 by remember { mutableStateOf(false) }
    var cau2 by remember { mutableStateOf(false) }
    var cau3 by remember { mutableStateOf(false) }
    var cau4 by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7FC))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(76.dp)
                .background(Color.White)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = quayLai) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.Black
                )
            }

            Text(
                text = "Trung tâm hỗ trợ",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(48.dp))
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Liên hệ trực tiếp",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactCard(
                icon = Icons.Default.Call,
                iconColor = Color(0xFF4CAF50),
                iconBg = Color(0xFFE9F8EC),
                title = "Hotline",
                subtitle = "0973711868 (8:00 - 21:00)"
            )

            Spacer(modifier = Modifier.height(14.dp))

            ContactCard(
                icon = Icons.Default.Email,
                iconColor = Color(0xFF2196F3),
                iconBg = Color(0xFFEAF6FF),
                title = "Email hỗ trợ",
                subtitle = "support@hoangshoe.vn"
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Câu hỏi thường gặp",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            FaqItem(
                question = "Làm thế nào để đổi trả hàng?",
                answer = "Bạn có thể đổi hàng trong vòng 30 ngày kể từ ngày nhận hàng. Sản phẩm phải còn nguyên tem mác.",
                expanded = cau1,
                onClick = { cau1 = !cau1 }
            )

            FaqItem(
                question = "Phí vận chuyển được tính thế nào?",
                answer = "Miễn phí vận chuyển cho đơn hàng từ 500.000đ. Các đơn hàng khác phí ship đồng giá 25.000đ.",
                expanded = cau2,
                onClick = { cau2 = !cau2 }
            )

            FaqItem(
                question = "Tôi có thể kiểm tra hàng trước không?",
                answer = "HoangShoes hỗ trợ đồng kiểm. Bạn có thể mở hộp kiểm tra sản phẩm trước khi thanh toán.",
                expanded = cau3,
                onClick = { cau3 = !cau3 }
            )

            FaqItem(
                question = "Bảo hành sản phẩm bao lâu?",
                answer = "Tất cả sản phẩm giày được bảo hành keo chỉ trong vòng 12 tháng.",
                expanded = cau4,
                onClick = { cau4 = !cau4 }
            )
        }
    }
}

@Composable
fun ContactCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    iconBg: Color,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFE5E5E5)
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(92.dp)
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.size(58.dp),
                shape = RoundedCornerShape(50),
                border = BorderStroke(
                    1.dp,
                    Color(0xFFE5E5E5)
                ),
                colors = CardDefaults.cardColors(containerColor = iconBg)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun FaqItem(
    question: String,
    answer: String,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFE5E5E5)
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (expanded) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = null,
                    tint = MauXanhChinh
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = answer,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    lineHeight = 24.sp
                )
            }
        }
    }
}