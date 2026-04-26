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
import com.example.appbangiay.ui.login.RegisterScreen
import com.example.appbangiay.ui.navigation.Screen
import com.example.appbangiay.ui.theme.AppBanGiayTheme
import com.example.appbangiay.viewmodel.AuthViewModel
import com.example.appbangiay.viewmodel.ChiTietGiayViewModel
import com.example.appbangiay.viewmodel.ThanhToanViewModel

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

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(navController, authViewModel)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController, authViewModel)
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