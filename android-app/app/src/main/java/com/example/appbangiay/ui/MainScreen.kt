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

// Khai báo các mục ở thanh điều hướng dưới
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Trang chủ")
    object Chat : BottomNavItem("chat", Icons.Default.Chat, "Chat")
    object Notification : BottomNavItem("notification", Icons.Default.Notifications, "Thông báo")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Tôi")
}

@Composable
fun MainScreen(onNavigateToDetail: (Int) -> Unit) {
    var selectedItem by remember { mutableStateOf(0) }
    val primaryBlue = Color(0xFF64A5FF)

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
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            unselectedIconColor = Color(0xFF7A6F6F),
                            unselectedTextColor = Color(0xFF7A6F6F),
                            indicatorColor = Color(0xFF5A9CFA)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // Nội dung thay đổi tùy theo tab được chọn
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                0 -> ManHinhTrangChu(chuyenSangChiTiet = onNavigateToDetail)
                1 -> ManHinhChat()
                2 -> ManHinhThongBao()
                3 -> Text("Màn hình Cá nhân đang phát triển...")
            }
        }
    }
}