package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.WardrobeItem
import com.example.data.model.WardrobeTaxonomy
import com.example.ui.FilterState
import com.example.ui.WardrobeViewModel
import com.example.ui.components.FilterSortSheet
import com.example.ui.components.ItemCard
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.DarkGunmetal
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentDark
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PlatinumDark
import com.example.ui.theme.PlatinumMuted
import com.example.ui.theme.PlatinumWhite
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateLightBorder
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WardrobeScreen(
    viewModel: WardrobeViewModel,
    onNavigateToAddItem: () -> Unit,
    onNavigateToItemDetails: (Long) -> Unit
) {
    val filteredItems by viewModel.filteredItems.collectAsStateWithLifecycle()
    val allItems by viewModel.allItems.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showFilterSheet by remember { mutableStateOf(false) }

    val categories = listOf("All") + WardrobeTaxonomy.Categories

    Scaffold(
        containerColor = ObsidianBlack,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddItem,
                containerColor = GoldAccent,
                contentColor = ObsidianBlack,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 70.dp)
                    .testTag("wardrobe_fab_add")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Wardrobe Item"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WARDROBE CATALOG",
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Collection (${filteredItems.size})",
                        color = PlatinumWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Filter & Sort Trigger
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BadgedBox(
                        badge = {
                            if (filterState.activeFilterCount > 0) {
                                Badge(
                                    containerColor = GoldAccent,
                                    contentColor = ObsidianBlack
                                ) {
                                    Text(
                                        text = filterState.activeFilterCount.toString(),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = { showFilterSheet = true },
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkGunmetal)
                                .size(40.dp)
                                .testTag("open_filter_sort_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter & Sort",
                                tint = if (filterState.activeFilterCount > 0) GoldAccent else PlatinumWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Search by name, color, fit, style...", color = PlatinumMuted, fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = PlatinumMuted
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search",
                                tint = PlatinumMuted
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .testTag("wardrobe_search_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkGunmetal,
                    unfocusedContainerColor = DarkGunmetal,
                    focusedBorderColor = GoldAccent,
                    unfocusedBorderColor = SlateBorder,
                    focusedTextColor = PlatinumWhite,
                    unfocusedTextColor = PlatinumWhite
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Chips Row
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = filterState.selectedCategory.equals(category, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectCategory(category) },
                        label = {
                            Text(
                                text = category,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = DarkGunmetal,
                            labelColor = PlatinumWhite,
                            selectedContainerColor = GoldAccent,
                            selectedLabelColor = ObsidianBlack
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = SlateBorder,
                            selectedBorderColor = GoldAccent,
                            enabled = true,
                            selected = isSelected
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("category_chip_$category")
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Grid Content
            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (allItems.isEmpty()) {
                        EmptyWardrobeState(onAddFirstItem = onNavigateToAddItem)
                    } else {
                        EmptyFilterResultsState(
                            onReset = {
                                viewModel.updateSearchQuery("")
                                viewModel.resetFilters()
                            }
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 120.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("wardrobe_items_grid")
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        ItemCard(
                            item = item,
                            onClick = { onNavigateToItemDetails(item.id) },
                            onToggleFavorite = { viewModel.toggleFavorite(item) }
                        )
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterSortSheet(
            sheetState = sheetState,
            currentFilter = filterState,
            onApply = { newFilter ->
                viewModel.updateFilterState(newFilter)
                coroutineScope.launch {
                    sheetState.hide()
                    showFilterSheet = false
                }
            },
            onDismiss = {
                coroutineScope.launch {
                    sheetState.hide()
                    showFilterSheet = false
                }
            }
        )
    }
}

@Composable
private fun EmptyWardrobeState(onAddFirstItem: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(DarkGunmetal),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = GoldAccent,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No items yet",
            color = PlatinumWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Add your first wardrobe item to start building your personal wardrobe.",
            color = PlatinumMuted,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onAddFirstItem,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldAccent,
                contentColor = ObsidianBlack
            ),
            modifier = Modifier.testTag("empty_add_first_item_btn")
        ) {
            Text(text = "+ Add First Item", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun EmptyFilterResultsState(onReset: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(DarkGunmetal),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = PlatinumMuted,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No matching items found",
            color = PlatinumWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Try adjusting your search keywords or resetting your active filter selections.",
            color = PlatinumMuted,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(
            onClick = onReset,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldAccent),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccentDark),
            modifier = Modifier.testTag("reset_search_filter_btn")
        ) {
            Text(text = "Reset All Filters", fontWeight = FontWeight.Bold)
        }
    }
}
