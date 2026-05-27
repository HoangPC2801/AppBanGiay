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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import com.example.appbangiay.model.ProductReviewOut
import com.example.appbangiay.network.KetNoiServer
import com.example.appbangiay.model.ProductReviewCreate
import com.example.appbangiay.model.ProductReviewSummary
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import com.example.appbangiay.database.ReviewCacheDao
import com.example.appbangiay.model.ReviewCache
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.collectAsState
import com.example.appbangiay.ui.components.CartIconWithBadge
import com.example.appbangiay.model.GioHang

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ManHinhChiTiet(
    maGiay: Int,
    dao: GioHangDao,
    yeuThichDao: YeuThichDao,
    reviewCacheDao: ReviewCacheDao,
    quayLai: () -> Unit,
    chuyenSangGioHang: () -> Unit,
    yeuCauDangNhap: () -> Unit,
    muaNgay: (GioHang) -> Unit = {},
    gioHangDao: GioHangDao
) {
    val viewModel: ChiTietGiayViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChiTietGiayViewModel(dao) as T
            }
        }
    )

    val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val soLuongGioHang by gioHangDao
        .layTongSoLuongGioHang(firebaseUid)
        .collectAsState(initial = 0)

    val giay by viewModel.giayChiTiet.collectAsState()
    val dangTai by viewModel.trangThaiTai.collectAsState()

    var sizeDaChon by remember { mutableStateOf<String?>(null) }
    var mauDaChon by remember { mutableStateOf<String?>(null) }
    var daYeuThich by remember { mutableStateOf(false) }
    var hienBangDanhGia by remember { mutableStateOf(false) }
    var diemTrungBinh by remember { mutableStateOf(0f) }
    var soDanhGia by remember { mutableStateOf(0) }
    var danhSachDanhGia by remember { mutableStateOf<List<ProductReviewOut>>(emptyList()) }
    val reviewCache by reviewCacheDao
        .layReviewCache(maGiay)
        .collectAsState(initial = emptyList())
    var soLuongDaBan by remember { mutableStateOf(0) }
    var hienBangSize by remember { mutableStateOf(false) }

    var tabDangChon by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(reviewCache) {
        if (reviewCache.isNotEmpty() && danhSachDanhGia.isEmpty()) {
            danhSachDanhGia = reviewCache.map {
                ProductReviewOut(
                    id = it.id,
                    productId = it.productId,
                    firebaseUid = it.firebaseUid ?: "",
                    userName = it.userName,
                    rating = it.rating,
                    comment = it.comment,
                    reviewImage = it.reviewImage,
                    isHidden = false,
                    adminReply = it.adminReply,
                    adminReplyAt = null,
                    createdAt = it.createdAt,
                    updatedAt = null,
                    likeCount = it.likeCount
                )
            }
        }
    }

    LaunchedEffect(maGiay) {
        viewModel.layThongTinGiay(maGiay)

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            daYeuThich = yeuThichDao.kiemTraDaYeuThich(
                firebaseUid = uid,
                maGiay = maGiay
            ) != null
        }

        try {
            val reviewData = KetNoiServer.api.layDanhGiaSanPham(
                productId = maGiay,
                page = 1,
                limit = 10
            )

            diemTrungBinh = reviewData.averageRating
            soDanhGia = reviewData.reviewCount
            soLuongDaBan = reviewData.soldCount
            danhSachDanhGia = reviewData.reviews
            reviewCacheDao.xoaReviewTheoSanPham(maGiay)

            reviewCacheDao.themDanhSachReview(
                reviewData.reviews.map {
                    ReviewCache(
                        id = it.id,
                        productId = it.productId,
                        firebaseUid = it.firebaseUid,
                        userName = it.userName,
                        rating = it.rating,
                        comment = it.comment,
                        reviewImage = it.reviewImage,
                        adminReply = it.adminReply,
                        createdAt = it.createdAt,
                        likeCount = it.likeCount
                    )
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(64.dp)
                    .background(Color.White)
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = quayLai) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = Color(0xFF064C8C),
                        modifier = Modifier.size(30.dp)
                    )
                }

                CartIconWithBadge(
                    soLuong = soLuongGioHang,
                    onClick = {
                        chuyenSangGioHang()
                    },
                    iconColor = Color(0xFF064C8C)
                )
            }
        },
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

                                val bienThe = it.variants.firstOrNull { variant ->
                                    variant.mauSac == mauDaChon && variant.size == sizeDaChon
                                }

                                val tonKho = bienThe?.soLuongTon ?: it.soLuongTon

                                if (tonKho <= 0) {
                                    Toast.makeText(context, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show()
                                    return@let
                                }

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
                        val currentUser = FirebaseAuth.getInstance().currentUser

                        if (currentUser == null) {
                            yeuCauDangNhap()
                        } else {
                            giay?.let {

                                val bienThe = it.variants.firstOrNull { variant ->
                                    variant.mauSac == mauDaChon && variant.size == sizeDaChon
                                }

                                val tonKho = bienThe?.soLuongTon ?: it.soLuongTon

                                if (tonKho <= 0) {
                                    Toast.makeText(context, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show()
                                    return@let
                                }

                                muaNgay(
                                    GioHang(
                                        firebaseUid = currentUser.uid,
                                        maGiay = it.maGiay,
                                        tenGiay = it.tenGiay,
                                        giaTien = it.giaTien,
                                        hinhAnh = it.hinhAnh,
                                        mauSac = mauDaChon,
                                        size = sizeDaChon,
                                        soLuong = 1
                                    )
                                )
                            }
                        }
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
                val tongTonKhoBienThe = product.variants.sumOf { it.soLuongTon }

                val bienTheDaChon = product.variants.firstOrNull { variant ->
                    variant.mauSac == mauDaChon && variant.size == sizeDaChon
                }

                val tonKhoDangHienThi = when {
                    bienTheDaChon != null -> bienTheDaChon.soLuongTon
                    mauDaChon != null -> product.variants
                        .filter { it.mauSac == mauDaChon }
                        .sumOf { it.soLuongTon }
                    else -> tongTonKhoBienThe
                }

                if (tongTonKhoBienThe <= 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sản phẩm này đã hết hàng",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }

                    return@Scaffold
                }

                val danhSachMau = product.variants
                    .filter { it.soLuongTon > 0 }
                    .mapNotNull { it.mauSac }
                    .filter { it.isNotBlank() }
                    .distinct()

                val danhSachSizeTheoMau = product.variants
                    .filter { variant ->
                        variant.soLuongTon > 0 &&
                                (mauDaChon == null || variant.mauSac == mauDaChon)
                    }
                    .mapNotNull { it.size }
                    .filter { it.isNotBlank() }
                    .distinct()

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
                        val danhSachAnh = remember(product) {
                            val anhChinh = if (!product.hinhAnh.isNullOrBlank()) {
                                listOf(product.hinhAnh)
                            } else {
                                emptyList()
                            }

                            val anhPhu = product.images
                                .sortedBy { it.thuTu }
                                .map { it.imageUrl }
                                .filter { it.isNotBlank() }

                            (anhChinh + anhPhu).distinct()
                        }

                        val pagerState = rememberPagerState(
                            pageCount = {
                                if (danhSachAnh.isEmpty()) 1 else danhSachAnh.size
                            }
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(360.dp)
                            ) { page ->

                                val imageUrl =
                                    if (danhSachAnh.isEmpty())
                                        product.hinhAnh
                                    else
                                        danhSachAnh[page]

                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = product.tenGiay,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            if (danhSachAnh.size > 1) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    repeat(danhSachAnh.size) { index ->
                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = 4.dp)
                                                .size(
                                                    if (pagerState.currentPage == index)
                                                        10.dp
                                                    else
                                                        7.dp
                                                )
                                                .clip(CircleShape)
                                                .background(
                                                    if (pagerState.currentPage == index)
                                                        Color(0xFF064C8C)
                                                    else
                                                        Color.LightGray
                                                )
                                        )
                                    }
                                }
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

                                    if (product.giaGoc > product.giaTien && product.phanTramGiam > 0) {
                                        Text(
                                            text = "${formatMoney(product.giaGoc)}đ",
                                            color = Color.Gray,
                                            fontSize = 16.sp,
                                            textDecoration = TextDecoration.LineThrough
                                        )

                                        Spacer(modifier = Modifier.width(10.dp))

                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    Color(0xFFFFEBEE),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "-${product.phanTramGiam}%",
                                                color = Color(0xFFE53935),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
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
                                                        hinhAnh = product.hinhAnh,

                                                        giaGoc = product.giaGoc,
                                                        phanTramGiam = product.phanTramGiam,
                                                        averageRating = product.averageRating,
                                                        soldCount = product.soldCount
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
                                    text = " ${if (diemTrungBinh == 0f) "5.0" else diemTrungBinh}/5 ",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                                Text(
                                    text = "($soDanhGia đánh giá)",
                                    color = Color(0xFF064C8C),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        hienBangDanhGia = true
                                    }
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "|",
                                    color = Color.LightGray,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "Đã bán $soLuongDaBan",
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        hienBangDanhGia = true
                                    }
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = Color.LightGray,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable {
                                            hienBangDanhGia = true
                                        }
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
                            onSizeSelected = { sizeDaChon = it },
                            onOpenSizeGuide = {
                                hienBangSize = true
                            }
                        )
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Còn lại: $tonKhoDangHienThi sản phẩm",
                                color = if (tonKhoDangHienThi > 0)
                                    Color(0xFF4CAF50)
                                else
                                    Color.Red,

                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
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

        if (hienBangDanhGia) {
            BangDanhGiaSanPham(
                maGiay = maGiay,
                danhSachDanhGia = danhSachDanhGia,
                onClose = {
                    hienBangDanhGia = false
                },
                yeuCauDangNhap = yeuCauDangNhap,
                onReviewSubmitted = { reviewData ->
                    diemTrungBinh = reviewData.averageRating
                    soDanhGia = reviewData.reviewCount
                    soLuongDaBan = reviewData.soldCount
                    danhSachDanhGia = reviewData.reviews
                    scope.launch {
                        reviewCacheDao.xoaReviewTheoSanPham(maGiay)

                        reviewCacheDao.themDanhSachReview(
                            reviewData.reviews.map {
                                ReviewCache(
                                    id = it.id,
                                    productId = it.productId,
                                    firebaseUid = it.firebaseUid,
                                    userName = it.userName,
                                    rating = it.rating,
                                    comment = it.comment,
                                    reviewImage = it.reviewImage,
                                    adminReply = it.adminReply,
                                    createdAt = it.createdAt,
                                    likeCount = it.likeCount
                                )
                            }
                        )
                    }
                }
            )
        }

        if (hienBangSize) {

            ModalBottomSheet(
                onDismissRequest = {
                    hienBangSize = false
                },

                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                ),

                shape = RoundedCornerShape(
                    topStart = 28.dp,
                    topEnd = 28.dp
                ),

                containerColor = Color.White
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.92f)
                        .padding(20.dp)
                ) {

                    // HEADER
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column {

                            Text(
                                text = "Bảng quy đổi kích cỡ",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Thương hiệu: ${giay?.thuongHieu ?: "Không rõ"}",
                                color = Color.Gray,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        IconButton(
                            onClick = {
                                hienBangSize = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // TAB
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Text(
                            text = "Nam (Men)",
                            fontWeight = FontWeight.Bold,
                            color =
                                if (tabDangChon == 0)
                                    Color(0xFF064C8C)
                                else
                                    Color.Gray,

                            fontSize = 20.sp,

                            modifier = Modifier.clickable {
                                tabDangChon = 0
                            }
                        )

                        Text(
                            text = "Nữ (Women)",
                            fontWeight = FontWeight.Bold,
                            color =
                                if (tabDangChon == 1)
                                    Color(0xFF064C8C)
                                else
                                    Color.Gray,

                            fontSize = 20.sp,

                            modifier = Modifier.clickable {
                                tabDangChon = 1
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    BangSize(tab = tabDangChon)
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
    onSizeSelected: (String) -> Unit,
    onOpenSizeGuide: () -> Unit
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
                color = Color(0xFF0D47A1),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable {
                        onOpenSizeGuide()
                    }
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

            Spacer(modifier = Modifier.width(6.dp))

            OutlinedButton(
                onClick = onCartClick,
                modifier = Modifier
                    .height(56.dp)
                    .width(100.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFFE53935))
            ) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = "Thêm giỏ hàng",
                    tint = Color(0xFFE53935)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BangDanhGiaSanPham(
    maGiay: Int,
    danhSachDanhGia: List<ProductReviewOut>,
    onClose: () -> Unit,
    yeuCauDangNhap: () -> Unit,
    onReviewSubmitted: (com.example.appbangiay.model.ProductReviewSummary) -> Unit
) {
    var tabDangChon by remember { mutableStateOf(0) }
    var soSao by remember { mutableStateOf(5) }
    var hoTen by remember { mutableStateOf("") }
    var noiDung by remember { mutableStateOf("") }
    var pageHienTai by remember { mutableStateOf(1) }
    var dangTaiThem by remember { mutableStateOf(false) }
    var conDuLieu by remember { mutableStateOf(true) }
    var anhDaChon by remember { mutableStateOf<Uri?>(null) }
    var dangUploadAnh by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val chonAnhLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        anhDaChon = uri
    }

    val scope = rememberCoroutineScope()
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
                .heightIn(min = 620.dp)
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Đánh giá sản phẩm",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Đóng",
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TabDanhGia(
                    text = "Tất cả đánh giá",
                    selected = tabDangChon == 0,
                    modifier = Modifier.weight(1f)
                ) {
                    tabDangChon = 0
                }

                TabDanhGia(
                    text = "Viết đánh giá",
                    selected = tabDangChon == 1,
                    modifier = Modifier.weight(1f)
                ) {
                    tabDangChon = 1
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (tabDangChon == 0) {
                Column {
                    DanhSachDanhGiaThat(
                        maGiay = maGiay,
                        danhSach = danhSachDanhGia,
                        onReviewSubmitted = onReviewSubmitted,
                        yeuCauDangNhap = yeuCauDangNhap
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (conDuLieu && danhSachDanhGia.isNotEmpty()) {
                        Button(
                            onClick = {
                                scope.launch {
                                    try {
                                        dangTaiThem = true

                                        val nextPage = pageHienTai + 1

                                        val reviewData = KetNoiServer.api.layDanhGiaSanPham(
                                            productId = maGiay,
                                            page = nextPage,
                                            limit = 10
                                        )

                                        if (reviewData.reviews.isEmpty()) {
                                            conDuLieu = false
                                        } else {
                                            pageHienTai = nextPage

                                            val danhSachMoi = danhSachDanhGia + reviewData.reviews

                                            onReviewSubmitted(
                                                reviewData.copy(
                                                    reviews = danhSachMoi
                                                )
                                            )
                                        }

                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Không tải thêm được đánh giá",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } finally {
                                        dangTaiThem = false
                                    }
                                }
                            },
                            enabled = !dangTaiThem,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF064C8C)
                            )
                        ) {
                            Text(
                                text = if (dangTaiThem) "ĐANG TẢI..." else "Xem thêm đánh giá",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bạn cảm thấy sản phẩm thế nào?",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (i in 1..5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (i <= soSao) Color(0xFFFFC107) else Color.LightGray,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable {
                                        soSao = i
                                    }
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(42.dp))

                    OutlinedTextField(
                        value = hoTen,
                        onValueChange = { hoTen = it },
                        placeholder = {
                            Text("Họ tên của bạn")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = noiDung,
                        onValueChange = { noiDung = it },
                        placeholder = {
                            Text("Chia sẻ cảm nhận...")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    )
                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedButton(
                        onClick = {
                            chonAnhLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text =
                                if (anhDaChon == null)
                                    "Thêm ảnh đánh giá"
                                else
                                    "Đổi ảnh đánh giá",

                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (anhDaChon != null) {

                        Spacer(modifier = Modifier.height(14.dp))

                        AsyncImage(
                            model = anhDaChon,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(34.dp))

                    Button(
                        onClick = {
                            val uid = FirebaseAuth.getInstance().currentUser?.uid

                            if (uid == null) {
                                yeuCauDangNhap()
                                return@Button
                            }

                            dangUploadAnh = true

                            fun guiReviewSauKhiCoAnh(imageUrl: String?) {
                                scope.launch {
                                    try {
                                        KetNoiServer.api.guiDanhGiaSanPham(
                                            productId = maGiay,
                                            review = ProductReviewCreate(
                                                firebaseUid = uid,
                                                userName = hoTen,
                                                rating = soSao,
                                                comment = noiDung,
                                                reviewImage = imageUrl
                                            )
                                        )

                                        val reviewData = KetNoiServer.api.layDanhGiaSanPham(
                                            productId = maGiay,
                                            page = 1,
                                            limit = 10
                                        )

                                        pageHienTai = 1
                                        conDuLieu = true
                                        onReviewSubmitted(reviewData)
                                        onClose()
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Gửi đánh giá thất bại: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } finally {
                                        dangUploadAnh = false
                                    }
                                }
                            }

                            if (anhDaChon != null) {
                                uploadReviewImageToFirebase(
                                    imageUri = anhDaChon!!,
                                    onSuccess = { imageUrl ->
                                        guiReviewSauKhiCoAnh(imageUrl)
                                    },
                                    onError = { e ->
                                        dangUploadAnh = false
                                        Toast.makeText(
                                            context,
                                            "Upload ảnh thất bại: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            } else {
                                guiReviewSauKhiCoAnh(   null)
                            }
                        },
                        enabled = !dangUploadAnh,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF064C8C)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text =
                                if (dangUploadAnh)
                                    "ĐANG GỬI..."
                                else
                                    "GỬI ĐÁNH GIÁ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TabDanhGia(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = if (selected) Color(0xFF064C8C) else Color.Gray,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .height(4.dp)
                .width(130.dp)
                .background(
                    if (selected) Color(0xFF064C8C) else Color.Transparent,
                    RoundedCornerShape(50)
                )
        )
    }
}

@Composable
fun DanhSachDanhGiaThat(
    maGiay: Int,
    danhSach: List<ProductReviewOut>,
    onReviewSubmitted: (ProductReviewSummary) -> Unit,
    yeuCauDangNhap: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column {
        if (danhSach.isEmpty()) {
            Text(
                text = "Chưa có đánh giá nào",
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        } else {
            danhSach.forEach { review ->
                DanhGiaItem(
                    ten = review.userName ?: "Người dùng",
                    ngay = review.createdAt?.take(10) ?: "",
                    noiDung = review.comment ?: "",
                    reviewImage = review.reviewImage,
                    adminReply = review.adminReply,
                    likeCount = review.likeCount,
                    onLikeClick = {
                        val uid = FirebaseAuth.getInstance().currentUser?.uid

                        if (uid == null) {
                            yeuCauDangNhap()
                            return@DanhGiaItem
                        }

                        scope.launch {
                            try {
                                KetNoiServer.api.likeReview(
                                    reviewId = review.id,
                                    firebaseUid = uid
                                )

                                val reviewData = KetNoiServer.api.layDanhGiaSanPham(
                                    productId = maGiay,
                                    page = 1,
                                    limit = 10
                                )

                                onReviewSubmitted(reviewData)

                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Không thể thích đánh giá: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DanhGiaItem(
    ten: String,
    ngay: String,
    noiDung: String,
    reviewImage: String? = null,
    adminReply: String? = null,
    likeCount: Int = 0,
    onLikeClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFF0F0F0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = ten.first().toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ten,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = ngay,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "★★★★★",
                color = Color(0xFFFFC107),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = noiDung,
                fontSize = 19.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Bold
            )

            if (!reviewImage.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                AsyncImage(
                    model = reviewImage,
                    contentDescription = "Ảnh đánh giá",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            if (!adminReply.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFEAF3FF),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Phản hồi từ HoangShoe",
                        color = Color(0xFF064C8C),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = adminReply,
                        color = Color(0xFF333333),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .clickable {
                        onLikeClick()
                    }
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👍",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Hữu ích ($likeCount)",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

fun uploadReviewImageToFirebase(
    imageUri: Uri,
    onSuccess: (String) -> Unit,
    onError: (Exception) -> Unit
) {
    val fileName = "reviews/${UUID.randomUUID()}.jpg"
    val ref = FirebaseStorage.getInstance().reference.child(fileName)

    ref.putFile(imageUri)
        .addOnSuccessListener {
            ref.downloadUrl
                .addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString())
                }
                .addOnFailureListener { exception ->
                    onError(exception)
                }
        }
        .addOnFailureListener { exception ->
            onError(exception)
        }
}

@Composable
fun BangSize(
    tab: Int
) {

    val sizeNam = listOf(
        listOf("6", "5", "39", "24.5"),
        listOf("6.5", "5.5", "39.5", "25"),
        listOf("7", "6", "40", "25.25"),
        listOf("7.5", "6.5", "40.5", "25.5"),
        listOf("8", "7", "41.5", "26"),
        listOf("8.5", "7.5", "42", "26.5"),
        listOf("9", "8", "42.5", "27"),
        listOf("9.5", "8.5", "43.5", "27.5")
    )

    val sizeNu = listOf(
        listOf("5", "3", "35.5", "22"),
        listOf("5.5", "3.5", "36", "22.5"),
        listOf("6", "4", "37", "23"),
        listOf("6.5", "4.5", "37.5", "23.5"),
        listOf("7", "5", "38", "24"),
        listOf("7.5", "5.5", "39", "24.5"),
        listOf("8", "6", "39.5", "25"),
        listOf("8.5", "6.5", "40", "25.5")
    )

    val data = if (tab == 0) sizeNam else sizeNu

    Column {

        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9EEF5))
                .padding(vertical = 14.dp)
        ) {

            TableCell("US")
            TableCell("UK")
            TableCell("EU (VN)")
            TableCell("CM (Giày)")
        }

        LazyColumn {

            items(data) { row ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color(0xFFEAEAEA)
                        )
                        .padding(vertical = 16.dp)
                ) {

                    row.forEach {

                        TableCell(it)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Chiều dài bàn chân hãy đo từ gót chân đến ngón chân dài nhất và thường lấy nhỏ hơn chiều dài giày 1cm - 1.5cm.",
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun RowScope.TableCell(
    text: String
) {

    Box(
        modifier = Modifier
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

fun formatMoney(value: Float): String {
    return "%,.0f".format(value).replace(",", ".")
}

