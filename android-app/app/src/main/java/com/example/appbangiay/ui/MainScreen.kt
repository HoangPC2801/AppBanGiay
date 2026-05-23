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
    isLoggedIn: Boolean
) {
    var selectedItem by rememberSaveable { mutableStateOf(0) }
    val primaryBlue = MauXanhChinh

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Chat,
        BottomNavItem.Notification,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = primaryBlue) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(28.dp)
                            )
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
                    chuyenSangGioHang = onNavigateToCart
                )
                1 -> ManHinhChat()
                2 -> ManHinhThongBao()
                3 -> ManHinhToi(
                    onLogoutSuccess = onRequireLogin,
                    onNavigateToAddressBook = onNavigateToAddressBook,
                    onNavigateToFavorite = onNavigateToFavorite,
                    onNavigateToAbout = onNavigateToAbout,
                    onNavigateToSupport = onNavigateToSupport,
                    onNavigateToAccountSettings = onNavigateToAccountSettings
                )
            }
        }
    }
}