package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import android.util.Log

class BeautyRepository(private val db: BeautyDatabase) {
    private val dao = db.beautyDao()

    val allShoppingItems: Flow<List<ShoppingItem>> = dao.getAllShoppingItems()
    val allStores: Flow<List<RegisteredStore>> = dao.getAllStores()
    val allProducts: Flow<List<StoreProduct>> = dao.getAllProductsFlow()

    suspend fun insertShoppingItem(text: String) {
        if (text.isNotBlank()) {
            dao.insertShoppingItem(ShoppingItem(text = text.trim()))
        }
    }

    suspend fun deleteShoppingItem(item: ShoppingItem) {
        dao.deleteShoppingItem(item)
    }

    suspend fun clearShoppingList() {
        dao.clearAllShoppingItems()
    }

    suspend fun getStoresSync(): List<RegisteredStore> = dao.getStoresSync()
    suspend fun getStoreProductsSync(storeId: Int): List<StoreProduct> = dao.getStoreProductsSync(storeId)
    suspend fun getAllProductsSync(): List<StoreProduct> = dao.getAllProductsSync()

    suspend fun insertStore(store: RegisteredStore): Long = dao.insertStore(store)
    suspend fun insertProduct(product: StoreProduct): Long = dao.insertProduct(product)
    suspend fun deleteStoreById(id: Int) {
        dao.deleteStoreById(id)
        dao.deleteProductsByStore(id)
    }
    suspend fun deleteProductById(id: Int) = dao.deleteProductById(id)
    suspend fun updateStore(store: RegisteredStore) = dao.updateStore(store)
    suspend fun updateProduct(product: StoreProduct) = dao.updateProduct(product)

    fun getStoreProductsFlow(storeId: Int): Flow<List<StoreProduct>> = dao.getStoreProductsFlow(storeId)

    // Method to pre-seed the database if empty
    suspend fun seedDatabaseIfEmpty() {
        val stores = dao.getStoresSync()
        if (stores.isNotEmpty()) {
            // Retroactively ensure Shiseido Fino Hair Mask is added for existing stores
            val allProds = dao.getAllProductsSync()
            val hasFino = allProds.any { it.productName.contains("Fino", ignoreCase = true) }
            if (!hasFino) {
                Log.d("BeautyRepository", "Retroactively seeding Shiseido Fino Hair Mask to existing stores...")
                for (store in stores) {
                    val price = when (store.name) {
                        "Glow Haven BD" -> 1250
                        "Beauty Central Dhaka" -> 1300
                        "Korean Cosmetics BD" -> 1200
                        "Sylhet Beauty Hub" -> 1400
                        else -> 1350
                    }
                    val status = when (store.name) {
                        "Korean Cosmetics BD" -> "Coming Soon"
                        "Aura Cosmetics Bangladesh" -> "Unavailable"
                        else -> "Available"
                    }
                    val restock = if (status == "Coming Soon") 3 else 0
                    dao.insertProduct(
                        StoreProduct(
                            storeId = store.id,
                            brandName = "Shiseido",
                            productName = "Fino Premium Touch Hair Mask",
                            searchToken = "shiseido fino hair mask premium touch hair treatment essence condition",
                            availabilityStatus = status,
                            restockDays = restock,
                            priceBdt = price
                        )
                    )
                }
            }
            return // already seeded
        }

        Log.d("BeautyRepository", "Seeding smart beauty database...")

        // Let's seed 5 gorgeous cosmetic stores in Bangladesh
        val store1Id = dao.insertStore(
            RegisteredStore(
                name = "Glow Haven BD",
                facebookUrl = "https://facebook.com/glowhaven.bd",
                district = "Dhaka",
                isVerified = true,
                isPremium = true,
                lastUpdatedDaysAgo = 0, // today
                rating = 4.9f
            )
        ).toInt()

        val store2Id = dao.insertStore(
            RegisteredStore(
                name = "Beauty Central Dhaka",
                facebookUrl = "https://facebook.com/beautycentral.dhaka",
                district = "Dhaka",
                isVerified = true,
                isPremium = false,
                lastUpdatedDaysAgo = 2, // 2 days ago
                rating = 4.6f
            )
        ).toInt()

        val store3Id = dao.insertStore(
            RegisteredStore(
                name = "Korean Cosmetics BD",
                facebookUrl = "https://facebook.com/korean.cosmetics.bd",
                district = "Chittagong",
                isVerified = false,
                isPremium = true,
                lastUpdatedDaysAgo = 5, // 5 days ago
                rating = 4.7f
            )
        ).toInt()

        val store4Id = dao.insertStore(
            RegisteredStore(
                name = "Sylhet Beauty Hub",
                facebookUrl = "https://facebook.com/sylhet.beauty.hub",
                district = "Sylhet",
                isVerified = true,
                isPremium = false,
                lastUpdatedDaysAgo = 12, // 12 days ago
                rating = 4.4f
            )
        ).toInt()

        val store5Id = dao.insertStore(
            RegisteredStore(
                name = "Aura Cosmetics Bangladesh",
                facebookUrl = "https://facebook.com/auracosmetics",
                district = "Chittagong",
                isVerified = false,
                isPremium = false,
                lastUpdatedDaysAgo = 25, // 25 days ago
                rating = 4.1f
            )
        ).toInt()

        // Seed products for each store to match typical customer requests:
        // "Maybelline Sky High Mascara", "Beauty of Joseon Sunscreen", "COSRX Snail Mucin", "Romand Tint", "Shiseido Fino Hair Mask"

        // 1. Glow Haven BD products (5/5 available or prepped, high freshness, premium)
        dao.insertProduct(StoreProduct(storeId = store1Id, brandName = "Beauty of Joseon", productName = "Relief Sun Sunscreen", searchToken = "boj sunscreen beauty of joseon sun block relief spf five", availabilityStatus = "Available", priceBdt = 1450))
        dao.insertProduct(StoreProduct(storeId = store1Id, brandName = "COSRX", productName = "Advanced Snail 96 Mucin Power Essence", searchToken = "cosrx snail mucin essence hydration", availabilityStatus = "Available", priceBdt = 1500))
        dao.insertProduct(StoreProduct(storeId = store1Id, brandName = "Romand", productName = "Juicy Lasting Tint", searchToken = "romand tint lips gloss lipstick", availabilityStatus = "Available", priceBdt = 1100))
        dao.insertProduct(StoreProduct(storeId = store1Id, brandName = "Maybelline", productName = "Lash Sensational Sky High Mascara", searchToken = "maybelline mascara sky high eyes makeup", availabilityStatus = "Available", priceBdt = 1200))
        dao.insertProduct(StoreProduct(storeId = store1Id, brandName = "The Ordinary", productName = "Niacinamide 10% + Zinc 1%", searchToken = "ordinary niacinamide serum skin", availabilityStatus = "Available", priceBdt = 950))
        dao.insertProduct(StoreProduct(storeId = store1Id, brandName = "Shiseido", productName = "Fino Premium Touch Hair Mask", searchToken = "shiseido fino hair mask premium touch hair treatment essence condition", availabilityStatus = "Available", priceBdt = 1250))

        // 2. Beauty Central Dhaka (4/5 available, 1 Coming Soon, highly fresh)
        dao.insertProduct(StoreProduct(storeId = store2Id, brandName = "Beauty of Joseon", productName = "Relief Sun Sunscreen", searchToken = "boj sunscreen beauty of joseon sun block relief spf five", availabilityStatus = "Available", priceBdt = 1400))
        dao.insertProduct(StoreProduct(storeId = store2Id, brandName = "COSRX", productName = "Advanced Snail 96 Mucin Power Essence", searchToken = "cosrx snail mucin essence hydration", availabilityStatus = "Available", priceBdt = 1580))
        dao.insertProduct(StoreProduct(storeId = store2Id, brandName = "Romand", productName = "Juicy Lasting Tint", searchToken = "romand tint lips gloss lipstick", availabilityStatus = "Coming Soon", restockDays = 4, priceBdt = 1050))
        dao.insertProduct(StoreProduct(storeId = store2Id, brandName = "Maybelline", productName = "Lash Sensational Sky High Mascara", searchToken = "maybelline mascara sky high eyes makeup", availabilityStatus = "Unavailable", restockDays = 15, priceBdt = 1150))
        dao.insertProduct(StoreProduct(storeId = store2Id, brandName = "Anua", productName = "Heartleaf 77% Soothing Toner", searchToken = "anua toner heartleaf calming", availabilityStatus = "Available", priceBdt = 1850))
        dao.insertProduct(StoreProduct(storeId = store2Id, brandName = "Shiseido", productName = "Fino Premium Touch Hair Mask", searchToken = "shiseido fino hair mask premium touch hair treatment essence condition", availabilityStatus = "Available", priceBdt = 1300))

        // 3. Korean Cosmetics BD (Chittagong)
        dao.insertProduct(StoreProduct(storeId = store3Id, brandName = "Beauty of Joseon", productName = "Relief Sun Sunscreen", searchToken = "boj sunscreen beauty of joseon sun block relief spf five", availabilityStatus = "Available", priceBdt = 1500))
        dao.insertProduct(StoreProduct(storeId = store3Id, brandName = "COSRX", productName = "Advanced Snail 96 Mucin Power Essence", searchToken = "cosrx snail mucin essence hydration", availabilityStatus = "Available", priceBdt = 1600))
        dao.insertProduct(StoreProduct(storeId = store3Id, brandName = "Romand", productName = "Juicy Lasting Tint", searchToken = "romand tint lips gloss lipstick", availabilityStatus = "Unavailable", priceBdt = 1200))
        dao.insertProduct(StoreProduct(storeId = store3Id, brandName = "Anua", productName = "Heartleaf 77% Soothing Toner", searchToken = "anua toner heartleaf calming", availabilityStatus = "Available", priceBdt = 1900))
        dao.insertProduct(StoreProduct(storeId = store3Id, brandName = "Shiseido", productName = "Fino Premium Touch Hair Mask", searchToken = "shiseido fino hair mask premium touch hair treatment essence condition", availabilityStatus = "Coming Soon", restockDays = 3, priceBdt = 1200))

        // 4. Sylhet Beauty Hub (Sylhet) - Has Romand & Mascara, others unavailable
        dao.insertProduct(StoreProduct(storeId = store4Id, brandName = "Maybelline", productName = "Lash Sensational Sky High Mascara", searchToken = "maybelline mascara sky high eyes makeup", availabilityStatus = "Available", priceBdt = 1150))
        dao.insertProduct(StoreProduct(storeId = store4Id, brandName = "Romand", productName = "Juicy Lasting Tint", searchToken = "romand tint lips gloss lipstick", availabilityStatus = "Available", priceBdt = 1080))
        dao.insertProduct(StoreProduct(storeId = store4Id, brandName = "Beauty of Joseon", productName = "Relief Sun Sunscreen", searchToken = "boj sunscreen beauty of joseon sun block relief spf five", availabilityStatus = "Unavailable", restockDays = 7, priceBdt = 1490))
        dao.insertProduct(StoreProduct(storeId = store4Id, brandName = "Shiseido", productName = "Fino Premium Touch Hair Mask", searchToken = "shiseido fino hair mask premium touch hair treatment essence condition", availabilityStatus = "Available", priceBdt = 1400))

        // 5. Aura Cosmetics Bangladesh (Chittagong) - Low freshness/standard
        dao.insertProduct(StoreProduct(storeId = store5Id, brandName = "Beauty of Joseon", productName = "Relief Sun Sunscreen", searchToken = "boj sunscreen beauty of joseon sun block relief spf five", availabilityStatus = "Available", priceBdt = 1350))
        dao.insertProduct(StoreProduct(storeId = store5Id, brandName = "COSRX", productName = "Advanced Snail 96 Mucin Power Essence", searchToken = "cosrx snail mucin essence hydration", availabilityStatus = "Available", priceBdt = 1450))
        dao.insertProduct(StoreProduct(storeId = store5Id, brandName = "Shiseido", productName = "Fino Premium Touch Hair Mask", searchToken = "shiseido fino hair mask premium touch hair treatment essence condition", availabilityStatus = "Unavailable", priceBdt = 1350))

        Log.d("BeautyRepository", "Seeding completed successfully!")
    }
}
