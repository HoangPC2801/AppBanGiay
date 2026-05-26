package com.example.appbangiay.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.appbangiay.model.Giay
import java.text.DecimalFormat

@Composable
fun ProductCard(
    giay: Giay,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val formatter = DecimalFormat("#,###")
    val giaBan = formatter.format(giay.giaTien)
    val giaGoc = formatter.format(giay.giaGoc)

    val coGiamGia = giay.giaGoc > giay.giaTien && giay.phanTramGiam > 0

    Card(
        modifier = modifier
            .height(245.dp)
            .clickable { onClick() },
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
                    model = giay.hinhAnh,
                    contentDescription = giay.tenGiay,

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),

                    contentScale = ContentScale.Crop
                )

                // ===== SALE =====
                if (giay.phanTramGiam > 0) {

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .zIndex(2f)
                            .background(
                                Color.Red,
                                RoundedCornerShape(
                                    bottomEnd = 12.dp
                                )
                            )
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                    ) {

                        Text(
                            text = "🎁 SIÊU SALE 🎁",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // ===== % =====
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .zIndex(2f)
                            .background(
                                Color.Red,
                                RoundedCornerShape(
                                    bottomStart = 12.dp
                                )
                            )
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                    ) {

                        Text(
                            text = "-${giay.phanTramGiam}%",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = giay.tenGiay,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF222222),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${giaBan}đ",
                        color = Color(0xFFE53935),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    if (coGiamGia) {
                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "${giaGoc}đ",
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.LineThrough,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Text(
                        text = String.format("%.1f", giay.averageRating ?: 5.0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "| Đã bán ${giay.soldCount ?: 0}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}