package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.StyleProfile
import com.example.data.model.WardrobeItem
import com.example.data.repository.StyleProfileRepository
import com.example.data.repository.WardrobeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SortOption(val displayName: String) {
    RECENTLY_ADDED("Recently Added"),
    RECENTLY_UPDATED("Recently Updated"),
    ALPHABETICAL("Alphabetical (A-Z)"),
    FAVORITES_FIRST("Favorites First")
}

enum class AvailabilityFilter(val displayName: String) {
    ALL("All Items"),
    AVAILABLE_ONLY("Active Only"),
    UNAVAILABLE_ONLY("Unavailable Only")
}

data class FilterState(
    val selectedCategory: String = "All",
    val selectedColors: Set<String> = emptySet(),
    val selectedFits: Set<String> = emptySet(),
    val selectedStyles: Set<String> = emptySet(),
    val selectedFormalities: Set<String> = emptySet(),
    val selectedSeasons: Set<String> = emptySet(),
    val selectedOccasions: Set<String> = emptySet(),
    val favoritesOnly: Boolean = false,
    val availability: AvailabilityFilter = AvailabilityFilter.ALL,
    val sortOption: SortOption = SortOption.RECENTLY_ADDED
) {
    val activeFilterCount: Int
        get() {
            var count = 0
            if (selectedCategory != "All") count++
            if (selectedColors.isNotEmpty()) count += selectedColors.size
            if (selectedFits.isNotEmpty()) count += selectedFits.size
            if (selectedStyles.isNotEmpty()) count += selectedStyles.size
            if (selectedFormalities.isNotEmpty()) count += selectedFormalities.size
            if (selectedSeasons.isNotEmpty()) count += selectedSeasons.size
            if (selectedOccasions.isNotEmpty()) count += selectedOccasions.size
            if (favoritesOnly) count++
            if (availability != AvailabilityFilter.ALL) count++
            return count
        }

    fun isDefault(): Boolean = activeFilterCount == 0 && sortOption == SortOption.RECENTLY_ADDED
}

class WardrobeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application, viewModelScope)
    val wardrobeRepository = WardrobeRepository(db.wardrobeDao())
    val styleProfileRepository = StyleProfileRepository(db.styleProfileDao())

    val allItems: StateFlow<List<WardrobeItem>> = wardrobeRepository.allItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentItems: StateFlow<List<WardrobeItem>> = wardrobeRepository.recentItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteItems: StateFlow<List<WardrobeItem>> = wardrobeRepository.favoriteItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val styleProfile: StateFlow<StyleProfile> = styleProfileRepository.profileFlow
        .map { it ?: StyleProfile() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            StyleProfile()
        )

    val totalItemCount: StateFlow<Int> = wardrobeRepository.itemCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val categoryCount: StateFlow<Int> = wardrobeRepository.categoryCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val favoriteCount: StateFlow<Int> = wardrobeRepository.favoriteCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Search & Filter State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    // Filtered and Sorted list
    val filteredItems: StateFlow<List<WardrobeItem>> = combine(
        allItems,
        _searchQuery,
        _filterState
    ) { items, query, filter ->
        var list = items

        // 1. Search Query filtering across multiple attributes
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter { item ->
                item.name.lowercase().contains(q) ||
                    item.category.lowercase().contains(q) ||
                    item.subcategory.lowercase().contains(q) ||
                    item.primaryColor.lowercase().contains(q) ||
                    item.secondaryColors.lowercase().contains(q) ||
                    item.style.lowercase().contains(q) ||
                    item.fit.lowercase().contains(q) ||
                    item.formality.lowercase().contains(q) ||
                    item.notes.lowercase().contains(q)
            }
        }

        // 2. Category
        if (filter.selectedCategory != "All") {
            list = list.filter { it.category.equals(filter.selectedCategory, ignoreCase = true) }
        }

        // 3. Colors
        if (filter.selectedColors.isNotEmpty()) {
            list = list.filter { item ->
                filter.selectedColors.any { color ->
                    item.primaryColor.equals(color, ignoreCase = true) ||
                        item.secondaryColors.contains(color, ignoreCase = true)
                }
            }
        }

        // 4. Fit
        if (filter.selectedFits.isNotEmpty()) {
            list = list.filter { item ->
                filter.selectedFits.any { it.equals(item.fit, ignoreCase = true) }
            }
        }

        // 5. Style
        if (filter.selectedStyles.isNotEmpty()) {
            list = list.filter { item ->
                filter.selectedStyles.any { it.equals(item.style, ignoreCase = true) }
            }
        }

        // 6. Formality
        if (filter.selectedFormalities.isNotEmpty()) {
            list = list.filter { item ->
                filter.selectedFormalities.any { it.equals(item.formality, ignoreCase = true) }
            }
        }

        // 7. Season
        if (filter.selectedSeasons.isNotEmpty()) {
            list = list.filter { item ->
                filter.selectedSeasons.any { it.equals(item.season, ignoreCase = true) || item.season.contains(it, ignoreCase = true) }
            }
        }

        // 8. Occasions
        if (filter.selectedOccasions.isNotEmpty()) {
            list = list.filter { item ->
                filter.selectedOccasions.any { item.occasions.contains(it, ignoreCase = true) }
            }
        }

        // 9. Favorites Only
        if (filter.favoritesOnly) {
            list = list.filter { it.favorite }
        }

        // 10. Availability
        when (filter.availability) {
            AvailabilityFilter.AVAILABLE_ONLY -> list = list.filter { it.available }
            AvailabilityFilter.UNAVAILABLE_ONLY -> list = list.filter { !it.available }
            AvailabilityFilter.ALL -> {}
        }

        // 11. Sorting
        when (filter.sortOption) {
            SortOption.RECENTLY_ADDED -> list.sortedByDescending { it.createdAt }
            SortOption.RECENTLY_UPDATED -> list.sortedByDescending { it.updatedAt }
            SortOption.ALPHABETICAL -> list.sortedBy { it.name.lowercase() }
            SortOption.FAVORITES_FIRST -> list.sortedWith(
                compareByDescending<WardrobeItem> { it.favorite }.thenByDescending { it.createdAt }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _filterState.update { it.copy(selectedCategory = category) }
    }

    fun updateFilterState(newFilter: FilterState) {
        _filterState.value = newFilter
    }

    fun resetFilters() {
        _filterState.value = FilterState(selectedCategory = _filterState.value.selectedCategory)
    }

    fun setSortOption(sortOption: SortOption) {
        _filterState.update { it.copy(sortOption = sortOption) }
    }

    fun addItem(item: WardrobeItem, onComplete: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = wardrobeRepository.addItem(item)
            onComplete(id)
        }
    }

    fun updateItem(item: WardrobeItem, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            wardrobeRepository.updateItem(item.copy(updatedAt = System.currentTimeMillis()))
            onComplete()
        }
    }

    fun deleteItem(item: WardrobeItem, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            wardrobeRepository.deleteItem(item)
            onComplete()
        }
    }

    fun deleteItemById(id: Long, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            wardrobeRepository.deleteItemById(id)
            onComplete()
        }
    }

    fun toggleFavorite(item: WardrobeItem) {
        viewModelScope.launch {
            wardrobeRepository.toggleFavorite(item.id, !item.favorite)
        }
    }

    fun toggleAvailability(item: WardrobeItem) {
        viewModelScope.launch {
            wardrobeRepository.toggleAvailability(item.id, !item.available)
        }
    }

    fun saveStyleProfile(profile: StyleProfile, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            styleProfileRepository.saveProfile(profile.copy(updatedAt = System.currentTimeMillis()))
            onComplete()
        }
    }

    fun clearAllData(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            wardrobeRepository.clearAll()
            _searchQuery.value = ""
            _filterState.value = FilterState()
            onComplete()
        }
    }

    fun resetToSampleData(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            wardrobeRepository.clearAll()
            AppDatabase.prepopulateInitialData(db.wardrobeDao(), db.styleProfileDao())
            onComplete()
        }
    }
}
