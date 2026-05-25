package com.example.appbangiay.ui.address

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appbangiay.database.DiaChiDao
import com.example.appbangiay.model.DiaChi
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.foundation.BorderStroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhChonDiaChi(
    dao: DiaChiDao,
    quayLai: () -> Unit,
    onDiaChiSelected: ((DiaChi) -> Unit)? = null
) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val danhSach by dao.layTheoNguoiDung(uid).collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    var hienSheetThemDiaChi by remember { mutableStateOf(false) }
    var tenNguoiNhan by remember { mutableStateOf("") }
    var tinhThanh by remember { mutableStateOf("") }
    var diaChiChiTiet by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7FC))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                    text = "Sổ địa chỉ",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(48.dp))
            }

            if (danhSach.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(82.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Bạn chưa lưu địa chỉ nào",
                            color = Color.Gray,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(danhSach) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .clickable {
                                    onDiaChiSelected?.invoke(item)
                                },
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(
                                1.dp,
                                Color(0xFFE5E5E5)
                            ),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.tenNguoiNhan,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )

                                    Text(
                                        text = "${item.diaChiChiTiet}, ${item.tinhThanh}",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                var menuMoRong by remember { mutableStateOf(false) }

                                Box {
                                    IconButton(
                                        onClick = {
                                            menuMoRong = true
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.MoreVert,
                                            contentDescription = "Tùy chọn"
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = menuMoRong,
                                        onDismissRequest = {
                                            menuMoRong = false
                                        }
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = "Sửa",
                                                    fontWeight = FontWeight.Bold
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Default.Edit,
                                                    contentDescription = null
                                                )
                                            },
                                            onClick = {
                                                menuMoRong = false
                                                // Phần sửa địa chỉ làm sau
                                            }
                                        )

                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = "Xóa",
                                                    color = Color.Red,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = null,
                                                    tint = Color.Red
                                                )
                                            },
                                            onClick = {
                                                menuMoRong = false
                                                scope.launch {
                                                    dao.xoaDiaChi(item)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                hienSheetThemDiaChi = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(24.dp)
                .height(64.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF064C8C)
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = null)

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Thêm địa chỉ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    if (hienSheetThemDiaChi) {
        ModalBottomSheet(
            onDismissRequest = {
                hienSheetThemDiaChi = false
            },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .navigationBarsPadding()
            ) {
                Text(
                    text = "Thêm địa chỉ mới",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = tenNguoiNhan,
                    onValueChange = { tenNguoiNhan = it },
                    placeholder = {
                        Text("Tên người nhận")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = tinhThanh,
                    onValueChange = { tinhThanh = it },
                    placeholder = {
                        Text("Chọn Tỉnh/Thành phố")
                    },
                    trailingIcon = {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = diaChiChiTiet,
                    onValueChange = { diaChiChiTiet = it },
                    placeholder = {
                        Text("Địa chỉ (Số nhà, đường, ...")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        scope.launch {
                            dao.themDiaChi(
                                DiaChi(
                                    firebaseUid = uid,
                                    tenNguoiNhan = tenNguoiNhan,
                                    tinhThanh = tinhThanh,
                                    diaChiChiTiet = diaChiChiTiet
                                )
                            )

                            tenNguoiNhan = ""
                            tinhThanh = ""
                            diaChiChiTiet = ""
                            hienSheetThemDiaChi = false
                        }
                    },
                    enabled = tenNguoiNhan.isNotBlank()
                            && tinhThanh.isNotBlank()
                            && diaChiChiTiet.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF064C8C)
                    )
                ) {
                    Text(
                        text = "Lưu địa chỉ",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}