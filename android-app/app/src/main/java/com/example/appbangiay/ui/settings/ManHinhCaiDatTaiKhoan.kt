package com.example.appbangiay.ui.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appbangiay.ui.theme.MauXanhChinh
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.BorderStroke

@Composable
fun ManHinhCaiDatTaiKhoan(
    quayLai: () -> Unit,
    veDangNhap: () -> Unit
) {
    val context = LocalContext.current

    var thongBaoDonHang by remember { mutableStateOf(true) }
    var thongBaoKhuyenMai by remember { mutableStateOf(true) }
    var hienDialogXoa by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7FC))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(76.dp)
                .background(Color.White)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = quayLai) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
            }

            Text(
                text = "Cài đặt tài khoản",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(48.dp))
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Thông báo", color = Color.Gray, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            CardSetting {
                SettingSwitchRow(
                    title = "Cập nhật đơn hàng",
                    subtitle = "Thông báo trạng thái đơn hàng, vận chuyển",
                    checked = thongBaoDonHang,
                    onCheckedChange = { thongBaoDonHang = it }
                )

                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                SettingSwitchRow(
                    title = "Khuyến mãi & Ưu đãi",
                    subtitle = "Nhận tin tức giảm giá, voucher mới",
                    checked = thongBaoKhuyenMai,
                    onCheckedChange = { thongBaoKhuyenMai = it }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text("Thông tin pháp lý", color = Color.Gray, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            CardSetting {
                SettingMenuRow(
                    icon = Icons.Outlined.Security,
                    title = "Chính sách bảo mật"
                )

                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                SettingMenuRow(
                    icon = Icons.Outlined.Description,
                    title = "Điều khoản sử dụng"
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text("Vùng nguy hiểm", color = Color.Gray, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { hienDialogXoa = true },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    1.dp,
                    Color(0xFFE5E5E5)
                ),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp)
                        .padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(34.dp)
                    )

                    Spacer(modifier = Modifier.width(18.dp))

                    Text(
                        text = "Yêu cầu xóa tài khoản",
                        color = Color(0xFFE53935),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "HoangShoe v1.2.1",
                color = Color.LightGray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }

    if (hienDialogXoa) {
        AlertDialog(
            onDismissRequest = { hienDialogXoa = false },
            containerColor = Color.White,
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(50.dp)
                )
            },
            title = {
                Text("Xóa tài khoản?", fontWeight = FontWeight.Bold, fontSize = 26.sp)
            },
            text = {
                Text(
                    text = "Hành động này không thể hoàn tác. Mọi dữ liệu về đơn hàng và thông tin cá nhân của bạn sẽ bị xóa vĩnh viễn khỏi hệ thống.",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    OutlinedButton(
                        onClick = {
                            hienDialogXoa = false
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFDADADA))
                    ) {
                        Text(
                            text = "Hủy bỏ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1
                        )
                    }

                    Button(
                        onClick = {
                            val user = FirebaseAuth.getInstance().currentUser

                            if (user == null) {
                                veDangNhap()
                                return@Button
                            }

                            user.delete()
                                .addOnSuccessListener {
                                    FirebaseAuth.getInstance().signOut()
                                    Toast.makeText(context, "Đã xóa tài khoản", Toast.LENGTH_SHORT).show()
                                    veDangNhap()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Cần đăng nhập lại trước khi xóa tài khoản",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF3B3B)
                        )
                    ) {
                        Text(
                            text = "Xác nhận",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1
                        )
                    }
                }
            },

            dismissButton = {}
        )
    }
}

@Composable
fun CardSetting(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFE5E5E5)
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(content = content)
    }
}

@Composable
fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(94.dp)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, color = Color.Gray, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MauXanhChinh,
                checkedTrackColor = MauXanhChinh.copy(alpha = 0.25f)
            )
        )
    }
}

@Composable
fun SettingMenuRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(30.dp))

        Spacer(modifier = Modifier.width(18.dp))

        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))

        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
}