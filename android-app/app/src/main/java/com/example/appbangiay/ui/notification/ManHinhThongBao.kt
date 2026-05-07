package com.example.appbangiay.ui.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NotificationItem(
    val title: String,
    val content: String,
    val time: String
)

@Composable
fun ManHinhThongBao() {
    val primaryBlue = Color(0xFF67A8F8)
    val backgroundGray = Color(0xFFEFEFEF)

    val notifications = listOf(
        NotificationItem(
            title = "Trải nghiệm mua sắm với\nvô vàn ưu đãi",
            content = "Cảm ơn bạn đã tham gia. Hãy khám phá ngay\nthế giới giày chính hãng với vô vàn ưu đãi!",
            time = "05/05 09:44"
        ),
        NotificationItem(
            title = "Bạn quên gì đó trong giỏ\nhàng kìa!",
            content = "Cảm ơn bạn đã tham gia. Hãy khám phá ngay\nthế giới giày chính hãng với vô vàn ưu đãi!",
            time = "04/05 18:32"
        ),
        NotificationItem(
            title = "Chào mừng đến với\nHoangShoe!",
            content = "Cảm ơn bạn đã tham gia. Hãy khám phá ngay\nthế giới giày chính hãng với vô vàn ưu đãi!",
            time = "03/05 09:30"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGray)
    ) {

        // HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(primaryBlue),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "Thông báo",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        HorizontalDivider(
            thickness = 2.dp,
            color = Color(0xFFD8D8D8)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(notifications) { item ->
                NotificationRow(item)
                HorizontalDivider(
                    thickness = 2.dp,
                    color = Color(0xFFD8D8D8)
                )
            }
        }
    }
}

@Composable
private fun NotificationRow(item: NotificationItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF4F4F4))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Outlined.NotificationsNone,
            contentDescription = null,
            tint = Color(0xFF5E9CF5),
            modifier = Modifier.size(34.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.content,
                fontSize = 12.sp,
                color = Color.Black,
                lineHeight = 15.sp
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = item.time,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}