package com.example.appbangiay.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Home : Screen("home_screen")

    object ChiTiet : Screen("detail_screen/{maGiay}") {
        fun taoDuongDan(maGiay: Int) = "detail_screen/$maGiay"
    }
}