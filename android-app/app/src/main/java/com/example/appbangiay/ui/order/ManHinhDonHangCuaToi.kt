package com.example.appbangiay.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appbangiay.model.DonHangCuaToi
import com.example.appbangiay.network.KetNoiServer
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.foundation.BorderStroke

@Composable
fun ManHinhDonHangCuaToi(
    trangThaiMacDinh: String = "pending",
    onOpenOrderDetail: (Int) -> Unit,
    quayLai: () -> Unit
) {
    val tabs = listOf(
        "pending" to "Chờ xử lý",
        "processing" to "Đang xử lý",
        "shipped" to "Đang giao",
        "completed" to "Hoàn thành",
        "cancelled" to "Hoàn / Hủy"
    )

    var tabDangChon by remember { mutableStateOf(trangThaiMacDinh) }
    var danhSachDonHang by remember { mutableStateOf<List<DonHangCuaToi>>(emptyList()) }
    var dangTai by remember { mutableStateOf(false) }

    LaunchedEffect(tabDangChon) {
        dangTai = true

        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            danhSachDonHang = KetNoiServer.api.layDonHangCuaToi(
                firebaseUid = uid,
                status = tabDangChon
            )
        } catch (e: Exception) {
            danhSachDonHang = emptyList()
            e.printStackTrace()
        } finally {
            dangTai = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(76.dp)
                .background(Color.White)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = quayLai) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = "Đơn hàng của tôi",
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.width(48.dp))
        }

        ScrollableTabRow(
            selectedTabIndex = tabs.indexOfFirst { it.first == tabDangChon },
            containerColor = Color.White,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                val index = tabs.indexOfFirst { it.first == tabDangChon }
                if (index >= 0) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                        color = Color(0xFF064C8C),
                        height = 4.dp
                    )
                }
            }
        ) {
            tabs.forEach { tab ->
                Tab(
                    selected = tabDangChon == tab.first,
                    onClick = {
                        tabDangChon = tab.first
                    },
                    text = {
                        Text(
                            text = tab.second,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (tabDangChon == tab.first)
                                Color(0xFF064C8C)
                            else
                                Color.Gray
                        )
                    }
                )
            }
        }

        if (dangTai) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (danhSachDonHang.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = Color(0xFFE0E0E0),
                        modifier = Modifier.size(72.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Chưa có đơn hàng nào",
                        color = Color.Gray,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(danhSachDonHang) { donHang ->
                    DonHangItem(
                        donHang = donHang,
                        onClick = {
                            onOpenOrderDetail(donHang.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DonHangItem(
    donHang: DonHangCuaToi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            1.dp,
            Color(0xFFE0E0E0)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Đơn hàng #${donHang.id}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = hienThiTrangThai(donHang.status),
                    color = Color(0xFF064C8C),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = donHang.tenSanPhamHienThi,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = formatDateOrder(donHang.createdAt),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tổng tiền: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "${formatMoneyOrder(donHang.total)}đ",
                    color = Color(0xFFE53935),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

fun hienThiTrangThai(status: String): String {
    return when (status) {
        "pending" -> "Chờ xử lý"
        "processing" -> "Đang xử lý"
        "shipped" -> "Đang giao"
        "completed" -> "Hoàn thành"
        "cancelled" -> "Đã hủy"
        else -> status
    }
}

fun formatMoneyOrder(value: Float): String {
    return "%,.0f".format(value).replace(",", ".")
}

fun formatDateOrder(value: String?): String {
    if (value.isNullOrBlank()) return ""
    return try {
        value.take(10).split("-").reversed().joinToString("/")
    } catch (e: Exception) {
        value.take(10)
    }
}