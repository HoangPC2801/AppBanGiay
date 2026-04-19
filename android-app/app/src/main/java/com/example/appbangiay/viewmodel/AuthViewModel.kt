package com.example.appbangiay.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Quản lý trạng thái thông báo lỗi hoặc thành công
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, pass: String) {
        if (email.isEmpty() || pass.isEmpty()) {
            _authState.value = AuthState.Error("Vui lòng nhập đầy đủ email và mật khẩu")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Đăng nhập thất bại")
                }
            }
    }

    fun register(email: String, pass: String, confirmPass: String) {
        // 1. Kiểm tra rỗng
        if (email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            _authState.value = AuthState.Error("Vui lòng điền đầy đủ thông tin")
            return
        }

        // 2. Kiểm tra mật khẩu khớp nhau
        if (pass != confirmPass) {
            _authState.value = AuthState.Error("Mật khẩu xác nhận không khớp")
            return
        }

        // 3. Kiểm tra độ dài mật khẩu (Firebase yêu cầu tối thiểu 6 ký tự)
        if (pass.length < 6) {
            _authState.value = AuthState.Error("Mật khẩu phải có ít nhất 6 ký tự")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Đăng ký thất bại")
                }
            }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

// Lớp đại diện cho các trạng thái của màn hình Đăng nhập
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}