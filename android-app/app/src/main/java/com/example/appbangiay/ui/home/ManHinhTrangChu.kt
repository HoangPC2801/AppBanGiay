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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.appbangiay.viewmodel.TrangChuViewModel
import androidx.compose.ui.text.style.TextAlign
import com.example.appbangiay.ui.theme.MauXanhChinh
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.style.TextOverflow
@Composable
fun ManHinhTrangChu(
    chuyenSangChiTiet: (Int) -> Unit,
    chuyenSangThuongHieu: (String) -> Unit,
    chuyenSangDanhMuc: (String) -> Unit,
    chuyenSangTimKiem: () -> Unit,
    chuyenSangGioHang: () -> Unit,
    viewModel: TrangChuViewModel = viewModel()
) {
    val primaryBlue = MauXanhChinh

    val danhSachGiay by viewModel.danhSachGiay.collectAsState()
    val dangTai by viewModel.trangThaiTai.collectAsState()
    var hienBangDanhMuc by remember { mutableStateOf(false) }

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
                    painter = painterResource(id = R.drawable.logo4),
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

                    IconButton(
                        onClick = {
                            chuyenSangTimKiem()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = {
                            chuyenSangGioHang()
                        }
                    ) {
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

            Spacer(modifier = Modifier.height(12.dp))
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
                }
            }

            // 3. DANH MỤC THƯƠNG HIỆU
            Text(
                text = "THƯƠNG HIỆU",
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
                BrandItem(
                    "Nike",
                    R.drawable.logo_nike,
                    modifier = Modifier.clickable {
                        chuyenSangThuongHieu("Nike")
                    }
                )

                BrandItem(
                    "Adidas",
                    R.drawable.logo_adidas,
                    modifier = Modifier.clickable {
                        chuyenSangThuongHieu("Adidas")
                    }
                )

                BrandItem(
                    "Biti's",
                    R.drawable.logo_bitis,
                    modifier = Modifier.clickable {
                        chuyenSangThuongHieu("Bitis")
                    }
                )

                BrandItem(
                    "Xem thêm",
                    R.drawable.ic_xem_them,
                    modifier = Modifier.clickable {
                        hienBangDanhMuc = true
                    }
                )
            }

            // 4. DANH SÁCH SẢN PHẨM (Giữ nguyên giao diện cũ dùng Row/Column)
            Text(
                text = "SẢN PHẨM",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                when {
                    dangTai -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    danhSachGiay.isEmpty() -> {
                        Text(
                            text = "Chưa có sản phẩm nào",
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    else -> {
                        val rows = danhSachGiay.chunked(2)

                        for (rowItems in rows) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                ProductCard(
                                    giay = rowItems[0],
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        chuyenSangChiTiet(rowItems[0].maGiay)
                                    }
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                if (rowItems.size > 1) {
                                    ProductCard(
                                        giay = rowItems[1],
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            chuyenSangChiTiet(rowItems[1].maGiay)
                                        }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
        if (hienBangDanhMuc) {
            BangTatCaDanhMuc(
                onClose = { hienBangDanhMuc = false },
                onBrandClick = { brand ->
                    hienBangDanhMuc = false
                    chuyenSangThuongHieu(brand)
                },
                onCategoryClick = { category ->
                    hienBangDanhMuc = false
                    chuyenSangDanhMuc(category)
                }
            )
        }
    }
}

@Composable
fun BrandItem(
    name: String,
    iconRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(end = 20.dp)
    ) {

        Card(
            modifier = Modifier.size(72.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(
                1.dp,
                Color(0xFFE0E0E0)
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = name,
                    modifier = Modifier.size(38.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
    }
}

@Composable
fun ProductCard(
    giay: Giay,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val formatter = DecimalFormat("#,###")
    val giaDaFormat = formatter.format(giay.giaTien)

    Card(
        modifier = modifier.clickable {
            onClick()
        },

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
                contentDescription = null,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(10.dp),

                contentScale = ContentScale.Fit
            )

            Text(
                text = giay.tenGiay,

                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .height(46.dp),

                maxLines = 2,

                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,

                fontWeight = FontWeight.Bold,

                fontSize = 16.sp,

                color = Color(0xFF222222)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${giaDaFormat}đ",

                modifier = Modifier.padding(
                    start = 10.dp,
                    bottom = 12.dp
                ),

                color = Color.Red,

                fontWeight = FontWeight.Bold,

                fontSize = 18.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BangTatCaDanhMuc(
    onClose: () -> Unit,
    onBrandClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tất cả danh mục",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Đóng",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "THƯƠNG HIỆU",
                color = Color.Gray,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SheetItem("Nike", R.drawable.logo_nike) {
                    onBrandClick("Nike")
                }

                SheetItem("Adidas", R.drawable.logo_adidas) {
                    onBrandClick("Adidas")
                }

                SheetItem("Biti's", R.drawable.logo_bitis) {
                    onBrandClick("Bitis")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Divider()

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "CHỨC NĂNG",
                color = Color.Gray,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SheetItem("Sneaker", R.drawable.ic_sneaker) {
                    onCategoryClick("Sneaker")
                }

                SheetItem("Chạy bộ", R.drawable.ic_chay_bo) {
                    onCategoryClick("Chạy bộ")
                }

                SheetItem("Thể thao", R.drawable.ic_the_thao) {
                    onCategoryClick("Thể thao")
                }

                SheetItem("Công sở", R.drawable.ic_cong_so) {
                    onCategoryClick("Công sở")
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SheetItem(
    name: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFEAEAEA), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = name,
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}