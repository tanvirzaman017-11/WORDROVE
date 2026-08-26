package com.example.data.repository

import com.example.data.local.StyleProfileDao
import com.example.data.model.StyleProfile
import kotlinx.coroutines.flow.Flow

class StyleProfileRepository(private val styleProfileDao: StyleProfileDao) {

    val profileFlow: Flow<StyleProfile?> = styleProfileDao.getProfileFlow()

    suspend fun getProfile(): StyleProfile? = styleProfileDao.getProfile()

    suspend fun saveProfile(profile: StyleProfile) = styleProfileDao.insertOrUpdateProfile(profile)

    suspend fun clearProfile() = styleProfileDao.clearProfile()
}
