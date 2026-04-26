package com.example.appbangiay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbangiay.model.Giay
import com.example.appbangiay.network.KetNoiServer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrangChuViewModel : ViewModel() {
    private val _danhSachGiay = MutableStateFlow<List<Giay>>(emptyList())
    val danhSachGiay: StateFlow<List<Giay>> = _danhSachGiay

    private val _trangThaiTai = MutableStateFlow(false)
    val trangThaiTai: StateFlow<Boolean> = _trangThaiTai

    init {
        goiApiLayDanhSachGiay()
    }

    private fun goiApiLayDanhSachGiay() {
        viewModelScope.launch {
            _trangThaiTai.value = true
            try {
                // Parse chuỗi JSON thành danh sách object
                val ketQua = KetNoiServer.api.layDanhSachGiay()
                _danhSachGiay.value = ketQua
            } catch (e: Exception) {
                android.util.Log.e("LoiAPI", "Lỗi tải danh sách giày: ${e.message}")
                e.printStackTrace()
            } finally {
                _trangThaiTai.value = false
            }
        }
    }
}