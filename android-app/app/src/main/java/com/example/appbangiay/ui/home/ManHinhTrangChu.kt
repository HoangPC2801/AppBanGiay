package com.example.appbangiay.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import com.example.appbangiay.R
import com.example.appbangiay.model.Giay
import java.text.DecimalFormat

@Composable
fun ManHinhTrangChu(chuyenSangChiTiet: (Int) -> Unit) {
    val primaryBlue = Color(0xFF64A5FF)

    // 1. TẠO DANH SÁCH GIẢ ĐỂ TEST (Chuẩn theo model Giay)
    val danhSachGiay = listOf(
        Giay(1, "Nike Dunk High", 3239000f, "Mô tả", "https://images.unsplash.com/photo-1542291026-7eec264c27ff?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", 3),
        Giay(2, "Adidas Superstar", 2600000f, "Mô tả", "https://images.unsplash.com/photo-1518002171953-a080ee817e1f?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", 3),
        Giay(3, "Nike Air Max", 2649000f, "Mô tả", "https://images.unsplash.com/photo-1551107696-a4b0a5f60fba?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", 1),
        Giay(4, "Biti's Hunter X", 999000f, "Mô tả", "https://images.unsplash.com/photo-1608231387042-66d1773070a5?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60", 3)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // --- THANH TRÊN CÙNG (HEADER) MỚI ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(primaryBlue)
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // LOGO
                Image(
                    painter = painterResource(id = R.drawable.logo_hoangshoe2),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(55.dp)
                        .width(140.dp),
                    contentScale = ContentScale.Fit
                )

                // ICONS
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }

        // --- NỘI DUNG CUỘN ---
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            // 2. BANNER QUẢNG CÁO
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.banner_giay),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Sản phẩm mới", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Giảm giá cực sốc 50%", color = Color.White)
                    }
                }
            }

            // 3. DANH MỤC THƯƠNG HIỆU
            Text(
                text = "Thương hiệu",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BrandItem("Nike", R.drawable.logo_nike)
                BrandItem("Adidas", R.drawable.logo_adidas)
                BrandItem("Biti's", R.drawable.logo_bitis)
                BrandItem("Converse", R.drawable.ic_google)
            }

            // 4. DANH SÁCH SẢN PHẨM (Giữ nguyên giao diện cũ dùng Row/Column)
            Text(
                text = "Sản phẩm mới nhất",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // Tách danh sách thành các nhóm nhỏ (mỗi nhóm 2 item)
                val rows = danhSachGiay.chunked(2)

                for (rowItems in rows) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Hiển thị item đầu tiên của hàng
                        ProductCard(giay = rowItems[0], modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.width(16.dp))

                        // Hiển thị item thứ 2 của hàng (nếu có)
                        if (rowItems.size > 1) {
                            ProductCard(giay = rowItems[1], modifier = Modifier.weight(1f))
                        } else {
                            // Nếu hàng chỉ có 1 item (số lượng lẻ), chèn Spacer rỗng để đẩy item đầu cho đúng kích thước
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun BrandItem(name: String, iconRes: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(end = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color(0xFFEEEEEE), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = name,
                modifier = Modifier.size(35.dp)
            )
        }
        Text(text = name, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
    }
}

// THÊM THAM SỐ GIAY VÀO ĐÂY
@Composable
fun ProductCard(giay: Giay, modifier: Modifier = Modifier) {
    val formatter = DecimalFormat("#,###")
    val giaDaFormat = formatter.format(giay.giaTien)

    Card(
        modifier = modifier.clickable { /* Chuyển chi tiết */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // ĐỔI IMAGE THÀNH ASYNCIMAGE
            AsyncImage(
                model = giay.hinhAnh,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = giay.tenGiay,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 1,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${giaDaFormat}đ",
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp, top = 4.dp),
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}