package com.example.appbangiay.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ManHinhToi() {
    val primaryBlue = Color(0xFF67A8F8)
    val bgGray = Color(0xFFEFEFEF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGray)
            .verticalScroll(rememberScrollState())
    ) {
        // --- 1. HEADER (Nền xanh) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(primaryBlue)
                .padding(top = 40.dp, bottom = 60.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFF9EC7FC), CircleShape)
                        .background(primaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "PH",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Thông tin User (Phạm Công Hoàng)
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Phạm Công Hoàng",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Badge Khách hàng thân quen
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Khách hàng thân quen",
                            color = Color(0xFF4CAF50),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "hoang@gmail.com",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }

                // Nút Chỉnh sửa
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit Profile",
                    tint = Color(0xFF9EC7FC),
                    modifier = Modifier.size(32.dp).clickable { /* Xử lý sửa hồ sơ */ }
                )
            }
        }

        // --- 2. NỘI DUNG CHÍNH (Đẩy lên trên để đè vào nền xanh) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-30).dp) // Kéo ngược lên 30dp
                .padding(horizontal = 16.dp)
        ) {
            // Khối Đơn hàng của tôi
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Đơn hàng của tôi", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Xem lịch sử >", fontSize = 12.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OrderStatusItem("Chờ xử lý", Icons.Outlined.Receipt)
                        OrderStatusItem("Đang xử lý", Icons.Outlined.Inventory2)
                        OrderStatusItem("Đang giao", Icons.Outlined.LocalShipping)
                        OrderStatusItem("Hoàn thành", Icons.Outlined.CheckCircleOutline)
                        OrderStatusItem("Hoàn / Hủy", Icons.Outlined.AssignmentReturn)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Khối Menu 1
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    MenuItemRow(icon = Icons.Outlined.LocationOn, iconColor = Color(0xFFFF9800), text = "Sổ địa chỉ")
                    HorizontalDivider(color = bgGray, thickness = 1.dp, modifier = Modifier.padding(start = 50.dp))
                    MenuItemRow(icon = Icons.Outlined.CardGiftcard, iconColor = Color(0xFF4CAF50), text = "Kho Voucher")
                    HorizontalDivider(color = bgGray, thickness = 1.dp, modifier = Modifier.padding(start = 50.dp))
                    MenuItemRow(icon = Icons.Outlined.FavoriteBorder, iconColor = Color(0xFFF44336), text = "Sản phẩm yêu thích")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Khối Menu 2
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    MenuItemRow(icon = Icons.Outlined.Storefront, iconColor = Color(0xFF424242), text = "Về HoangShoe")
                    HorizontalDivider(color = bgGray, thickness = 1.dp, modifier = Modifier.padding(start = 50.dp))
                    MenuItemRow(icon = Icons.Outlined.HelpOutline, iconColor = Color(0xFF424242), text = "Trung tâm hỗ trợ")
                    HorizontalDivider(color = bgGray, thickness = 1.dp, modifier = Modifier.padding(start = 50.dp))
                    MenuItemRow(icon = Icons.Outlined.Settings, iconColor = Color(0xFF424242), text = "Cài đặt tài khoản")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nút Đăng xuất
            OutlinedButton(
                onClick = { /* Xử lý đăng xuất */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red)
            ) {
                Text(text = "Đăng xuất", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(30.dp)) // Đệm dưới cùng tránh bị thanh điều hướng đè lên
        }
    }
}

// Component dùng chung cho các Icon trạng thái đơn hàng
@Composable
fun OrderStatusItem(title: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { /* Xử lý click */ }
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFF67A8F8), RoundedCornerShape(12.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = Color.Black, modifier = Modifier.size(26.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = title, fontSize = 11.sp, color = Color.Black)
    }
}

// Component dùng chung cho các dòng Menu
@Composable
fun MenuItemRow(icon: ImageVector, iconColor: Color, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Xử lý click */ }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = text, tint = iconColor, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = text, fontSize = 15.sp, color = Color.Black, modifier = Modifier.weight(1f))

        Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}