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

// Định nghĩa các endpoint API
interface ApiDichVu {
    @GET("products/")
    suspend fun layDanhSachGiay(): List<Giay>

    @GET("products/{id}")
    suspend fun layChiTietGiay(@Path("id") maGiay: Int): Giay

    @POST("orders/")
    suspend fun taoDonHang(@Body yeuCau: YeuCauDatHang): Response<ResponseBody>
}

// Khởi tạo Retrofit Client
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