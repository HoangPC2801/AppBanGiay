package com.example.appbangiay.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appbangiay.model.Giay
import com.example.appbangiay.viewmodel.TrangChuViewModel
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.appbangiay.R

@Composable
fun ManHinhTrangChu(chuyenSangChiTiet: (Int) -> Unit) {
    val primaryBlue = Color(0xFF64A5FF)

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

        // Dùng Column cuộn để chứa nội dung
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = 12.dp)
        ) {

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
                        painter = painterResource(id = R.drawable.banner_giay), // Bạn cần thêm ảnh banner
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
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
                BrandItem("Converse", R.drawable.ic_google) // Thay bằng logo thật
            }

            // 4. DANH SÁCH SẢN PHẨM (Sử dụng Grid không cuộn bên trong Column)
            Text(
                text = "Sản phẩm mới nhất",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            // Ở đây tôi dùng Column + Row để giả lập Grid vì đang trong Column cuộn
            // Trong thực tế bạn có thể dùng LazyVerticalGrid với chiều cao cố định
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                for (i in 1..3) { // Giả lập 3 hàng
                    Row(modifier = Modifier.fillMaxWidth()) {
                        ProductCard(modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(16.dp))
                        ProductCard(modifier = Modifier.weight(1f))
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

@Composable
fun ProductCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.clickable { /* Chuyển chi tiết */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.logo_hoangshoe), // Thay bằng ảnh giày
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Giày Thể Thao Cao Cấp",
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 1,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "1.200.000đ",
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp, top = 4.dp),
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}