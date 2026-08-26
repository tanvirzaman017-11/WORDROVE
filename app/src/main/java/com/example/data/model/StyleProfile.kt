package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User's Personal Style Profile.
 * Stores personal aesthetic identity, color preferences, fits, goals, and custom rules for future AI systems.
 */
@Entity(tableName = "style_profile")
data class StyleProfile(
    @PrimaryKey
    val id: Int = 1,
    val styleAesthetics: String = "Minimal, Smart Casual, Old Money",
    val preferredColors: String = "Black, Charcoal, Navy, Cream, Camel",
    val avoidColors: String = "Neon Green, Hot Pink",
    val preferredFits: String = "Relaxed, Oversized, Tailored",
    val preferredSilhouettes: String = "Boxy Top + Straight Trousers, Tailored Jacket + Relaxed Pants",
    val preferredClothingTypes: String = "Heavyweight Tees, Cashmere Sweaters, Tailored Overcoats, Raw Denim",
    val preferredFootwear: String = "Chelsea Boots, Minimal White Sneakers, Loafers",
    val preferredAccessories: String = "Minimal Chronograph Watch, Silver Cuff, Black Leather Belt",
    val styleGoals: String = "Attractive, Clean, Premium, Masculine, Modern",
    val personalRules: String = "Prefer heavyweight fabrics.\nKeep colors muted and neutral.\nMonochromatic bases with one subtle texture contrast.",
    val updatedAt: Long = System.currentTimeMillis()
)
