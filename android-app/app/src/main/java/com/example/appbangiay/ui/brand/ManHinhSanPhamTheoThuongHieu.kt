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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appbangiay.model.Giay
import com.example.appbangiay.viewmodel.TrangChuViewModel
import androidx.compose.foundation.BorderStroke

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

    val danhSachLoc = danhSachGiay.filter {
        it.thuongHieu.equals(brand, ignoreCase = true)
    }

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
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Lọc"
                        )
                        Text("Lọc", fontWeight = FontWeight.Bold)
                    }
                }
            )
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
                        BrandProductCard(
                            giay = giay,
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

@Composable
fun BrandProductCard(
    giay: Giay,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),

        border = BorderStroke(
            1.dp,
            Color(0xFFE5E5E5)
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column {
            AsyncImage(
                model = giay.hinhAnh,
                contentDescription = giay.tenGiay,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .padding(12.dp),
                contentScale = ContentScale.Fit
            )

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = giay.tenGiay,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${formatMoney(giay.giaTien)}đ",
                    color = Color(0xFFE53935),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "⭐ 5.0  |  Đã bán 25",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

fun formatMoney(value: Float): String {
    return "%,.0f".format(value).replace(",", ".")
}

