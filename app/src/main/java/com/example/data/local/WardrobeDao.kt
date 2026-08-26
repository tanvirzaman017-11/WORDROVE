package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.WardrobeItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WardrobeDao {

    @Query("SELECT * FROM wardrobe_items ORDER BY createdAt DESC")
    fun getAllItemsFlow(): Flow<List<WardrobeItem>>

    @Query("SELECT * FROM wardrobe_items WHERE available = 1 ORDER BY createdAt DESC")
    fun getActiveItemsFlow(): Flow<List<WardrobeItem>>

    @Query("SELECT * FROM wardrobe_items WHERE favorite = 1 ORDER BY updatedAt DESC")
    fun getFavoriteItemsFlow(): Flow<List<WardrobeItem>>

    @Query("SELECT * FROM wardrobe_items ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentlyAddedFlow(limit: Int = 10): Flow<List<WardrobeItem>>

    @Query("SELECT * FROM wardrobe_items WHERE id = :id")
    suspend fun getItemById(id: Long): WardrobeItem?

    @Query("SELECT * FROM wardrobe_items WHERE id = :id")
    fun getItemByIdFlow(id: Long): Flow<WardrobeItem?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: WardrobeItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<WardrobeItem>)

    @Update
    suspend fun updateItem(item: WardrobeItem)

    @Delete
    suspend fun deleteItem(item: WardrobeItem)

    @Query("DELETE FROM wardrobe_items WHERE id = :id")
    suspend fun deleteItemById(id: Long)

    @Query("UPDATE wardrobe_items SET favorite = :isFavorite, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE wardrobe_items SET available = :isAvailable, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateAvailabilityStatus(id: Long, isAvailable: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM wardrobe_items")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM wardrobe_items")
    fun getItemCountFlow(): Flow<Int>

    @Query("SELECT COUNT(DISTINCT category) FROM wardrobe_items")
    fun getCategoryCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM wardrobe_items WHERE favorite = 1")
    fun getFavoriteCountFlow(): Flow<Int>
}
