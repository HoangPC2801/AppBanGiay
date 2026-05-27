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
import com.example.appbangiay.ui.theme.MauXanhChinh
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.*
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.google.firebase.auth.UserProfileChangeRequest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import com.google.firebase.storage.FirebaseStorage
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.PhotoCamera

@Composable
fun ManHinhToi(
    onLogoutSuccess: () -> Unit,
    onNavigateToAddressBook: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToFavorite: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToAccountSettings: () -> Unit
) {
    val primaryBlue = MauXanhChinh
    val bgGray = Color(0xFFEFEFEF)
    var hienDialogDangXuat by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser

    var hienCapNhatHoSo by remember { mutableStateOf(false) }
    var tenHienThi by remember {
        mutableStateOf(user?.displayName ?: "Phạm Công Hoàng")
    }

    val emailHienThi = user?.email ?: "Chưa có email"

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
                    val avatarUrl = FirebaseAuth
                        .getInstance()
                        .currentUser
                        ?.photoUrl
                        ?.toString()

                    if (avatarUrl != null) {

                        AsyncImage(
                            model = avatarUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                    } else {

                        Text(
                            text = layChuCaiDau(tenHienThi),
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Thông tin User (Phạm Công Hoàng)
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = tenHienThi,
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
                        text = emailHienThi,
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }

                // Nút Chỉnh sửa
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit Profile",
                    tint = Color(0xFF9EC7FC),
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            hienCapNhatHoSo = true
                        }
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
                    MenuItemRow(
                        icon = Icons.Outlined.LocationOn,
                        iconColor = Color(0xFFFF9800),
                        text = "Sổ địa chỉ",
                        onClick = onNavigateToAddressBook
                    )
                    HorizontalDivider(color = bgGray, thickness = 1.dp, modifier = Modifier.padding(start = 50.dp))
                    MenuItemRow(icon = Icons.Outlined.CardGiftcard, iconColor = Color(0xFF4CAF50), text = "Kho Voucher")
                    HorizontalDivider(color = bgGray, thickness = 1.dp, modifier = Modifier.padding(start = 50.dp))
                    MenuItemRow(icon = Icons.Outlined.FavoriteBorder, iconColor = Color(0xFFF44336), text = "Sản phẩm yêu thích", onClick = onNavigateToFavorite)
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
                    MenuItemRow(icon = Icons.Outlined.Storefront, iconColor = Color(0xFF424242), text = "Về HoangShoes", onClick = onNavigateToAbout)
                    HorizontalDivider(color = bgGray, thickness = 1.dp, modifier = Modifier.padding(start = 50.dp))
                    MenuItemRow(icon = Icons.Outlined.HelpOutline, iconColor = Color(0xFF424242), text = "Trung tâm hỗ trợ",  onClick = onNavigateToSupport)
                    HorizontalDivider(color = bgGray, thickness = 1.dp, modifier = Modifier.padding(start = 50.dp))
                    MenuItemRow(icon = Icons.Outlined.Settings, iconColor = Color(0xFF424242), text = "Cài đặt tài khoản", onClick = onNavigateToAccountSettings)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nút Đăng xuất
            OutlinedButton(
                onClick = { hienDialogDangXuat = true },
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

        if (hienDialogDangXuat) {
            AlertDialog(
                onDismissRequest = {
                    hienDialogDangXuat = false
                },

                containerColor = Color.White, // màu nền dialog

                icon = {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(48.dp)
                    )
                },
                title = {
                    Text(
                        text = "Đăng xuất",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Bạn có chắc chắn muốn đăng xuất?",
                        fontWeight = FontWeight.Bold
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            hienDialogDangXuat = false
                            onLogoutSuccess()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF3B3B)
                        )
                    ) {
                        Text("Đăng xuất")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            hienDialogDangXuat = false
                        }
                    ) {
                        Text(
                            text = "Ở lại",
                            color = Color(0xFF064C8C),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }

        if (hienCapNhatHoSo) {
            CapNhatHoSoSheet(
                tenHienTai = tenHienThi,
                email = emailHienThi,
                onDismiss = {
                    hienCapNhatHoSo = false
                },
                onSaved = { tenMoi ->
                    tenHienThi = tenMoi
                    hienCapNhatHoSo = false
                    Toast.makeText(context, "Cập nhật hồ sơ thành công", Toast.LENGTH_SHORT).show()
                }
            )
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
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

// Component dùng chung cho các dòng Menu
@Composable
fun MenuItemRow(
    icon: ImageVector,
    iconColor: Color,
    text: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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

        Text(text = text, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.weight(1f))

        Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CapNhatHoSoSheet(
    tenHienTai: String,
    email: String,
    onDismiss: () -> Unit,
    onSaved: (String) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var hoTen by remember { mutableStateOf(tenHienTai) }
    var matKhauMoi by remember { mutableStateOf("") }
    var nhapLaiMatKhau by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Cập nhật hồ sơ",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D2433)
            )

            Spacer(modifier = Modifier.height(28.dp))

            val user = FirebaseAuth.getInstance().currentUser

            var avatarUri by remember {
                mutableStateOf(user?.photoUrl?.toString())
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->

                if (uri != null) {

                    val storageRef = FirebaseStorage
                        .getInstance()
                        .reference
                        .child("avatars/${user?.uid}.jpg")

                    storageRef.putFile(uri)
                        .continueWithTask { task ->
                            if (!task.isSuccessful) {
                                throw task.exception ?: Exception("Upload failed")
                            }

                            storageRef.downloadUrl
                        }
                        .addOnSuccessListener { downloadUri ->

                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setPhotoUri(downloadUri)
                                .build()

                            user?.updateProfile(profileUpdates)
                                ?.addOnSuccessListener {
                                    avatarUri = downloadUri.toString()

                                    Toast.makeText(
                                        context,
                                        "Đổi avatar thành công",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                }
            }

            Box(
                modifier = Modifier.size(130.dp),
                contentAlignment = Alignment.BottomEnd
            ) {

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MauXanhChinh)
                        .border(4.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {

                    if (avatarUri != null) {

                        AsyncImage(
                            model = avatarUri,
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                    } else {

                        Text(
                            text = layChuCaiDau(hoTen),
                            color = Color.White,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MauXanhChinh)
                        .clickable {
                            launcher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Đổi avatar",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Thông tin cá nhân",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = hoTen,
                onValueChange = { hoTen = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Họ và tên") },
                leadingIcon = {
                    Icon(Icons.Outlined.Person, contentDescription = null)
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Outlined.Email, contentDescription = null)
                },
                trailingIcon = {
                    Icon(Icons.Outlined.Lock, contentDescription = null)
                },
                enabled = false,
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Đổi mật khẩu (Bỏ trống nếu không đổi)",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = matKhauMoi,
                onValueChange = { matKhauMoi = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Mật khẩu mới") },
                leadingIcon = {
                    Icon(Icons.Outlined.Lock, contentDescription = null)
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = nhapLaiMatKhau,
                onValueChange = { nhapLaiMatKhau = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nhập lại mật khẩu mới") },
                leadingIcon = {
                    Icon(Icons.Outlined.LockReset, contentDescription = null)
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val user = FirebaseAuth.getInstance().currentUser

                    if (hoTen.isBlank()) {
                        Toast.makeText(context, "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (matKhauMoi.isNotBlank()) {
                        if (matKhauMoi.length < 6) {
                            Toast.makeText(context, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (matKhauMoi != nhapLaiMatKhau) {
                            Toast.makeText(context, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                    }

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(hoTen)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnSuccessListener {
                            if (matKhauMoi.isNotBlank()) {
                                user.updatePassword(matKhauMoi)
                                    .addOnSuccessListener {
                                        onSaved(hoTen)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            "Không đổi được mật khẩu. Hãy đăng nhập lại rồi thử tiếp",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            } else {
                                onSaved(hoTen)
                            }
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MauXanhChinh)
            ) {
                Text(
                    text = "Lưu thay đổi",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

fun layChuCaiDau(ten: String): String {
    val parts = ten.trim().split(" ").filter { it.isNotBlank() }

    return when {
        parts.size >= 2 -> "${parts.first().first()}${parts.last().first()}".uppercase()
        parts.size == 1 -> parts.first().take(2).uppercase()
        else -> "U"
    }
}