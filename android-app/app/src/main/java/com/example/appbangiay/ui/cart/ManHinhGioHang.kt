package com.example.appbangiay.ui.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appbangiay.database.GioHangDao
import com.example.appbangiay.model.GioHang

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhGioHang(
    dao: GioHangDao,
    quayLai: () -> Unit,
    chuyenSangThanhToan: () -> Unit
) {
    // Lấy luồng dữ liệu tự động cập nhật từ Room DB
    val danhSachGioHang by dao.layDanhSachGioHang().collectAsState(initial = emptyList())

    // Tính tổng tiền
    val tongTien = danhSachGioHang.sumOf { (it.giaTien * it.soLuong).toDouble() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Giỏ hàng của bạn") },
                navigationIcon = {
                    IconButton(onClick = quayLai) { Icon(Icons.Default.ArrowBack, "Quay lại") }
                }
            )
        },
        bottomBar = {
            if (danhSachGioHang.isNotEmpty()) {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tổng: $tongTien đ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Button(onClick = chuyenSangThanhToan) {
                            Text("Thanh toán")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (danhSachGioHang.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Giỏ hàng đang trống", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                items(danhSachGioHang) { monHang ->
                    ItemGioHang(monHang)
                }
            }
        }
    }
}

@Composable
fun ItemGioHang(monHang: GioHang) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = monHang.hinhAnh ?: "https://via.placeholder.com/150",
                contentDescription = monHang.tenGiay,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = monHang.tenGiay, style = MaterialTheme.typography.titleMedium)
                Text(text = "${monHang.giaTien} đ", color = MaterialTheme.colorScheme.primary)
                Text(text = "Số lượng: ${monHang.soLuong}")
            }
        }
    }
}