package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.WardrobeItem
import com.example.data.model.WardrobeTaxonomy
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Vanguard Wardrobe", appName)
    }

    @Test
    fun `taxonomy categories and colors are defined`() {
        assertTrue(WardrobeTaxonomy.Categories.contains("Outerwear"))
        assertTrue(WardrobeTaxonomy.Categories.contains("T-Shirts"))
        assertTrue(WardrobeTaxonomy.PrimaryColors.contains("Black"))
        assertTrue(WardrobeTaxonomy.Fits.contains("Oversized"))
    }

    @Test
    fun `database insertion and retrieval`() = runBlocking {
        val dao = db.wardrobeDao()

        val item = WardrobeItem(
            name = "Test Leather Jacket",
            category = "Outerwear",
            subcategory = "Biker Jacket",
            primaryColor = "Black",
            fit = "Regular",
            style = "Starboy",
            formality = "Smart Casual",
            season = "Fall / Winter",
            occasions = "Date Night",
            favorite = true,
            available = true
        )

        val id = dao.insertItem(item)
        val retrieved = dao.getItemById(id)

        assertNotNull(retrieved)
        assertEquals("Test Leather Jacket", retrieved?.name)
        assertEquals("Outerwear", retrieved?.category)
        assertTrue(retrieved?.favorite == true)

        // Update item test
        val updated = retrieved!!.copy(
            name = "Vintage Biker Jacket",
            favorite = false,
            available = false
        )
        dao.updateItem(updated)
        val afterUpdate = dao.getItemById(id)
        assertEquals("Vintage Biker Jacket", afterUpdate?.name)
        assertEquals(false, afterUpdate?.favorite)
        assertEquals(false, afterUpdate?.available)

        // Delete item test
        dao.deleteItem(afterUpdate!!)
        val afterDelete = dao.getItemById(id)
        assertTrue(afterDelete == null)
    }

    @Test
    fun `style profile insertion and retrieval`() = runBlocking {
        val styleDao = db.styleProfileDao()

        val profile = com.example.data.model.StyleProfile(
            id = 1,
            styleAesthetics = "Minimal, Starboy",
            preferredColors = "Black, Charcoal, Navy",
            avoidColors = "Neon Orange",
            preferredFits = "Oversized, Regular",
            preferredSilhouettes = "Boxy Top + Straight Pants",
            preferredClothingTypes = "Wool Coat, Heavyweight Tee",
            preferredFootwear = "Chelsea Boots",
            preferredAccessories = "Silver Watch",
            styleGoals = "Clean & intentional luxury",
            personalRules = "No synthetic shiny polyester",
            updatedAt = System.currentTimeMillis()
        )

        styleDao.insertOrUpdateProfile(profile)
        val retrieved = styleDao.getProfile()

        assertNotNull(retrieved)
        assertEquals("Minimal, Starboy", retrieved?.styleAesthetics)
        assertEquals("Neon Orange", retrieved?.avoidColors)
        assertEquals("Chelsea Boots", retrieved?.preferredFootwear)
    }
}
