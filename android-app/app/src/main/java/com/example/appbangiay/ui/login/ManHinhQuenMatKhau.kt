package com.example.appbangiay.ui.login

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appbangiay.viewmodel.AuthState
import com.example.appbangiay.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val primaryBlue = Color(0xFF64A5FF)
    val grayBackground = Color(0xFFEAEAEA)

    var email by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()

    // Lắng nghe trạng thái gửi email
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                Toast.makeText(context, "Đã gửi email đặt lại mật khẩu! Vui lòng kiểm tra hộp thư.", Toast.LENGTH_LONG).show()
                viewModel.resetState()
                onNavigateBack() // Gửi xong thì tự động quay lại màn hình đăng nhập
            }
            is AuthState.Error -> {
                val errorMessage = (authState as AuthState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- VẼ ĐƯỜNG SÓNG ---
        Canvas(modifier = Modifier.fillMaxWidth().height(220.dp).align(Alignment.TopCenter)) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(0f, size.height * 0.6f)
                cubicTo(
                    x1 = size.width * 0.3f, y1 = size.height * 0.6f,
                    x2 = size.width * 0.5f, y2 = size.height * 0.1f,
                    x3 = size.width, y3 = size.height * 0.3f
                )
                lineTo(size.width, 0f)
                close()
            }
            drawPath(path = path, color = primaryBlue)
        }

        Canvas(modifier = Modifier.fillMaxWidth().height(180.dp).align(Alignment.BottomCenter)) {
            val path = Path().apply {
                moveTo(0f, size.height)
                lineTo(0f, size.height * 0.5f)
                quadraticBezierTo(
                    x1 = size.width * 0.5f, y1 = size.height * 1f,
                    x2 = size.width, y2 = size.height * 0.5f
                )
                lineTo(size.width, size.height)
                close()
            }
            drawPath(path = path, color = primaryBlue)
        }

        // --- NỘI DUNG CHÍNH ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Quên Mật Khẩu",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = primaryBlue
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Vui lòng nhập email của bạn. Chúng tôi sẽ gửi một mã xác thực để đặt lại mật khẩu.",
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Ô nhập Email
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = Color.DarkGray) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon", tint = Color.Gray)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = grayBackground,
                    unfocusedContainerColor = grayBackground,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nút Gửi yêu cầu
            Button(
                onClick = { viewModel.resetPassword(email) },
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Cho nút hơi thu vào một chút giống ảnh
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                shape = RoundedCornerShape(16.dp),
                enabled = authState != AuthState.Loading
            ) {
                if (authState == AuthState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Gửi yêu cầu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nút Quay lại
            Text(
                text = "Quay lại",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .clickable { onNavigateBack() }
                    .padding(8.dp)
            )
        }
    }
}