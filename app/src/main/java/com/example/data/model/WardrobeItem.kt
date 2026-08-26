package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Core Wardrobe Item entity.
 * Designed with full Phase 1 attribute fidelity and extensible metadata fields for future AI integration.
 */
@Entity(tableName = "wardrobe_items")
data class WardrobeItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val subcategory: String = "",
    val primaryColor: String,
    val secondaryColors: String = "",
    val imageUri: String = "",
    val fit: String = "Regular",
    val style: String = "Casual",
    val formality: String = "Casual",
    val season: String = "All Season",
    val occasions: String = "Daily",
    val favorite: Boolean = false,
    val available: Boolean = true,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    // Future AI Extensibility hooks (stored for forward compatibility)
    val aiTags: String = "",
    val wearCount: Int = 0,
    val lastWornTimestamp: Long? = null
)
