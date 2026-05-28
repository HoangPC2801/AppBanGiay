package com.example.appbangiay.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.appbangiay.model.ChiTietDonHang
import com.example.appbangiay.network.KetNoiServer
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.BorderStroke

@Composable
fun ManHinhChiTietDonHang(
    orderId: Int,
    quayLai: () -> Unit
) {
    val context = LocalContext.current
    var donHang by remember { mutableStateOf<ChiTietDonHang?>(null) }
    var dangTai by remember { mutableStateOf(true) }

    var dangHuyDon by remember { mutableStateOf(false) }

    LaunchedEffect(orderId) {
        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            donHang = KetNoiServer.api.layChiTietDonHangCuaToi(orderId, uid)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            dangTai = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
    ) {
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
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }

            Text(
                text = "Chi tiết đơn hàng",
                modifier = Modifier.weight(1f),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.width(48.dp))
        }

        if (dangTai) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }

        val order = donHang ?: return@Column

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                CardBlock {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = Color(0xFF064C8C),
                            modifier = Modifier.size(34.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Trạng thái: ${hienThiTrangThai(order.status)}",
                                color = Color(0xFF064C8C),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Text(
                                text = "Ngày đặt: ${formatDateOrder(order.createdAt)}",
                                color = Color.Gray,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            if (order.status == "pending") {
                item {
                    Button(
                        onClick = {
                            dangHuyDon = true

                            GlobalScope.launch(Dispatchers.IO) {
                                try {
                                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

                                    KetNoiServer.api.huyDonHang(
                                        orderId = order.id,
                                        firebaseUid = uid
                                    )

                                    withContext(Dispatchers.Main) {
                                        donHang = donHang?.copy(status = "cancelled")

                                        Toast.makeText(
                                            context,
                                            "Đã hủy đơn hàng",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Không thể hủy đơn",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } finally {
                                    withContext(Dispatchers.Main) {
                                        dangHuyDon = false
                                    }
                                }
                            }
                        },
                        enabled = !dangHuyDon,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (dangHuyDon) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Hủy đơn hàng",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            item {
                CardBlock {
                    Text(
                        text = "Địa chỉ nhận hàng",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = order.customerName ?: "Khách hàng",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = order.customerEmail ?: "",
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = order.shippingAddress ?: "Chưa có địa chỉ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF444444)
                    )
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(
                        1.dp,
                        Color(0xFFE0E0E0)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column {
                        Text(
                            text = "Sản phẩm",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(16.dp)
                        )

                        HorizontalDivider()

                        order.items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = item.productImage,
                                    contentDescription = item.productName,
                                    modifier = Modifier.size(72.dp),
                                    contentScale = ContentScale.Fit
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.productName ?: "Sản phẩm",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )

                                    Text(
                                        text = "Màu sắc: ${item.color ?: "Không có"}",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "Chọn size: ${item.size ?: "Không có"}",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "x${item.quantity}",
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }

                                Text(
                                    text = "${formatMoneyOrder(item.price)}đ",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            item {
                CardBlock {
                    SummaryDetailRow("Tạm tính", "${formatMoneyOrder(order.total)}đ")
                    Spacer(modifier = Modifier.height(20.dp))
                    SummaryDetailRow("Phí vận chuyển", "0đ")
                    Spacer(modifier = Modifier.height(20.dp))
                    SummaryDetailRow(
                        "Tổng cộng",
                        "${formatMoneyOrder(order.total)}đ",
                        red = true
                    )
                }
            }
        }
    }
}

@Composable
fun CardBlock(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            1.dp,
            Color(0xFFE0E0E0)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
fun SummaryDetailRow(
    title: String,
    value: String,
    red: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 18.sp,
            color = Color.Gray,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = value,
            fontSize = 19.sp,
            color = if (red) Color(0xFFE53935) else Color.Black,
            fontWeight = FontWeight.ExtraBold
        )
    }
}