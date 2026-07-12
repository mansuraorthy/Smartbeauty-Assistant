package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BeautyDao {
    // Shopping List Queries
    @Query("SELECT * FROM shopping_items ORDER BY timestamp DESC")
    fun getAllShoppingItems(): Flow<List<ShoppingItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(item: ShoppingItem): Long

    @Delete
    suspend fun deleteShoppingItem(item: ShoppingItem)

    @Query("DELETE FROM shopping_items WHERE id = :id")
    suspend fun deleteShoppingItemById(id: Int)

    @Query("DELETE FROM shopping_items")
    suspend fun clearAllShoppingItems()

    // Store & Inventory Queries
    @Query("SELECT * FROM registered_stores ORDER BY isPremium DESC, isVerified DESC")
    fun getAllStores(): Flow<List<RegisteredStore>>

    @Query("SELECT * FROM registered_stores")
    suspend fun getStoresSync(): List<RegisteredStore>

    @Query("SELECT * FROM store_products WHERE storeId = :storeId")
    suspend fun getStoreProductsSync(storeId: Int): List<StoreProduct>

    @Query("SELECT * FROM store_products")
    fun getAllProductsFlow(): Flow<List<StoreProduct>>

    @Query("SELECT * FROM store_products WHERE storeId = :storeId")
    fun getStoreProductsFlow(storeId: Int): Flow<List<StoreProduct>>

    @Query("SELECT * FROM store_products")
    suspend fun getAllProductsSync(): List<StoreProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: RegisteredStore): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: StoreProduct): Long

    @Update
    suspend fun updateStore(store: RegisteredStore)

    @Update
    suspend fun updateProduct(product: StoreProduct)

    @Query("DELETE FROM registered_stores WHERE id = :id")
    suspend fun deleteStoreById(id: Int)

    @Query("DELETE FROM store_products WHERE id = :id")
    suspend fun deleteProductById(id: Int)

    @Query("DELETE FROM store_products WHERE storeId = :storeId")
    suspend fun deleteProductsByStore(storeId: Int)
}
