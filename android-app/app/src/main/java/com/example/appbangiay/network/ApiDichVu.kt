package com.example.appbangiay.network

import com.example.appbangiay.model.Giay
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.appbangiay.model.YeuCauDatHang
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.appbangiay.model.ProductReviewOut
import com.example.appbangiay.model.ProductReviewCreate
import com.example.appbangiay.model.ProductReviewSummary
import retrofit2.http.Query
import com.example.appbangiay.model.OrderResponse
import com.example.appbangiay.model.AppBanner
import com.example.appbangiay.model.DonHangCuaToi
import com.example.appbangiay.model.ChiTietDonHang
import retrofit2.http.PATCH
import com.example.appbangiay.model.ThongBao
import com.example.appbangiay.model.ThongBaoChuaDocResponse
import com.example.appbangiay.model.FcmTokenRequest

interface ApiDichVu {
    @GET("products/")
    suspend fun layDanhSachGiay(): List<Giay>

    @GET("products/{id}")
    suspend fun layChiTietGiay(@Path("id") maGiay: Int): Giay

    @POST("orders/")
    suspend fun taoDonHang(
        @Body request: YeuCauDatHang
    ): Response<OrderResponse>

    @GET("products/{productId}/reviews")
    suspend fun layDanhGiaSanPham(
        @Path("productId") productId: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): ProductReviewSummary

    @POST("products/{productId}/reviews")
    suspend fun guiDanhGiaSanPham(
        @Path("productId") productId: Int,
        @Body review: ProductReviewCreate
    ): ProductReviewOut

    @POST("products/reviews/{reviewId}/like")
    suspend fun likeReview(
        @Path("reviewId") reviewId: Int,
        @Query("firebase_uid") firebaseUid: String
    ): Map<String, Boolean>

    @GET("app-banners/")
    suspend fun layDanhSachBanner(): List<AppBanner>

    @GET("orders/my-orders")
    suspend fun layDonHangCuaToi(
        @Query("firebase_uid") firebaseUid: String,
        @Query("status") status: String
    ): List<DonHangCuaToi>

    @GET("orders/my-orders/{order_id}")
    suspend fun layChiTietDonHangCuaToi(
        @Path("order_id") orderId: Int,
        @Query("firebase_uid") firebaseUid: String
    ): ChiTietDonHang

    @PATCH("orders/cancel/{order_id}")
    suspend fun huyDonHang(
        @Path("order_id") orderId: Int,
        @Query("firebase_uid") firebaseUid: String
    ): ResponseBody

    @GET("notifications/")
    suspend fun layThongBao(
        @Query("firebase_uid") firebaseUid: String,
        @Query("type") type: String? = null
    ): List<ThongBao>

    @PATCH("notifications/{notification_id}/read")
    suspend fun danhDauDaDocThongBao(
        @Path("notification_id") notificationId: Int
    ): ResponseBody

    @GET("notifications/unread-count")
    suspend fun laySoThongBaoChuaDoc(
        @Query("firebase_uid") firebaseUid: String
    ): ThongBaoChuaDocResponse

    @POST("fcm/token")
    suspend fun luuFcmToken(
        @Body request: FcmTokenRequest
    ): ResponseBody
}

object KetNoiServer {
    private const val BASE_URL = "http://10.0.2.2:8000/" // Dành cho máy ảo Android

    val api: ApiDichVu by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiDichVu::class.java)
    }
}