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
import com.example.appbangiay.ui.MainScreen
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.example.appbangiay.ui.brand.ManHinhSanPhamTheoThuongHieu
import com.example.appbangiay.ui.brand.ManHinhSanPhamTheoDanhMuc
import com.example.appbangiay.ui.search.ManHinhTimKiem
import com.example.appbangiay.ui.address.ManHinhChonDiaChi
import com.example.appbangiay.ui.favorite.ManHinhYeuThich
import androidx.core.view.WindowCompat
import androidx.compose.runtime.getValue
import com.example.appbangiay.ui.about.ManHinhVeHoangShoe
import com.example.appbangiay.ui.support.ManHinhTrungTamHoTro
import com.example.appbangiay.ui.settings.ManHinhCaiDatTaiKhoan
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.WHITE
        window.navigationBarColor = android.graphics.Color.WHITE
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true
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
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    fun daDangNhap(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    val db = HeThongDatabase.layDatabase(context)
    val gioHangDao = db.gioHangDao()
    val diaChiDao = db.diaChiDao()
    val yeuThichDao = db.yeuThichDao()
    // SỬA: Thay đổi startDestination thành "splash_screen"
    NavHost(navController = navController, startDestination = "splash_screen") {

        // 1. Màn hình Splash
        composable("splash_screen") {
            SplashScreen(
                onNavigateNext = {
                    val daXemIntro = sharedPreferences.getBoolean("da_xem_intro", false)

                    if (daXemIntro) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo("splash_screen") { inclusive = true }
                        }
                    } else {
                        navController.navigate("intro_screen") {
                            popUpTo("splash_screen") { inclusive = true }
                        }
                    }
                }
            )
        }

        // 2. Màn hình Intro
        composable("intro_screen") {
            IntroScreen(
                onFinishIntro = {
                    sharedPreferences.edit()
                        .putBoolean("da_xem_intro", true)
                        .apply()

                    navController.navigate(Screen.Home.route) {
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
                },
                quayVeTrangChu = {
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
            MainScreen(
                onNavigateToDetail = { maGiay ->
                    navController.navigate(Screen.ChiTiet.taoDuongDan(maGiay))
                },
                onNavigateToBrand = { brand ->
                    navController.navigate(Screen.SanPhamTheoThuongHieu.taoDuongDan(brand))
                },
                onNavigateToCategory = { category ->
                    navController.navigate("category_products/$category")
                },
                onRequireLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onNavigateToSearch = {
                    navController.navigate(Screen.Search.route)
                },
                onNavigateToCart = {

                    val currentUser = FirebaseAuth.getInstance().currentUser

                    if (currentUser == null) {
                        navController.navigate(Screen.Login.route)
                    } else {
                        navController.navigate(Screen.Cart.route)
                    }
                },
                onNavigateToAddressBook = {
                    navController.navigate(Screen.AddressBook.route)
                },
                onNavigateToFavorite = {
                    navController.navigate(Screen.Favorite.route)
                },
                onNavigateToAbout = {
                    navController.navigate(Screen.About.route)
                },
                onNavigateToSupport = {
                    navController.navigate(Screen.Support.route)
                },
                onNavigateToAccountSettings = {
                    navController.navigate(Screen.AccountSettings.route)
                },
                isLoggedIn = daDangNhap()
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
                dao = gioHangDao,
                yeuThichDao = yeuThichDao,
                quayLai = { navController.popBackStack() },
                chuyenSangGioHang = {
                    navController.navigate("cart_screen")
                },
                yeuCauDangNhap = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable("cart_screen") {
            if (!daDangNhap()) {
                navController.navigate(Screen.Login.route) {
                    popUpTo("cart_screen") { inclusive = true }
                }
            } else {
                ManHinhGioHang(
                    dao = gioHangDao,
                    quayLai = { navController.popBackStack() },
                    chuyenSangThanhToan = {
                        navController.navigate("checkout_screen")
                    }
                )
            }
        }

        composable("checkout_screen") {
            if (!daDangNhap()) {
                navController.navigate(Screen.Login.route) {
                    popUpTo("checkout_screen") { inclusive = true }
                }
            } else {
                val thanhToanViewModel: ThanhToanViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return ThanhToanViewModel(gioHangDao) as T
                        }
                    }
                )

                val diaChiDaChon = navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>("dia_chi_da_chon")
                    ?: ""

                ManHinhThanhToan(
                    viewModel = thanhToanViewModel,
                    quayVeTrangChu = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    chonDiaChi = {
                        navController.navigate(Screen.Address.route)
                    },
                    diaChiDaChon = diaChiDaChon
                )
            }
        }

        composable("brand_products/{brand}") { backStackEntry ->
            val brand = backStackEntry.arguments?.getString("brand") ?: ""

            ManHinhSanPhamTheoThuongHieu(
                brand = brand,
                quayLai = { navController.popBackStack() },
                chuyenSangChiTiet = { maGiay ->
                    navController.navigate(Screen.ChiTiet.taoDuongDan(maGiay))
                }
            )
        }

        composable("category_products/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""

            ManHinhSanPhamTheoDanhMuc(
                category = category,
                quayLai = { navController.popBackStack() },
                chuyenSangChiTiet = { maGiay ->
                    navController.navigate(Screen.ChiTiet.taoDuongDan(maGiay))
                }
            )
        }

        composable(Screen.Search.route) {

            ManHinhTimKiem(
                quayLai = {
                    navController.popBackStack()
                },
                chuyenSangChiTiet = { maGiay ->
                    navController.navigate(
                        Screen.ChiTiet.taoDuongDan(maGiay)
                    )
                }
            )
        }

        composable(Screen.Cart.route) {
            ManHinhGioHang(
                dao = gioHangDao,
                quayLai = {
                    navController.popBackStack()
                },
                chuyenSangThanhToan = {
                    navController.navigate("checkout_screen")
                }
            )
        }

        composable(Screen.Address.route) {
            ManHinhChonDiaChi(
                dao = diaChiDao,
                quayLai = {
                    navController.popBackStack()
                },
                onDiaChiSelected = { diaChi ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "dia_chi_da_chon",
                            "${diaChi.tenNguoiNhan} - ${diaChi.diaChiChiTiet}, ${diaChi.tinhThanh}"
                        )

                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AddressBook.route) {
            ManHinhChonDiaChi(
                dao = diaChiDao,
                quayLai = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Favorite.route) {
            ManHinhYeuThich(
                dao = yeuThichDao,
                quayLai = {
                    navController.popBackStack()
                },
                moChiTiet = { maGiay ->
                    navController.navigate(
                        Screen.ChiTiet.taoDuongDan(maGiay)
                    )
                }
            )
        }

        composable(Screen.About.route) {
            ManHinhVeHoangShoe(
                quayLai = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Support.route) {
            ManHinhTrungTamHoTro(
                quayLai = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AccountSettings.route) {
            ManHinhCaiDatTaiKhoan(
                quayLai = {
                    navController.popBackStack()
                },
                veDangNhap = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

    }
}

