package com.example.data.repository

import com.example.data.local.WardrobeDao
import com.example.data.model.WardrobeItem
import kotlinx.coroutines.flow.Flow

class WardrobeRepository(private val wardrobeDao: WardrobeDao) {

    val allItems: Flow<List<WardrobeItem>> = wardrobeDao.getAllItemsFlow()
    val activeItems: Flow<List<WardrobeItem>> = wardrobeDao.getActiveItemsFlow()
    val favoriteItems: Flow<List<WardrobeItem>> = wardrobeDao.getFavoriteItemsFlow()
    val recentItems: Flow<List<WardrobeItem>> = wardrobeDao.getRecentlyAddedFlow(10)

    val itemCount: Flow<Int> = wardrobeDao.getItemCountFlow()
    val categoryCount: Flow<Int> = wardrobeDao.getCategoryCountFlow()
    val favoriteCount: Flow<Int> = wardrobeDao.getFavoriteCountFlow()

    fun getItemByIdFlow(id: Long): Flow<WardrobeItem?> = wardrobeDao.getItemByIdFlow(id)

    suspend fun getItemById(id: Long): WardrobeItem? = wardrobeDao.getItemById(id)

    suspend fun addItem(item: WardrobeItem): Long = wardrobeDao.insertItem(item)

    suspend fun updateItem(item: WardrobeItem) = wardrobeDao.updateItem(item)

    suspend fun deleteItem(item: WardrobeItem) = wardrobeDao.deleteItem(item)

    suspend fun deleteItemById(id: Long) = wardrobeDao.deleteItemById(id)

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        wardrobeDao.updateFavoriteStatus(id, isFavorite, System.currentTimeMillis())
    }

    suspend fun toggleAvailability(id: Long, isAvailable: Boolean) {
        wardrobeDao.updateAvailabilityStatus(id, isAvailable, System.currentTimeMillis())
    }

    suspend fun clearAll() = wardrobeDao.deleteAll()

    suspend fun insertAll(items: List<WardrobeItem>) = wardrobeDao.insertAll(items)
}
