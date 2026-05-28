package com.example.appbangiay.ui.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appbangiay.model.ThongBao
import com.example.appbangiay.network.KetNoiServer
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import androidx.compose.foundation.BorderStroke

@Composable
fun ManHinhThongBao(
    onOpenOrderDetail: (Int) -> Unit = {}
) {
    var tabDangChon by remember { mutableStateOf<String?>(null) }
    var danhSachThongBao by remember { mutableStateOf<List<ThongBao>>(emptyList()) }
    var dangTai by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    fun taiThongBao() {
        scope.launch {
            try {
                dangTai = true

                danhSachThongBao = KetNoiServer.api.layThongBao(
                    firebaseUid = uid,
                    type = tabDangChon
                )

            } catch (e: Exception) {
                e.printStackTrace()
                danhSachThongBao = emptyList()
            } finally {
                dangTai = false
            }
        }
    }

    LaunchedEffect(tabDangChon) {
        taiThongBao()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Thông báo",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
        }

        ScrollableTabRow(
            selectedTabIndex = when (tabDangChon) {
                null -> 0
                "order" -> 1
                "promotion" -> 2
                "system" -> 3
                else -> 0
            },
            containerColor = Color.White,
            edgePadding = 0.dp
        ) {
            Tab(
                selected = tabDangChon == null,
                onClick = { tabDangChon = null },
                text = { Text("Tất cả", fontWeight = FontWeight.Bold) }
            )

            Tab(
                selected = tabDangChon == "order",
                onClick = { tabDangChon = "order" },
                text = { Text("Đơn hàng", fontWeight = FontWeight.Bold) }
            )

            Tab(
                selected = tabDangChon == "promotion",
                onClick = { tabDangChon = "promotion" },
                text = { Text("Khuyến mãi", fontWeight = FontWeight.Bold) }
            )

            Tab(
                selected = tabDangChon == "system",
                onClick = { tabDangChon = "system" },
                text = { Text("Hệ thống", fontWeight = FontWeight.Bold) }
            )
        }

        if (dangTai) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (danhSachThongBao.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Chưa có thông báo nào",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(danhSachThongBao) { thongBao ->
                    ThongBaoItem(
                        thongBao = thongBao,
                        onClick = {
                            scope.launch {
                                try {
                                    KetNoiServer.api.danhDauDaDocThongBao(thongBao.id)
                                    danhSachThongBao = danhSachThongBao.map {
                                        if (it.id == thongBao.id) it.copy(isRead = true) else it
                                    }

                                    if (thongBao.type == "order" && thongBao.relatedOrderId != null) {
                                        onOpenOrderDetail(thongBao.relatedOrderId)
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ThongBaoItem(
    thongBao: ThongBao,
    onClick: () -> Unit
) {
    val icon = when (thongBao.type) {
        "order" -> Icons.Default.Inventory2
        "promotion" -> Icons.Default.Campaign
        else -> Icons.Default.Notifications
    }

    val iconColor = when (thongBao.type) {
        "order" -> Color(0xFF064C8C)
        "promotion" -> Color(0xFFE53935)
        else -> Color(0xFF4CAF50)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (thongBao.isRead) Color.White else Color(0xFFEAF3FF)
        ),
        border = BorderStroke(
            1.dp,
            Color(0xFFE0E0E0)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )

    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = thongBao.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )

                    if (!thongBao.isRead) {
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE53935))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = thongBao.message,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF555555),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatNgayThongBao(thongBao.createdAt),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun formatNgayThongBao(value: String?): String {
    if (value.isNullOrBlank()) return ""
    return value.replace("T", " ").take(16)
}