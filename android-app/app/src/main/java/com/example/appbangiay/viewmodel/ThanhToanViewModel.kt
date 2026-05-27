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
import com.google.firebase.auth.FirebaseAuth
import com.example.appbangiay.model.GioHang

class ThanhToanViewModel(private val dao: GioHangDao) : ViewModel() {

    // Lấy danh sách realtime từ Room DB
    private val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val danhSachGioHang = dao.layTheoNguoiDung(uid)

    private val _trangThaiDatHang = MutableStateFlow<String>("Chưa đặt")
    val trangThaiDatHang: StateFlow<String> = _trangThaiDatHang

    fun thucHienDatHang(
        maNguoiDung: Int,
        diaChi: String,
        phuongThucThanhToan: String,
        danhSachDatHang: List<GioHang>,
        xoaGioHangSauKhiDat: Boolean
    ) {
        viewModelScope.launch {
            _trangThaiDatHang.value = "Đang xử lý..."

            try {
                if (danhSachDatHang.isEmpty()) {
                    _trangThaiDatHang.value = "Giỏ hàng trống"
                    return@launch
                }

                val tongTien = danhSachDatHang.sumOf {
                    (it.giaTien * it.soLuong).toDouble()
                }.toFloat()

                val dsChiTiet = danhSachDatHang.map {
                    ChiTietMonHang(
                        maGiay = it.maGiay,
                        soLuong = it.soLuong,
                        giaTien = it.giaTien,
                        mauSac = it.mauSac,
                        size = it.size
                    )
                }

                val user = FirebaseAuth.getInstance().currentUser

                val yeuCau = YeuCauDatHang(
                    maNguoiDung = maNguoiDung,
                    firebaseUid = user?.uid,
                    tenKhachHang = user?.displayName ?: "Khách hàng",
                    emailKhachHang = user?.email,
                    tongTien = tongTien,
                    diaChiGiaoHang = diaChi,
                    phuongThucThanhToan = phuongThucThanhToan,
                    danhSachMonHang = dsChiTiet
                )

                val phanHoi = KetNoiServer.api.taoDonHang(yeuCau)

                if (phanHoi.isSuccessful) {
                    if (xoaGioHangSauKhiDat) {
                        dao.xoaTheoNguoiDung(uid)
                    }

                    _trangThaiDatHang.value = "Đặt hàng thành công"
                } else {
                    _trangThaiDatHang.value = "Lỗi server: ${phanHoi.code()}"
                }

            } catch (e: Exception) {
                _trangThaiDatHang.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }
}