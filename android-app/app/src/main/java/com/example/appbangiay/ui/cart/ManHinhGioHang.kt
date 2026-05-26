package com.example.appbangiay.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.appbangiay.database.GioHangDao
import com.example.appbangiay.model.GioHang
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.BorderStroke
import com.example.appbangiay.network.KetNoiServer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhGioHang(
    dao: GioHangDao,
    quayLai: () -> Unit,
    chuyenSangThanhToan: () -> Unit
) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    val gioHang by if (uid != null) {
        dao.layTheoNguoiDung(uid).collectAsState(initial = emptyList())
    } else {
        remember { mutableStateOf(emptyList()) }
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(gioHang) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@LaunchedEffect

        gioHang.forEach { item ->
            try {
                val spMoi = KetNoiServer.api.layChiTietGiay(item.maGiay)

                dao.capNhatThongTinSanPhamTrongGioHang(
                    firebaseUid = uid,
                    maGiay = item.maGiay,
                    tenGiay = spMoi.tenGiay,
                    giaTien = spMoi.giaTien,
                    hinhAnh = spMoi.hinhAnh
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val tongTien = gioHang.sumOf {
        (it.giaTien * it.soLuong).toDouble()
    }.toFloat()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(72.dp)
                    .background(Color.White)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = quayLai) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = Color.Black
                    )
                }

                Text(
                    text = "Giỏ hàng",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(48.dp))
            }
        },
        bottomBar = {
            if (gioHang.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .navigationBarsPadding()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tổng cộng:",
                            color = Color.Gray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "${formatMoneyCart(tongTien)}đ",
                            color = Color(0xFF064C8C),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = chuyenSangThanhToan,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF064C8C)
                        )
                    ) {
                        Text(
                            text = "TIẾN HÀNH THANH TOÁN",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (gioHang.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF7F7FC)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(90.dp),
                        tint = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Giỏ hàng của bạn đang trống",
                        color = Color.Gray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = quayLai,
                        modifier = Modifier
                            .width(240.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF064C8C)
                        )
                    ) {
                        Text(
                            text = "Tiếp tục mua sắm",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF7F7FC)),
                contentPadding = PaddingValues(10.dp)
            ) {
                items(gioHang) { item ->
                    CartItemCard(
                        item = item,
                        onDelete = {
                            scope.launch {
                                dao.xoa(item)
                            }
                        },
                        onDecrease = {
                            scope.launch {
                                if (item.soLuong > 1) {
                                    dao.capNhat(item.copy(soLuong = item.soLuong - 1))
                                } else {
                                    dao.xoa(item)
                                }
                            }
                        },
                        onIncrease = {
                            scope.launch {
                                dao.capNhat(item.copy(soLuong = item.soLuong + 1))
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: GioHang,
    onDelete: () -> Unit,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFE5E5E5)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Box(
            modifier = Modifier.padding(12.dp)
        ) {
            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Xóa",
                    tint = Color.Gray
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = item.hinhAnh,
                    contentDescription = item.tenGiay,
                    modifier = Modifier
                        .size(90.dp)
                        .padding(end = 10.dp),
                    contentScale = ContentScale.Fit
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 42.dp)
                ) {
                    Text(
                        text = item.tenGiay,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Màu sắc: ${item.mauSac ?: "Chưa chọn"}",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Chọn size: ${item.size ?: "Chưa chọn"}",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${formatMoneyCart(item.giaTien)}đ",
                        color = Color(0xFF064C8C),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDecrease,
                    modifier = Modifier.size(36.dp)
                ) {
                    Text("-", fontSize = 22.sp)
                }

                Text(
                    text = item.soLuong.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier.size(36.dp)
                ) {
                    Text("+", fontSize = 22.sp)
                }
            }
        }
    }
}

fun formatMoneyCart(value: Float): String {
    return "%,.0f".format(value).replace(",", ".")
}