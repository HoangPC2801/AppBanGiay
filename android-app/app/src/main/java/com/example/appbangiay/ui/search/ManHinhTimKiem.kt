package com.example.appbangiay.ui.search

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appbangiay.model.Giay
import com.example.appbangiay.viewmodel.TrangChuViewModel
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.BorderStroke

@Composable
fun ManHinhTimKiem(
    quayLai: () -> Unit,
    chuyenSangChiTiet: (Int) -> Unit,
    viewModel: TrangChuViewModel = viewModel()
) {
    val danhSachGiay by viewModel.danhSachGiay.collectAsState()

    var tuKhoa by remember {
        mutableStateOf("")
    }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    val ketQua = remember(tuKhoa, danhSachGiay) {
        if (tuKhoa.isBlank()) {
            emptyList()
        } else {
            danhSachGiay.filter {
                it.tenGiay.contains(tuKhoa, ignoreCase = true) ||
                        (it.thuongHieu?.contains(tuKhoa, ignoreCase = true) == true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .statusBarsPadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = quayLai) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Quay lại"
                )
            }

            OutlinedTextField(
                value = tuKhoa,
                onValueChange = {
                    tuKhoa = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = {
                    Text(
                        "Tìm tên giày, model..."
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
        }

        if (tuKhoa.isBlank()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Nhập từ khóa để tìm kiếm",
                        color = Color.Gray,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(ketQua) { giay ->

                    SearchProductCard(
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

@Composable
fun SearchProductCard(
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
                    .height(180.dp)
                    .padding(12.dp),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier.padding(10.dp)
            ) {

                Text(
                    text = giay.tenGiay,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${formatMoneySearch(giay.giaTien)}đ",
                    color = Color(0xFFE53935),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "⭐ 5.0  |  Đã bán 25",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun formatMoneySearch(value: Float): String {
    return "%,.0f".format(value).replace(",", ".")
}