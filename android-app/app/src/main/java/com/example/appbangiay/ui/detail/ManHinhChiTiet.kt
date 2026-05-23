package com.example.appbangiay.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appbangiay.viewmodel.ChiTietGiayViewModel
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appbangiay.database.GioHangDao
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import com.example.appbangiay.database.YeuThichDao
import com.example.appbangiay.model.SanPhamYeuThich
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhChiTiet(
    maGiay: Int,
    dao: GioHangDao,
    yeuThichDao: YeuThichDao,
    quayLai: () -> Unit,
    chuyenSangGioHang: () -> Unit,
    yeuCauDangNhap: () -> Unit,
    muaNgay: () -> Unit = {}
) {
    val viewModel: ChiTietGiayViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChiTietGiayViewModel(dao) as T
            }
        }
    )

    val giay by viewModel.giayChiTiet.collectAsState()
    val dangTai by viewModel.trangThaiTai.collectAsState()

    var sizeDaChon by remember { mutableStateOf<String?>(null) }
    var mauDaChon by remember { mutableStateOf<String?>(null) }
    var daYeuThich by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    LaunchedEffect(maGiay) {
        viewModel.layThongTinGiay(maGiay)

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            daYeuThich = yeuThichDao.kiemTraDaYeuThich(
                firebaseUid = uid,
                maGiay = maGiay
            ) != null
        }
    }

    Scaffold(
        bottomBar = {
            BottomActionBar(
                gia = giay?.giaTien ?: 0f,
                onChatClick = {},
                onCartClick = {
                    if ((giay?.variants?.isNotEmpty() == true) && (mauDaChon == null || sizeDaChon == null)) {
                        Toast.makeText(
                            context,
                            "Vui lòng chọn màu sắc và size giày",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        giay?.let {
                            val currentUser = FirebaseAuth.getInstance().currentUser

                            if (currentUser == null) {
                                yeuCauDangNhap()
                            } else {

                                viewModel.themVaoGioHang(
                                    giay = it,
                                    mauSac = mauDaChon,
                                    size = sizeDaChon
                                )

                                Toast.makeText(
                                    context,
                                    "Đã thêm vào giỏ hàng",
                                    Toast.LENGTH_SHORT
                                ).show()

                                chuyenSangGioHang()
                            }
                        }
                    }
                },
                onBuyNowClick = {
                    if ((giay?.variants?.isNotEmpty() == true) && (mauDaChon == null || sizeDaChon == null)) {
                        Toast.makeText(
                            context,
                            "Vui lòng chọn màu sắc và size giày",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        muaNgay()
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

            giay == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Không tìm thấy sản phẩm")
                }
            }

            else -> {
                val product = giay!!
                val danhSachMau = product.variants
                    .mapNotNull { it.mauSac }
                    .filter { it.isNotBlank() }
                    .distinct()

                val danhSachSizeTheoMau = product.variants
                    .filter { variant ->
                        mauDaChon == null || variant.mauSac == mauDaChon
                    }
                    .mapNotNull { it.size }
                    .filter { it.isNotBlank() }
                    .distinct()

                val bienTheDaChon = product.variants.firstOrNull {
                    it.mauSac == mauDaChon && it.size == sizeDaChon
                }

                LaunchedEffect(product.variants) {
                    if (mauDaChon == null && danhSachMau.isNotEmpty()) {
                        mauDaChon = danhSachMau.first()
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color.White)
                ) {
                    item {
                        Box {
                            AsyncImage(
                                model = product.hinhAnh,
                                contentDescription = product.tenGiay,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(360.dp)
                                    .padding(24.dp),
                                contentScale = ContentScale.Fit
                            )

                            IconButton(
                                onClick = quayLai,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.TopStart)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Quay lại",
                                    tint = Color(0xFF064C8C)
                                )
                            }

                            IconButton(
                                onClick = chuyenSangGioHang,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Giỏ hàng",
                                    tint = Color(0xFF064C8C)
                                )
                            }
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${formatMoney(product.giaTien)}đ",
                                        color = Color(0xFFE53935),
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = "${formatMoney(product.giaTien * 1.1f)}đ",
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            val uid = FirebaseAuth.getInstance().currentUser?.uid

                                            if (uid == null) {
                                                yeuCauDangNhap()
                                                return@launch
                                            }

                                            val daTonTai = yeuThichDao.kiemTraDaYeuThich(
                                                firebaseUid = uid,
                                                maGiay = product.maGiay
                                            )

                                            if (daTonTai == null) {
                                                yeuThichDao.themYeuThich(
                                                    SanPhamYeuThich(
                                                        firebaseUid = uid,
                                                        maGiay = product.maGiay,
                                                        tenGiay = product.tenGiay,
                                                        giaTien = product.giaTien,
                                                        hinhAnh = product.hinhAnh
                                                    )
                                                )

                                                daYeuThich = true
                                            } else {
                                                yeuThichDao.xoaYeuThich(daTonTai)
                                                daYeuThich = false
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector =
                                            if (daYeuThich)
                                                Icons.Default.Favorite
                                            else
                                                Icons.Default.FavoriteBorder,

                                        contentDescription = null,

                                        tint =
                                            if (daYeuThich)
                                                Color.Red
                                            else
                                                Color.LightGray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = product.tenGiay,
                                color = Color(0xFF222222),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 28.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFF4F4F4), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Mã: ${product.maGiay}",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Text("⭐", fontSize = 18.sp)

                                Text(
                                    text = " 5.0/5 ",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                                Text(
                                    text = "(0 đánh giá)",
                                    color = Color(0xFF064C8C),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    item {
                        Divider(modifier = Modifier.padding(top = 16.dp))
                    }

                    item {
                        ColorSection(
                            danhSachMau = danhSachMau,
                            mauDaChon = mauDaChon,
                            onColorSelected = { mau ->
                                mauDaChon = mau
                                sizeDaChon = null
                            }
                        )

                        SizeSection(
                            danhSachSize = danhSachSizeTheoMau,
                            sizeDaChon = sizeDaChon,
                            onSizeSelected = { sizeDaChon = it }
                        )
                    }

                    item {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Mô tả sản phẩm",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = product.moTa ?: "Chưa có mô tả sản phẩm.",
                                fontSize = 17.sp,
                                lineHeight = 26.sp,
                                color = Color(0xFF333333)
                            )

                            Spacer(modifier = Modifier.height(90.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColorSection(
    danhSachMau: List<String>,
    mauDaChon: String?,
    onColorSelected: (String) -> Unit
) {
    if (danhSachMau.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Màu sắc",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            danhSachMau.forEach { mau ->
                val selected = mau == mauDaChon

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = if (selected) 2.dp else 1.dp,
                            color = if (selected) Color(0xFF064C8C) else Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            if (selected) Color.White else Color(0xFFF5F5F5)
                        )
                        .clickable {
                            onColorSelected(mau)
                        }
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = mau,
                        color = if (selected) Color(0xFF064C8C) else Color(0xFF222222),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SizeSection(
    danhSachSize: List<String>,
    sizeDaChon: String?,
    onSizeSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Kích thước",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "▣ Hướng dẫn chọn size",
                color = Color(0xFF064C8C),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            danhSachSize.forEach { size ->
                val selected = size == sizeDaChon

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .border(
                            width = if (selected) 2.dp else 1.dp,
                            color = if (selected) Color(0xFF064C8C) else Color.Transparent,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .background(
                            if (selected) Color.White else Color(0xFFF5F5F5)
                        )
                        .clickable {
                            onSizeSelected(size)
                        }
                        .padding(horizontal = 22.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = size,
                        color = if (selected) Color(0xFF064C8C) else Color(0xFF222222),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun BottomActionBar(
    gia: Float,
    onChatClick: () -> Unit,
    onCartClick: () -> Unit,
    onBuyNowClick: () -> Unit
) {
    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(70.dp)
                    .clickable { onChatClick() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "Chat",
                    tint = Color(0xFF064C8C)
                )
                Text(
                    text = "Chat",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = onCartClick,
                modifier = Modifier
                    .height(56.dp)
                    .width(120.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFFE53935))
            ) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = "Thêm giỏ hàng",
                    tint = Color(0xFFE53935)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onBuyNowClick,
                modifier = Modifier
                    .height(56.dp)
                    .weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935)
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "MUA NGAY",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${formatMoney(gia)}đ | Freeship",
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

fun formatMoney(value: Float): String {
    return "%,.0f".format(value).replace(",", ".")
}

