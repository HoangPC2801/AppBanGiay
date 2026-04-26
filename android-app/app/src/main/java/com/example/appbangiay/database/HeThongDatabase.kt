package com.example.appbangiay.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appbangiay.model.GioHang

@Database(entities = [GioHang::class], version = 1, exportSchema = false)
abstract class HeThongDatabase : RoomDatabase() {
    abstract fun gioHangDao(): GioHangDao

    companion object {
        @Volatile
        private var INSTANCE: HeThongDatabase? = null

        fun layDatabase(context: Context): HeThongDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HeThongDatabase::class.java,
                    "bizflow_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}