package com.example.appbangiay.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.appbangiay.model.GioHang
import com.example.appbangiay.viewmodel.ThanhToanViewModel
import androidx.compose.foundation.clickable
import android.net.Uri
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.CheckCircle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhThanhToan(
    viewModel: ThanhToanViewModel,
    quayVeTrangChu: () -> Unit,
    chonDiaChi: () -> Unit,
    diaChiDaChon: String = "",
    sanPhamMuaNgay: GioHang? = null
) {
    var diaChi by remember { mutableStateOf("") }
    LaunchedEffect(diaChiDaChon) {
        if (diaChiDaChon.isNotBlank()) {
            diaChi = diaChiDaChon
        }
    }



    var ghiChu by remember { mutableStateOf("") }
    var phuongThuc by remember { mutableStateOf("COD") }

    val danhSachGioHang by viewModel.danhSachGioHang.collectAsState(initial = emptyList())

    val danhSach = if (sanPhamMuaNgay != null) {
        listOf(sanPhamMuaNgay)
    } else {
        danhSachGioHang
    }

    val tamTinh = danhSach.sumOf {
        (it.giaTien * it.soLuong).toDouble()
    }.toFloat()

    val phiVanChuyen = 0f

    val tongThanhToan = tamTinh + phiVanChuyen

    val maDonHang by viewModel.maDonHang.collectAsState()

    var hienManHinhQR by remember { mutableStateOf(false) }
    val bankId = "MB"
    val soTaiKhoan = "280112349999"
    val tenTaiKhoan = "PHAM CONG HOANG"
    val noiDungCK = "THANH TOAN DON ${maDonHang ?: ""}"

    val vietQrUrl =
        "https://img.vietqr.io/image/$bankId-$soTaiKhoan-compact2.png" +
                "?amount=${tongThanhToan.toInt()}" +
                "&addInfo=${Uri.encode(noiDungCK)}" +
                "&accountName=${Uri.encode(tenTaiKhoan)}"

    LaunchedEffect(maDonHang) {
        if (phuongThuc == "BANK_TRANSFER" && maDonHang != null) {
            hienManHinhQR = true
        }
    }

    val laMuaNgay = sanPhamMuaNgay != null

    var hienDialogThanhCong by remember { mutableStateOf(false) }
    val trangThaiDatHang by viewModel.trangThaiDatHang.collectAsState()

    LaunchedEffect(trangThaiDatHang) {
        if (
            trangThaiDatHang == "Đặt hàng thành công" &&
            phuongThuc != "BANK_TRANSFER"
        ) {
            hienDialogThanhCong = true
        }
    }

    if (hienDialogThanhCong) {

        AlertDialog(
            onDismissRequest = {},
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White,

            title = null,

            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(
                                Color(0xFFE8F5E9),
                                shape = RoundedCornerShape(50.dp)
                            ),

                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(70.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Đặt hàng thành công",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Đơn hàng của bạn đã được gửi đến HoangShoes để xử lý.",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            },

            confirmButton = {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {

                    Button(
                        onClick = {
                            hienDialogThanhCong = false
                            quayVeTrangChu()
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),

                        shape = RoundedCornerShape(14.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF064C8C)
                        )
                    ) {

                        Text(
                            text = "TIẾP TỤC",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        )
    }


    if (hienManHinhQR) {
        ManHinhQuetMaThanhToan(
            maDonTam = maDonHang ?: 0,
            tongTien = tongThanhToan,
            qrUrl = vietQrUrl,
            noiDungCK = noiDungCK,
            onToiDaThanhToan = {
                hienDialogThanhCong = true
            },
            onTiepTucMuaSam = {
                quayVeTrangChu()
            }
        )
        return
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(76.dp)
                    .background(Color.White)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = quayVeTrangChu) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                }

                Text(
                    text = "Thanh toán",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.width(48.dp))
            }
        },
        bottomBar = {
            Button(
                onClick = {
                    if (diaChi.isBlank() || diaChi == "Chưa có địa chỉ") return@Button

                    if (phuongThuc == "BANK_TRANSFER") {
                        viewModel.thucHienDatHang(
                            maNguoiDung = 1,
                            diaChi = diaChi,
                            phuongThucThanhToan = "BANK_TRANSFER",
                            danhSachDatHang = danhSach,
                            ghiChu = ghiChu,
                            xoaGioHangSauKhiDat = !laMuaNgay
                        )
                        return@Button
                    }

                    viewModel.thucHienDatHang(
                        maNguoiDung = 1,
                        diaChi = diaChi,
                        phuongThucThanhToan = phuongThuc,
                        danhSachDatHang = danhSach,
                        ghiChu = ghiChu,
                        xoaGioHangSauKhiDat = !laMuaNgay
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
                    .height(58.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF064C8C),
                    disabledContainerColor = Color.LightGray
                ),
                enabled = danhSach.isNotEmpty()
                        && diaChi.isNotBlank()
                        && diaChi != "Chưa có địa chỉ"
                        && trangThaiDatHang != "Đang xử lý..."
            ) {
                Text(
                    text = if (trangThaiDatHang == "Đang xử lý...")
                        "ĐANG XỬ LÝ..."
                    else
                        "ĐẶT HÀNG NGAY",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF7F7FC)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text("Địa chỉ nhận hàng", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            chonDiaChi()
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF064C8C))

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {

                            Text(
                                text = if (diaChi.isBlank())
                                    "Chưa có địa chỉ nhận hàng"
                                else
                                    diaChi,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (diaChi.isBlank())
                                    "Vui lòng chọn hoặc thêm địa chỉ mới"
                                else
                                    "Nhấn để thay đổi địa chỉ",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }

                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                    }
                }
            }

            item {
                Text("Sản phẩm", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        danhSach.forEach { item ->
                            CheckoutProductItem(item)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }

            item {
                Text("Vận chuyển", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color(0xFF064C8C))

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Vận chuyển", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Nhận hàng sau 2-4 ngày", color = Color.Gray, fontWeight = FontWeight.Bold)
                        }

                        Text("Miễn phí", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Text("Thanh toán", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        PaymentOption(
                            title = "Thanh toán khi nhận hàng",
                            selected = phuongThuc == "COD",
                            icon = {
                                Icon(Icons.Default.Money, contentDescription = null, tint = Color(0xFF4CAF50))
                            },
                            onClick = {
                                phuongThuc = "COD"
                            }
                        )

                        PaymentOption(
                            title = "Chuyển khoản ngân hàng",
                            selected = phuongThuc == "BANK_TRANSFER",
                            icon = {
                                Icon(Icons.Default.QrCode2, contentDescription = null, tint = Color(0xFF03A9F4))
                            },
                            onClick = {
                                phuongThuc = "BANK_TRANSFER"
                            }
                        )

                        if (phuongThuc == "BANK_TRANSFER") {
                            Text(
                                text = """
                                Mã QR thanh toán sẽ được hiển thị
                                sau khi bạn bấm ĐẶT HÀNG.
                                """.trimIndent(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFEFF8FF), RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                color = Color(0xFF333333),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        PaymentOption(
                            title = "Thanh toán qua thẻ VISA",
                            selected = phuongThuc == "VISA",
                            icon = {
                                Text("VISA", color = Color(0xFF064C8C), fontWeight = FontWeight.Bold)
                            },
                            onClick = {
                                phuongThuc = "VISA"
                            }
                        )

                        if (phuongThuc == "VISA") {
                            Text(
                                text = "Thanh toán VISA đang ở chế độ giả lập. Khi bấm ĐẶT HÀNG NGAY, hệ thống sẽ giả lập thanh toán thành công.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFFF8E1), RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                color = Color(0xFF333333),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item {
                Text("Ghi chú", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = ghiChu,
                    onValueChange = { ghiChu = it },
                    leadingIcon = {
                        Icon(Icons.Default.NoteAlt, contentDescription = null)
                    },
                    placeholder = {
                        Text("Nhập ghi chú cho HoangShoes...")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SummaryRow("Tạm tính", "${formatMoneyCheckout(tamTinh)}đ")
                        SummaryRow("Phí vận chuyển", "Miễn phí")

                        Spacer(modifier = Modifier.height(10.dp))

                        SummaryRow(
                            "Tổng thanh toán",
                            "${formatMoneyCheckout(tongThanhToan)}đ",
                            big = true
                        )
                    }
                }
            }
        }


    }
}

@Composable
fun CheckoutProductItem(item: GioHang) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.hinhAnh,
            contentDescription = item.tenGiay,
            modifier = Modifier.size(72.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.tenGiay,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Màu sắc: ${item.mauSac ?: "Chưa chọn"}",
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )

            Text(
                text = "Chọn size: ${item.size ?: "Chưa chọn"}",
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )

            Text(
                text = "${formatMoneyCheckout(item.giaTien)}đ",
                color = Color(0xFF064C8C),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Text(
            text = "x${item.soLuong}",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun PaymentOption(
    title: String,
    selected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        icon()
    }
}

@Composable
fun SummaryRow(
    label: String,
    value: String,
    big: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = if (big) Color.Black else Color.Gray,
            fontSize = if (big) 18.sp else 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            color = if (big) Color(0xFF064C8C) else Color.Black,
            fontSize = if (big) 22.sp else 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ManHinhQuetMaThanhToan(
    maDonTam: Int,
    tongTien: Float,
    qrUrl: String,
    noiDungCK: String,
    onToiDaThanhToan: () -> Unit,
    onTiepTucMuaSam: () -> Unit
) {
    var daBamThanhToan by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50)),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", color = Color.White, fontSize = 58.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = "Đặt hàng thành công!",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF222222)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Mã đơn hàng: #$maDonTam",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(34.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                border = BorderStroke(1.dp, Color(0xFF0D4F8B)),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "QUÉT MÃ ĐỂ THANH TOÁN",
                        color = Color(0xFF064C8C),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "${formatMoneyCheckout(tongTien)}đ",
                        color = Color(0xFFE53935),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AsyncImage(
                        model = qrUrl,
                        contentDescription = "VietQR",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(330.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Nội dung chuyển khoản:",
                        color = Color.Gray,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF3F3F3), RoundedCornerShape(10.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = noiDungCK,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF222222)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Sau khi chuyển khoản, hãy bấm “Tôi đã thanh toán”. HoangShoes sẽ xác nhận thanh toán trên hệ thống.",
                color = Color.Gray,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = {
                    if (!daBamThanhToan) {
                        daBamThanhToan = true
                        onToiDaThanhToan()
                    }
                },
                enabled = !daBamThanhToan,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF064C8C),
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Text(
                    text = if (daBamThanhToan) "ĐANG TẠO ĐƠN..." else "TÔI ĐÃ THANH TOÁN",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onTiepTucMuaSam,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("TIẾP TỤC MUA SẮM", fontWeight = FontWeight.Bold)
            }
        }
    }
}

fun formatMoneyCheckout(value: Float): String {
    return "%,.0f".format(value).replace(",", ".")
}
