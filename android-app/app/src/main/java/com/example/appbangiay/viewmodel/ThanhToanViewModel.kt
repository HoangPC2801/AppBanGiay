package com.example.appbangiay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbangiay.database.GioHangDao
import com.example.appbangiay.model.ChiTietMonHang
import com.example.appbangiay.model.YeuCauDatHang
import com.example.appbangiay.network.KetNoiServer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ThanhToanViewModel(private val dao: GioHangDao) : ViewModel() {

    // Lấy danh sách realtime từ Room DB
    val danhSachGioHang = dao.layDanhSachGioHang()

    private val _trangThaiDatHang = MutableStateFlow<String>("Chưa đặt")
    val trangThaiDatHang: StateFlow<String> = _trangThaiDatHang

    fun thucHienDatHang(maNguoiDung: Int, diaChi: String) {
        viewModelScope.launch {
            _trangThaiDatHang.value = "Đang xử lý..."
            try {
                // 1. Lấy dữ liệu giỏ hàng hiện tại
                val danhSachHienTai = danhSachGioHang.first()
                if (danhSachHienTai.isEmpty()) {
                    _trangThaiDatHang.value = "Giỏ hàng trống"
                    return@launch
                }

                // 2. Tính tổng tiền
                var tongTien = 0f
                val dsChiTiet = mutableListOf<ChiTietMonHang>()

                for (monHang in danhSachHienTai) {
                    tongTien += monHang.giaTien * monHang.soLuong
                    dsChiTiet.add(
                        ChiTietMonHang(
                            maGiay = monHang.maGiay,
                            soLuong = monHang.soLuong,
                            giaTien = monHang.giaTien
                        )
                    )
                }

                // 3. Đóng gói Payload (Dữ liệu gửi đi)
                val yeuCau = YeuCauDatHang(
                    maNguoiDung = maNguoiDung,
                    tongTien = tongTien,
                    diaChiGiaoHang = diaChi,
                    phuongThucThanhToan = "COD", // Trả tiền mặt mặc định
                    danhSachMonHang = dsChiTiet
                )

                // 4. Gửi lên FastAPI Backend
                val phanHoi = KetNoiServer.api.taoDonHang(yeuCau)
                if (phanHoi.isSuccessful) {
                    _trangThaiDatHang.value = "Thành công"
                    dao.xoaToanBoGioHang() // Đặt thành công thì xóa giỏ hàng local
                } else {
                    _trangThaiDatHang.value = "Lỗi server: ${phanHoi.code()}"
                }
            } catch (e: Exception) {
                _trangThaiDatHang.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }
}