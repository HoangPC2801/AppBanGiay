package com.example.appbangiay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbangiay.model.Giay
import com.example.appbangiay.network.KetNoiServer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.appbangiay.database.GioHangDao
import com.example.appbangiay.model.GioHang
class ChiTietGiayViewModel(
    private val dao: GioHangDao
) : ViewModel() {
    private val _giayChiTiet = MutableStateFlow<Giay?>(null)
    val giayChiTiet: StateFlow<Giay?> = _giayChiTiet

    private val _trangThaiTai = MutableStateFlow(false)
    val trangThaiTai: StateFlow<Boolean> = _trangThaiTai

    fun layThongTinGiay(maGiay: Int) {
        viewModelScope.launch {
            _trangThaiTai.value = true
            try {
                // Gọi API lấy thông tin chi tiết của 1 đôi giày
                val ketQua = KetNoiServer.api.layChiTietGiay(maGiay)
                _giayChiTiet.value = ketQua
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _trangThaiTai.value = false
            }
        }
    }

    fun themVaoGioHang(giay: Giay) {
        viewModelScope.launch {
            val monHang = GioHang(
                maGiay = giay.maGiay,
                tenGiay = giay.tenGiay,
                giaTien = giay.giaTien,
                hinhAnh = giay.hinhAnh,
                soLuong = 1 // Mặc định mỗi lần bấm là thêm 1 đôi
            )
            dao.themVaoGio(monHang)
        }
    }
}