package com.example.appbangiay.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appbangiay.model.GioHang
import com.example.appbangiay.model.DiaChi
import com.example.appbangiay.model.SanPhamYeuThich

@Database(
    entities = [
        GioHang::class,
        DiaChi::class,
        SanPhamYeuThich::class
    ],
    version = 5,
    exportSchema = false
)
abstract class HeThongDatabase : RoomDatabase() {
    abstract fun gioHangDao(): GioHangDao
    abstract fun diaChiDao(): DiaChiDao
    abstract fun yeuThichDao(): YeuThichDao
    companion object {
        @Volatile
        private var INSTANCE: HeThongDatabase? = null

        fun layDatabase(context: Context): HeThongDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HeThongDatabase::class.java,
                    "bizflow_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
