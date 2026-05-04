package com.example.appbangiay.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    // Khởi tạo Firebase Auth
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // 1. Hàm xử lý đăng nhập bằng Email / Mật khẩu
    fun loginWithEmail(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Vui lòng nhập đầy đủ Email và Mật khẩu")
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

    // --- XỬ LÝ ĐĂNG KÝ BẰNG EMAIL ---
    fun registerWithEmail(email: String, pass: String, confirmPass: String) {
        if (email.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
            _authState.value = AuthState.Error("Vui lòng điền đầy đủ thông tin")
            return
        }

        if (pass != confirmPass) {
            _authState.value = AuthState.Error("Mật khẩu xác nhận không khớp")
            return
        }

        if (pass.length < 6) {
            _authState.value = AuthState.Error("Mật khẩu phải có ít nhất 6 ký tự")
            return
        }

        _authState.value = AuthState.Loading

        // Gọi Firebase để tạo tài khoản mới
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Mặc định Firebase sẽ tự động đăng nhập luôn sau khi tạo tài khoản thành công
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Đăng ký thất bại")
                }
            }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    // --- XỬ LÝ GOOGLE LOGIN ---
    fun loginWithGoogle(idToken: String) {
        _authState.value = AuthState.Loading
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Lỗi đăng nhập Google")
                }
            }
    }

    // --- XỬ LÝ FACEBOOK LOGIN ---
    fun loginWithFacebook(accessToken: String) {
        _authState.value = AuthState.Loading
        val credential = FacebookAuthProvider.getCredential(accessToken)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Lỗi đăng nhập Facebook")
                }
            }
    }

    // --- XỬ LÝ QUÊN MẬT KHẨU ---
    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _authState.value = AuthState.Error("Vui lòng nhập email của bạn")
            return
        }

        _authState.value = AuthState.Loading

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Tận dụng trạng thái Success để báo thành công
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Không thể gửi email đặt lại mật khẩu")
                }
            }
    }
}
