package com.example.appbangiay.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.appbangiay.ui.home.ManHinhTrangChu
import com.example.appbangiay.ui.chat.ManHinhChat
import com.example.appbangiay.ui.notification.ManHinhThongBao
import com.example.appbangiay.ui.profile.ManHinhToi
import com.example.appbangiay.ui.navigation.Screen
import com.example.appbangiay.ui.theme.MauXanhChinh
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import com.example.appbangiay.database.GioHangDao
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import com.google.firebase.auth.FirebaseAuth
import com.example.appbangiay.network.KetNoiServer
import androidx.compose.foundation.background

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Trang chủ")
    object Chat : BottomNavItem("chat", Icons.Default.Chat, "Chat")
    object Notification : BottomNavItem("notification", Icons.Default.Notifications, "Thông báo")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Tôi")
}

@Composable
fun MainScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToBrand: (String) -> Unit,
    onNavigateToCategory: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onRequireLogin: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToAddressBook: () -> Unit,
    onNavigateToFavorite: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToAccountSettings: () -> Unit,
    onNavigateToMyOrders: (String) -> Unit,
    isLoggedIn: Boolean,
    onNavigateToOrderDetail: (Int) -> Unit,
    gioHangDao: GioHangDao,
    moChatSanPhamNgay: Boolean = false,
    moThongBaoNgay: Boolean = false,
    chatProductId: Int? = null,
    chatProductName: String? = null,
    chatProductPrice: Int? = null,
    chatProductImage: String? = null
) {
    val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val soLuongGioHang by gioHangDao
        .layTongSoLuongGioHang(firebaseUid)
        .collectAsState(initial = 0)
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    var daXuLyMoChatSanPham by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(chatProductId) {
        if (
            moChatSanPhamNgay &&
            chatProductId != null &&
            !daXuLyMoChatSanPham
        ) {
            selectedItem = 1
            daXuLyMoChatSanPham = true
        }
    }
    var daXuLyMoThongBao by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(moThongBaoNgay) {
        if (moThongBaoNgay && !daXuLyMoThongBao) {
            selectedItem = 2
            daXuLyMoThongBao = true
        }
    }
    var soThongBaoChuaDoc by remember {
        mutableStateOf(0)
    }
    val primaryBlue = MauXanhChinh

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Chat,
        BottomNavItem.Notification,
        BottomNavItem.Profile
    )

    suspend fun taiSoThongBao() {
        try {

            val uid = FirebaseAuth
                .getInstance()
                .currentUser
                ?.uid ?: ""

            val response = KetNoiServer.api
                .laySoThongBaoChuaDoc(uid)

            soThongBaoChuaDoc = response.unread_count

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(selectedItem) {

        if (isLoggedIn) {
            if (isLoggedIn) {
                taiSoThongBao()
            }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = primaryBlue) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {

                            Box {

                                Icon(
                                    item.icon,
                                    contentDescription = item.label,
                                    modifier = Modifier.size(28.dp)
                                )

                                if (
                                    item == BottomNavItem.Notification &&
                                    soThongBaoChuaDoc > 0
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset {
                                                IntOffset(10, -10)
                                            }
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .background(Color.Red),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        Text(
                                            text = if (soThongBaoChuaDoc > 9) "9+" else soThongBaoChuaDoc.toString(),
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        selected = selectedItem == index,
                        onClick = {
                            if (
                                item == BottomNavItem.Chat ||
                                item == BottomNavItem.Notification ||
                                item == BottomNavItem.Profile
                            ) {
                                if (isLoggedIn) {
                                    selectedItem = index

                                    if (item == BottomNavItem.Notification) {
                                        soThongBaoChuaDoc = 0
                                    }
                                } else {
                                    onRequireLogin()
                                }
                            } else {
                                selectedItem = index
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,

                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                            unselectedTextColor = Color.White.copy(alpha = 0.7f),

                            indicatorColor = Color.White.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                0 -> ManHinhTrangChu(
                    chuyenSangChiTiet = onNavigateToDetail,
                    chuyenSangThuongHieu = onNavigateToBrand,
                    chuyenSangDanhMuc = onNavigateToCategory,
                    chuyenSangTimKiem = onNavigateToSearch,
                    chuyenSangGioHang = onNavigateToCart,
                    gioHangDao = gioHangDao
                )
                1 -> ManHinhChat(
                    soLuongGioHang = soLuongGioHang,
                    chuyenSangGioHang = onNavigateToCart,
                    chuyenSangChiTiet = onNavigateToDetail,
                    sanPhamId = if (selectedItem == 1) chatProductId else null,
                    tenSanPham = if (selectedItem == 1) chatProductName else null,
                    giaSanPham = if (selectedItem == 1) chatProductPrice else null,
                    anhSanPham = if (selectedItem == 1) chatProductImage else null
                )
                2 -> ManHinhThongBao(
                    onOpenOrderDetail = { orderId ->
                        onNavigateToOrderDetail(orderId)
                    }
                )
                3 -> ManHinhToi(
                    onLogoutSuccess = onRequireLogin,
                    onNavigateToAddressBook = onNavigateToAddressBook,
                    onNavigateToFavorite = onNavigateToFavorite,
                    onNavigateToAbout = onNavigateToAbout,
                    onNavigateToSupport = onNavigateToSupport,
                    onNavigateToAccountSettings = onNavigateToAccountSettings,
                    onNavigateToMyOrders = onNavigateToMyOrders
                )
            }
        }
    }
}