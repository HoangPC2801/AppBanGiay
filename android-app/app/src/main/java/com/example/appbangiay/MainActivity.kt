package com.example.appbangiay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appbangiay.database.HeThongDatabase
import com.example.appbangiay.ui.cart.ManHinhGioHang
import com.example.appbangiay.ui.checkout.ManHinhThanhToan
import com.example.appbangiay.ui.detail.ManHinhChiTiet
import com.example.appbangiay.ui.home.ManHinhTrangChu
import com.example.appbangiay.ui.login.LoginScreen
import com.example.appbangiay.ui.login.ForgotPasswordScreen
import com.example.appbangiay.ui.login.RegisterScreen
import com.example.appbangiay.ui.navigation.Screen
import com.example.appbangiay.ui.theme.AppBanGiayTheme
import com.example.appbangiay.viewmodel.AuthViewModel
import com.example.appbangiay.viewmodel.ChiTietGiayViewModel
import com.example.appbangiay.viewmodel.ThanhToanViewModel
import com.example.appbangiay.ui.intro.SplashScreen
import com.example.appbangiay.ui.intro.IntroScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppBanGiayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    val context = LocalContext.current
    val db = HeThongDatabase.layDatabase(context)
    val gioHangDao = db.gioHangDao()

    // SỬA: Thay đổi startDestination thành "splash_screen"
    NavHost(navController = navController, startDestination = "splash_screen") {

        // 1. Màn hình Splash
        composable("splash_screen") {
            SplashScreen(
                onNavigateNext = {
                    // Chuyển sang Intro và xóa Splash khỏi lịch sử
                    navController.navigate("intro_screen") {
                        popUpTo("splash_screen") { inclusive = true }
                    }
                }
            )
        }

        // 2. Màn hình Intro
        composable("intro_screen") {
            IntroScreen(
                onFinishIntro = {
                    // Chuyển sang Login và xóa Intro khỏi lịch sử
                    navController.navigate(Screen.Login.route) {
                        popUpTo("intro_screen") { inclusive = true }
                    }
                }
            )
        }

        // 3. Màn hình Login (Sử dụng UI mới)
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate("forgot_password_screen")
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    // Nhấn chữ "Đăng nhập" thì quay lại màn hình Login
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    // Đăng ký xong Firebase sẽ tự login, đẩy thẳng vào Trang Chủ
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable("forgot_password_screen") {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onNavigateBack = {
                    navController.popBackStack() // Quay lại màn hình đăng nhập
                }
            )
        }

        composable(Screen.Home.route) {
            ManHinhTrangChu(
                chuyenSangChiTiet = { maGiay ->
                    navController.navigate(Screen.ChiTiet.taoDuongDan(maGiay))
                }
            )
        }

        composable(
            route = Screen.ChiTiet.route,
            arguments = listOf(navArgument("maGiay") { type = NavType.IntType })
        ) { backStackEntry ->
            val maGiay = backStackEntry.arguments?.getInt("maGiay") ?: 0

            val chiTietViewModel: ChiTietGiayViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ChiTietGiayViewModel(gioHangDao) as T
                    }
                }
            )

            ManHinhChiTiet(
                maGiay = maGiay,
                quayLai = { navController.popBackStack() },
                chuyenSangGioHang = {
                    navController.navigate("cart_screen")
                },
                viewModel = chiTietViewModel
            )
        }

        composable("cart_screen") {
            ManHinhGioHang(
                dao = gioHangDao,
                quayLai = { navController.popBackStack() },
                chuyenSangThanhToan = {
                    navController.navigate("checkout_screen")
                }
            )
        }

        composable("checkout_screen") {
            val thanhToanViewModel: ThanhToanViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ThanhToanViewModel(gioHangDao) as T
                    }
                }
            )

            ManHinhThanhToan(
                viewModel = thanhToanViewModel,
                quayVeTrangChu = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}