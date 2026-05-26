package com.example.appbangiay.ui.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.appbangiay.database.YeuThichDao
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.launch
import androidx.compose.foundation.BorderStroke
import com.example.appbangiay.network.KetNoiServer

@Composable
fun ManHinhYeuThich(
    dao: YeuThichDao,
    quayLai: () -> Unit,
    moChiTiet: (Int) -> Unit
) {

    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val danhSach by dao
        .layDanhSachYeuThich(uid)
        .collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()

    LaunchedEffect(danhSach) {
        if (uid.isNotBlank()) {
            danhSach.forEach { item ->
                try {
                    val spMoi = KetNoiServer.api.layChiTietGiay(item.maGiay)

                    dao.capNhatThongTinYeuThich(
                        firebaseUid = uid,
                        maGiay = item.maGiay,
                        tenGiay = spMoi.tenGiay,
                        giaTien = spMoi.giaTien,
                        hinhAnh = spMoi.hinhAnh,
                        giaGoc = spMoi.giaGoc,
                        phanTramGiam = spMoi.phanTramGiam,
                        averageRating = spMoi.averageRating,
                        soldCount = spMoi.soldCount
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7FC))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(76.dp)
                .background(Color.White)
                .padding(horizontal = 12.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = quayLai) {
                Icon(Icons.Default.ArrowBack, null)
            }

            Text(
                text = "Sản phẩm yêu thích",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        if (danhSach.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(80.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Chưa có sản phẩm yêu thích",
                        color = Color.Gray,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = quayLai,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF064C8C)
                        )
                    ) {
                        Text("Khám phá ngay")
                    }
                }
            }

        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(danhSach) { item ->

                    Card(
                        modifier = Modifier
                            .height(245.dp)
                            .clickable {
                                moChiTiet(item.maGiay)
                            },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFFE5E5E5)),
                        elevation = CardDefaults.cardElevation(3.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                            ) {
                                AsyncImage(
                                    model = item.hinhAnh,
                                    contentDescription = item.tenGiay,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp),
                                    contentScale = ContentScale.Crop
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .background(
                                            Color.Red,
                                            RoundedCornerShape(bottomEnd = 12.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    if (item.phanTramGiam > 0) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .background(
                                                    Color.Red,
                                                    RoundedCornerShape(bottomEnd = 12.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "-${item.phanTramGiam}%",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            dao.xoaYeuThich(item)
                                        }
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Bỏ yêu thích",
                                        tint = Color.Red
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(
                                    text = item.tenGiay,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color(0xFF222222),
                                    lineHeight = 16.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${formatMoney(item.giaTien)}đ",
                                        color = Color(0xFFE53935),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    if (item.giaGoc > item.giaTien) {
                                        Spacer(modifier = Modifier.width(6.dp))

                                        Text(
                                            text = "${formatMoney(item.giaGoc)}đ",
                                            color = Color.Gray,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            textDecoration = TextDecoration.LineThrough,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "⭐ ${String.format("%.1f", item.averageRating ?: 5.0)}   Đã bán ${item.soldCount ?: 0}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatMoney(value: Float): String {
    return "%,.0f".format(value).replace(",", ".")
}