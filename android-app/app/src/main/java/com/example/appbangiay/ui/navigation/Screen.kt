package com.example.appbangiay.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Home : Screen("home_screen")
    object ChiTiet : Screen("detail_screen/{maGiay}") {
        fun taoDuongDan(maGiay: Int) = "detail_screen/$maGiay"
    }
    object SanPhamTheoThuongHieu : Screen("brand_products/{brand}") {
        fun taoDuongDan(brand: String): String {
            return "brand_products/$brand"
        }
    }
    object Search : Screen("search")
    object Cart : Screen("cart")
    object Address : Screen("address")
    object AddressBook : Screen("address_book")
    object Favorite : Screen("favorite")
    object About : Screen("about")
    object Support : Screen("support")
    object AccountSettings : Screen("account_settings")
}