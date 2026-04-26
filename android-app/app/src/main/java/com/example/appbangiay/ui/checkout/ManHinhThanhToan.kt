package com.example.appbangiay.ui.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appbangiay.viewmodel.ThanhToanViewModel

@Composable
fun ManHinhThanhToan(viewModel: ThanhToanViewModel, quayVeTrangChu: () -> Unit) {
    var diaChi by remember { mutableStateOf("") }

    val danhSach by viewModel.danhSachGioHang.collectAsState(initial = emptyList())
    val trangThai by viewModel.trangThaiDatHang.collectAsState()

    // Lắng nghe trạng thái thành công để quay về trang chủ
    LaunchedEffect(trangThai) {
        if (trangThai == "Thành công") {
            quayVeTrangChu()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Xác nhận Đơn hàng", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị tóm tắt giỏ hàng
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(danhSach) { monHang ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Text("${monHang.tenGiay} (x${monHang.soLuong})", modifier = Modifier.weight(1f))
                    Text("${monHang.giaTien * monHang.soLuong} đ", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        // Tính lại tổng tiền để hiển thị lên UI
        val tongTienUI = danhSach.sumOf { (it.giaTien * it.soLuong).toDouble() }
        Text("Tổng cộng: $tongTienUI đ", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = diaChi,
            onValueChange = { diaChi = it },
            label = { Text("Nhập địa chỉ giao hàng") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Trạng thái: $trangThai", color = MaterialTheme.colorScheme.error)

        Button(
            onClick = { viewModel.thucHienDatHang(maNguoiDung = 1, diaChi = diaChi) }, // Truyền cứng user_id = 1 để test
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = diaChi.isNotBlank() && trangThai != "Đang xử lý..."
        ) {
            Text("CHỐT ĐƠN")
        }
    }
}