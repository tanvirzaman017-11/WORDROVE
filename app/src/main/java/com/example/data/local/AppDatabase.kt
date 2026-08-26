package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.R
import com.example.data.model.StyleProfile
import com.example.data.model.WardrobeItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [WardrobeItem::class, StyleProfile::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wardrobeDao(): WardrobeDao
    abstract fun styleProfileDao(): StyleProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vanguard_wardrobe.db"
                )
                    .addCallback(DatabaseCallback(scope, context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope,
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        prepopulateInitialData(database.wardrobeDao(), database.styleProfileDao())
                    }
                }
            }
        }

        suspend fun prepopulateInitialData(wardrobeDao: WardrobeDao, styleProfileDao: StyleProfileDao) {
            // Pre-seed curated style profile
            styleProfileDao.insertOrUpdateProfile(
                StyleProfile(
                    id = 1,
                    styleAesthetics = "Minimal, Smart Casual, Old Money",
                    preferredColors = "Black, Charcoal, Navy, Cream / Off-White, Camel / Tan",
                    avoidColors = "Neon Yellow, Hot Pink",
                    preferredFits = "Relaxed, Oversized, Tailored",
                    preferredSilhouettes = "Boxy Heavyweight Top + Straight Trousers, Tailored Jacket + Relaxed Chinos",
                    preferredClothingTypes = "Heavyweight Tees, Cashmere Overcoats, Oxford Shirts, Raw Denim",
                    preferredFootwear = "Chelsea Boots, Minimal White Sneakers, Penny Loafers",
                    preferredAccessories = "Minimalist Chronograph, Matte Black Leather Belt, Silver Ring",
                    styleGoals = "Clean, Premium, Masculine, Modern, Attractive",
                    personalRules = "1. Maintain monochromatic bases with structured tailoring.\n2. Prioritize heavyweight natural fibers (wool, cotton, leather).\n3. Keep jewelry subtle and silver-toned.",
                    updatedAt = System.currentTimeMillis()
                )
            )

            // Pre-seed initial high-quality wardrobe essentials
            val sampleItems = listOf(
                WardrobeItem(
                    name = "Heavyweight Boxy Tee",
                    category = "T-Shirts",
                    subcategory = "Heavyweight Crewneck",
                    primaryColor = "Black",
                    secondaryColors = "Charcoal",
                    imageUri = "res://drawable/item_black_tee_1787752237704",
                    fit = "Oversized",
                    style = "Minimal",
                    formality = "Casual",
                    season = "All Season",
                    occasions = "Daily, Travel / Commute",
                    favorite = true,
                    available = true,
                    notes = "280 GSM luxury combed cotton with drop shoulders. Versatile core staple."
                ),
                WardrobeItem(
                    name = "Lambskin Leather Biker",
                    category = "Outerwear",
                    subcategory = "Leather Biker Jacket",
                    primaryColor = "Black",
                    secondaryColors = "Silver",
                    imageUri = "res://drawable/item_leather_jacket_1787752250093",
                    fit = "Regular",
                    style = "Starboy",
                    formality = "Smart Casual",
                    season = "Fall / Winter",
                    occasions = "Date Night, Party & Nightout, Dinner & Drinks",
                    favorite = true,
                    available = true,
                    notes = "Supple matte Italian lambskin with silver hardware and asymmetric zip."
                ),
                WardrobeItem(
                    name = "Suede Chelsea Boots",
                    category = "Shoes",
                    subcategory = "Chelsea Boots",
                    primaryColor = "Espresso Brown",
                    secondaryColors = "Camel / Tan",
                    imageUri = "res://drawable/item_chelsea_boots_1787752265350",
                    fit = "Regular",
                    style = "Italian Classy",
                    formality = "Smart Casual",
                    season = "All Season",
                    occasions = "Date Night, Work / Office, Dinner & Drinks",
                    favorite = true,
                    available = true,
                    notes = "Handcrafted espresso suede with crepe rubber sole. Extremely comfortable and versatile."
                ),
                WardrobeItem(
                    name = "Raw Selvedge Denim",
                    category = "Pants / Jeans",
                    subcategory = "Raw Denim Jeans",
                    primaryColor = "Navy",
                    secondaryColors = "White",
                    imageUri = "",
                    fit = "Straight",
                    style = "Classic",
                    formality = "Casual",
                    season = "All Season",
                    occasions = "Daily, Dinner & Drinks",
                    favorite = false,
                    available = true,
                    notes = "14oz Japanese selvedge denim with natural indigo dye."
                ),
                WardrobeItem(
                    name = "Oxford Cotton Button-Down",
                    category = "Shirts",
                    subcategory = "Oxford Button-Down",
                    primaryColor = "White",
                    secondaryColors = "Navy",
                    imageUri = "",
                    fit = "Regular",
                    style = "Old Money",
                    formality = "Smart Casual",
                    season = "All Season",
                    occasions = "Work / Office, Date Night, Special Event",
                    favorite = true,
                    available = true,
                    notes = "Crisp white Oxford cloth with mother-of-pearl buttons."
                ),
                WardrobeItem(
                    name = "Minimal Steel Chronograph",
                    category = "Watches",
                    subcategory = "Minimalist Chronograph",
                    primaryColor = "Silver",
                    secondaryColors = "Black",
                    imageUri = "",
                    fit = "Tailored",
                    style = "Minimal",
                    formality = "Business Casual",
                    season = "All Season",
                    occasions = "Daily, Work / Office, Special Event",
                    favorite = true,
                    available = true,
                    notes = "40mm brushed stainless steel casing with sapphire crystal."
                )
            )

            wardrobeDao.insertAll(sampleItems)
        }
    }
}
