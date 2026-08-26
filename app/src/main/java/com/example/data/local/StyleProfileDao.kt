package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.StyleProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface StyleProfileDao {

    @Query("SELECT * FROM style_profile WHERE id = 1 LIMIT 1")
    fun getProfileFlow(): Flow<StyleProfile?>

    @Query("SELECT * FROM style_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): StyleProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: StyleProfile)

    @Query("DELETE FROM style_profile")
    suspend fun clearProfile()
}
