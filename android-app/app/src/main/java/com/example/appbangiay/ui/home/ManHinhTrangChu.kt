package com.example.appbangiay.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appbangiay.model.Giay
import com.example.appbangiay.viewmodel.TrangChuViewModel

@Composable
fun ManHinhTrangChu(
    viewModel: TrangChuViewModel = viewModel(),
    chuyenSangChiTiet: (Int) -> Unit // Hàm callback để chuyển màn hình
) {
    val danhSach by viewModel.danhSachGiay.collectAsState()
    val dangTai by viewModel.trangThaiTai.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Banner hiển thị
        Card(modifier = Modifier.fillMaxWidth().height(150.dp)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Banner Khuyến Mãi BIZFLOW", style = MaterialTheme.typography.titleLarge)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Danh sách giày", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (dangTai) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(danhSach) { giay ->
                    ItemGiay(giay = giay, onClick = { chuyenSangChiTiet(giay.maGiay) })
                }
            }
        }
    }
}

@Composable
fun ItemGiay(giay: Giay, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Load hình bằng thư viện Coil
            AsyncImage(
                model = giay.hinhAnh ?: "https://via.placeholder.com/150",
                contentDescription = giay.tenGiay,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = giay.tenGiay, style = MaterialTheme.typography.bodyMedium)
            Text(text = "${giay.giaTien} VNĐ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
        }
    }
}