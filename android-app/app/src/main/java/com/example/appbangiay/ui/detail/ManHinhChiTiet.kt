package com.example.appbangiay.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appbangiay.viewmodel.ChiTietGiayViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhChiTiet(
    maGiay: Int,
    quayLai: () -> Unit,
    chuyenSangGioHang: () -> Unit,
    viewModel: ChiTietGiayViewModel = viewModel()
) {
    val giay by viewModel.giayChiTiet.collectAsState()
    val dangTai by viewModel.trangThaiTai.collectAsState()

    // Gọi API ngay khi màn hình vừa được mở lên
    LaunchedEffect(maGiay) {
        viewModel.layThongTinGiay(maGiay)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết sản phẩm") },
                navigationIcon = {
                    IconButton(onClick = { quayLai() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        // Khi giay không bị null thì tiến hành lưu vào Room
                        giay?.let {
                            viewModel.themVaoGioHang(it)
                            chuyenSangGioHang() // Chuyển trang sau khi thêm
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    enabled = giay != null
                ) {
                    Text("Thêm vào giỏ hàng")
                }
            }
        }
    ) { paddingValues ->
        if (dangTai) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (giay != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Hình ảnh sản phẩm
                AsyncImage(
                    model = giay!!.hinhAnh ?: "https://via.placeholder.com/400",
                    contentDescription = giay!!.tenGiay,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(300.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Thông tin chi tiết
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = giay!!.tenGiay,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${giay!!.giaTien} VNĐ",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Mô tả sản phẩm",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = giay!!.moTa ?: "Đang cập nhật mô tả...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}