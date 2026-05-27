package com.example.appbangiay.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appbangiay.model.Giay
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

enum class SapXepSanPham {
    MOI_NHAT,
    GIA_THAP_DEN_CAO,
    GIA_CAO_XUONG_THAP,
    TEN_A_Z
}

data class BoLocSanPhamState(
    val sapXep: SapXepSanPham = SapXepSanPham.MOI_NHAT,
    val giaTu: Float = 0f,
    val giaDen: Float = 20_000_000f,
    val size: String? = null
)

fun locVaSapXepSanPham(
    danhSach: List<Giay>,
    boLoc: BoLocSanPhamState
): List<Giay> {
    return danhSach
        .filter { giay ->
            giay.giaTien >= boLoc.giaTu &&
                    giay.giaTien <= boLoc.giaDen &&
                    (
                            boLoc.size == null ||
                                    giay.variants.any { it.size == boLoc.size }
                            )
        }
        .let { list ->
            when (boLoc.sapXep) {
                SapXepSanPham.MOI_NHAT -> list.sortedByDescending { it.maGiay }
                SapXepSanPham.GIA_THAP_DEN_CAO -> list.sortedBy { it.giaTien }
                SapXepSanPham.GIA_CAO_XUONG_THAP -> list.sortedByDescending { it.giaTien }
                SapXepSanPham.TEN_A_Z -> list.sortedBy { it.tenGiay }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BoLocSanPhamBottomSheet(
    boLocHienTai: BoLocSanPhamState,
    onDismiss: () -> Unit,
    onApDung: (BoLocSanPhamState) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var boLocTam by remember { mutableStateOf(boLocHienTai) }

    val danhSachSize = listOf(
        "36", "37", "38", "39", "40",
        "41", "42", "43", "44", "45",
        "37.5", "38.5", "39.5", "40.5"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Bộ lọc tìm kiếm",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = {
                        boLocTam = BoLocSanPhamState()
                    }
                ) {
                    Text(
                        text = "Thiết lập lại",
                        color = Color(0xFFE53935),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Sắp xếp theo",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChipItem("Mới nhất", boLocTam.sapXep == SapXepSanPham.MOI_NHAT) {
                    boLocTam = boLocTam.copy(sapXep = SapXepSanPham.MOI_NHAT)
                }

                FilterChipItem("Giá thấp đến cao", boLocTam.sapXep == SapXepSanPham.GIA_THAP_DEN_CAO) {
                    boLocTam = boLocTam.copy(sapXep = SapXepSanPham.GIA_THAP_DEN_CAO)
                }

                FilterChipItem("Giá cao xuống thấp", boLocTam.sapXep == SapXepSanPham.GIA_CAO_XUONG_THAP) {
                    boLocTam = boLocTam.copy(sapXep = SapXepSanPham.GIA_CAO_XUONG_THAP)
                }

                FilterChipItem("Tên A-Z", boLocTam.sapXep == SapXepSanPham.TEN_A_Z) {
                    boLocTam = boLocTam.copy(sapXep = SapXepSanPham.TEN_A_Z)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Khoảng giá (VNĐ)",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatMoneyFilter(boLocTam.giaTu), fontWeight = FontWeight.Bold)
                Text(formatMoneyFilter(boLocTam.giaDen), fontWeight = FontWeight.Bold)
            }

            RangeSlider(
                value = boLocTam.giaTu..boLocTam.giaDen,
                onValueChange = {
                    boLocTam = boLocTam.copy(
                        giaTu = it.start,
                        giaDen = it.endInclusive
                    )
                },
                valueRange = 0f..20_000_000f,
                steps = 19
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Chọn Size",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                danhSachSize.forEach { size ->
                    FilterChipItem(
                        text = size,
                        selected = boLocTam.size == size
                    ) {
                        boLocTam = boLocTam.copy(
                            size = if (boLocTam.size == size) null else size
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    onApDung(boLocTam)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0D4F8B)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "ÁP DỤNG",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FilterChipItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) Color(0xFFEAF3FF) else Color.White,
            contentColor = if (selected) Color(0xFF0D4F8B) else Color.Black
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatMoneyFilter(value: Float): String {
    return "%,.0f".format(value).replace(",", ".")
}