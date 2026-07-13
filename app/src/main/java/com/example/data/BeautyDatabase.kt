package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RegisteredStore::class, StoreProduct::class, ShoppingItem::class],
    version = 2,
    exportSchema = false
)
abstract class BeautyDatabase : RoomDatabase() {
    abstract fun beautyDao(): BeautyDao

    companion object {
        @Volatile
        private var INSTANCE: BeautyDatabase? = null

        fun getDatabase(context: Context): BeautyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BeautyDatabase::class.java,
                    "smart_beauty_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
