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

                            Box {

                                AsyncImage(
                                    model = item.hinhAnh,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp),
                                    contentScale = ContentScale.Fit
                                )

                                Box(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Red)
                                        .padding(
                                            horizontal = 10.dp,
                                            vertical = 4.dp
                                        )
                                ) {
                                    Text(
                                        text = "-5%",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
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
                                    .fillMaxSize()
                                    .padding(12.dp)
                            ) {

                                Text(
                                    text = item.tenGiay,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.height(46.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${formatMoney(item.giaTien)}đ",
                                        color = Color(0xFF064C8C),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
                                        text = "${formatMoney(item.giaTien * 1.1f)}đ",
                                        color = Color.Gray,
                                        fontSize = 13.sp,
                                        textDecoration = TextDecoration.LineThrough,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.width(70.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedButton(
                                    onClick = {
                                        moChiTiet(item.maGiay)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                ) {
                                    Text(
                                        text = "Thêm vào giỏ",
                                        color = Color(0xFF064C8C),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
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