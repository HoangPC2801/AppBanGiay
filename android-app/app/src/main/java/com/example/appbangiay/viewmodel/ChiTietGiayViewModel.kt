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
import com.google.firebase.auth.FirebaseAuth
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

    fun themVaoGioHang(
        giay: Giay,
        mauSac: String?,
        size: String?
    ) {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

            val sanPhamDaCo = dao.timSanPhamTrongGioHang(
                firebaseUid = uid,
                maGiay = giay.maGiay,
                mauSac = mauSac,
                size = size
            )

            if (sanPhamDaCo != null) {
                dao.capNhat(
                    sanPhamDaCo.copy(
                        soLuong = sanPhamDaCo.soLuong + 1,
                        tenGiay = giay.tenGiay,
                        giaTien = giay.giaTien,
                        hinhAnh = giay.hinhAnh
                    )
                )
            } else {
                val monHang = GioHang(
                    firebaseUid = uid,
                    maGiay = giay.maGiay,
                    tenGiay = giay.tenGiay,
                    giaTien = giay.giaTien,
                    hinhAnh = giay.hinhAnh,
                    mauSac = mauSac,
                    size = size,
                    soLuong = 1
                )

                dao.themVaoGio(monHang)
            }
        }
    }
}