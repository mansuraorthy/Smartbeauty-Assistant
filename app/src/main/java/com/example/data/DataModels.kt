package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registered_stores")
data class RegisteredStore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val facebookUrl: String,
    val district: String,
    val isVerified: Boolean,
    val isPremium: Boolean,
    val lastUpdatedDaysAgo: Int, // 0 = today, 1 = 1 day ago, etc.
    val logoText: String = name.take(2).uppercase(),
    val rating: Float = 4.5f,
    val complaintsCount: Int = 0,
    val engagementRate: Float = 0.8f
)

@Entity(tableName = "store_products")
data class StoreProduct(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val storeId: Int,
    val brandName: String,
    val productName: String,
    val searchToken: String, // e.g. "boj sunscreen beauty of joseon"
    val availabilityStatus: String, // "Available", "Coming Soon", "Unavailable", "Availability Unknown"
    val restockDays: Int = 0, // days expected to restock if Coming Soon
    val priceBdt: Int,
    val facebookPostUrl: String? = null,
    val facebookPostText: String? = null
)

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val isCompleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
