package com.example.appbangiay.ui.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appbangiay.R
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.appbangiay.viewmodel.AuthState
import com.example.appbangiay.viewmodel.AuthViewModel
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.AccessToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel, // Thêm ViewModel vào đây
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val primaryBlue = Color(0xFF64A5FF)
    val grayBackground = Color(0xFFEAEAEA)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isRememberPassword by remember { mutableStateOf(false) }

    // Lắng nghe trạng thái từ ViewModel
    val authState by viewModel.authState.collectAsState()

    // --- CẤU HÌNH LAUNCHER CHO GOOGLE ---
    // THAY ĐOẠN MÃ CỦA BẠN VÀO CHỖ "MÃ_WEB_CLIENT_ID_CỦA_BẠN"
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("641802044909-24qqo2p5dsuu0cg99sfjkchoabobo41s.apps.googleusercontent.com")
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val googleAuthLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { viewModel.loginWithGoogle(it) }
            } catch (e: Exception) {
                Toast.makeText(context, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- CẤU HÌNH LAUNCHER CHO FACEBOOK ---
    val facebookAuthLauncher = rememberLauncherForActivityResult(
        LoginManager.getInstance().createLogInActivityResultContract()
    ) { result ->
        // Kiểm tra xem người dùng có đăng nhập thành công không
        if (result.resultCode == Activity.RESULT_OK) {
            // Lấy Token trực tiếp từ phiên đăng nhập hiện tại của Facebook
            val token = AccessToken.getCurrentAccessToken()?.token
            if (token != null) {
                viewModel.loginWithFacebook(token)
            } else {
                Toast.makeText(context, "Không thể lấy thông tin từ Facebook", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Xử lý khi người dùng bấm Hủy (Cancel) tắt bảng đăng nhập
            Toast.makeText(context, "Đã hủy đăng nhập Facebook", Toast.LENGTH_SHORT).show()
        }
    }

    // Lắng nghe trạng thái đăng nhập
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
                onLoginSuccess()
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
        // --- 1. VẼ ĐƯỜNG SÓNG TRÊN VÀ DƯỚI (Tái sử dụng từ Splash/Intro) ---
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

        // --- 2. NỘI DUNG CHÍNH (ĐĂNG NHẬP) ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_hoangshoe1),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tiêu đề
            Text(
                text = "Đăng Nhập",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = primaryBlue
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Input Email
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

            Spacer(modifier = Modifier.height(16.dp))

            // Input Mật khẩu
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Mật khẩu", color = Color.DarkGray) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock Icon", tint = Color.Gray)
                },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle Password", tint = Color.Gray)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = grayBackground,
                    unfocusedContainerColor = grayBackground,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Nhớ mật khẩu & Quên mật khẩu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isRememberPassword,
                        onCheckedChange = { isRememberPassword = it },
                        colors = CheckboxDefaults.colors(checkedColor = primaryBlue)
                    )
                    Text(text = "Nhớ mật khẩu", fontSize = 14.sp)
                }

                Text(
                    text = "Quên mật khẩu?",
                    fontSize = 14.sp,
                    color = primaryBlue,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigateToForgotPassword() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nút Đăng nhập
            Button(
                onClick = {
                    // Gọi hàm login từ ViewModel khi bấm nút
                    viewModel.loginWithEmail(email, password)
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                shape = RoundedCornerShape(16.dp),
                enabled = authState != AuthState.Loading // Khóa nút khi đang tải
            ) {
                if (authState == AuthState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Đăng Nhập", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Dòng chữ: Hoặc đăng nhập bằng
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Black)
                Text(
                    text = " Hoặc đăng nhập bằng ",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nút Mạng xã hội (Facebook, Google)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Nút Facebook
                Image(
                    painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription = "Facebook Login",
                    modifier = Modifier.size(50.dp).clip(CircleShape).clickable {
                        // Gọi màn hình đăng nhập FB
                        facebookAuthLauncher.launch(listOf("email", "public_profile"))
                    }
                )
                Spacer(modifier = Modifier.width(32.dp))
                // Nút Google
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Login",
                    modifier = Modifier.size(50.dp).clip(CircleShape).clickable {
                        // Khởi chạy Intent đăng nhập Google
                        googleAuthLauncher.launch(googleSignInClient.signInIntent)
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Chữ: Chưa có tài khoản, Đăng ký ngay
            Text(
                text = buildAnnotatedString {
                    append("Chưa có tài khoản, ")
                    withStyle(style = SpanStyle(color = primaryBlue, fontWeight = FontWeight.Bold)) {
                        append("Đăng ký ngay")
                    }
                },
                fontSize = 14.sp,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        viewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
        onNavigateToRegister = {},
        onNavigateToForgotPassword = {},
        onLoginSuccess = {}
    )
}