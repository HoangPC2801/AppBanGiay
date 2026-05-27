package com.example.appbangiay.ui.brand

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appbangiay.model.Giay
import com.example.appbangiay.viewmodel.TrangChuViewModel
import com.example.appbangiay.ui.components.ProductCard
import com.example.appbangiay.ui.components.BoLocSanPhamBottomSheet
import com.example.appbangiay.ui.components.BoLocSanPhamState
import com.example.appbangiay.ui.components.locVaSapXepSanPham

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhSanPhamTheoThuongHieu(
    brand: String,
    quayLai: () -> Unit,
    chuyenSangChiTiet: (Int) -> Unit,
    viewModel: TrangChuViewModel = viewModel()
) {
    val danhSachGiay by viewModel.danhSachGiay.collectAsState()
    val dangTai by viewModel.trangThaiTai.collectAsState()

    var hienBoLoc by remember { mutableStateOf(false) }
    var boLoc by remember { mutableStateOf(BoLocSanPhamState()) }

    val danhSachGoc = danhSachGiay.filter {
        it.thuongHieu.equals(brand, ignoreCase = true)
    }

    val danhSachLoc = locVaSapXepSanPham(danhSachGoc, boLoc)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = brand.uppercase(),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = quayLai) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Tìm kiếm"
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable {
                                hienBoLoc = true
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Lọc"
                        )
                        Text("Lọc", fontWeight = FontWeight.Bold)
                    }
                }
            )
            if (hienBoLoc) {
                BoLocSanPhamBottomSheet(
                    boLocHienTai = boLoc,
                    onDismiss = {
                        hienBoLoc = false
                    },
                    onApDung = {
                        boLoc = it
                    }
                )
            }
        }
    ) { padding ->
        when {
            dangTai -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            danhSachLoc.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Không có sản phẩm của thương hiệu $brand")
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(padding)
                        .background(Color(0xFFF5F5F5)),
                    contentPadding = PaddingValues(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(danhSachLoc) { giay ->
                        ProductCard(
                            giay = giay,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                chuyenSangChiTiet(giay.maGiay)
                            }
                        )
                    }
                }
            }
        }
    }
}