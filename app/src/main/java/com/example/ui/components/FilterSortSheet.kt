package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WardrobeTaxonomy
import com.example.ui.AvailabilityFilter
import com.example.ui.FilterState
import com.example.ui.SortOption
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.DarkGunmetal
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentDark
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PlatinumMuted
import com.example.ui.theme.PlatinumWhite
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateLightBorder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterSortSheet(
    sheetState: SheetState,
    currentFilter: FilterState,
    onApply: (FilterState) -> Unit,
    onDismiss: () -> Unit
) {
    var filterDraft by remember(currentFilter) { mutableStateOf(currentFilter) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DarkGunmetal,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(SlateLightBorder)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with Reset & Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter & Sort",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlatinumWhite
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Reset All",
                        fontSize = 13.sp,
                        color = GoldAccent,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .testTag("reset_filters_btn")
                            .clickable {
                                filterDraft = FilterState(selectedCategory = filterDraft.selectedCategory)
                            }
                            .padding(8.dp)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = PlatinumMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Sort Options
            SectionTitle(title = "Sort By")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SortOption.values().forEach { option ->
                    val isSelected = filterDraft.sortOption == option
                    FilterChip(
                        selected = isSelected,
                        onClick = { filterDraft = filterDraft.copy(sortOption = option) },
                        label = { Text(option.displayName, fontSize = 12.sp) },
                        colors = customChipColors(isSelected),
                        border = customChipBorder(isSelected)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(18.dp))

            // 2. Favorites & Availability Toggles
            SectionTitle(title = "Item Status")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkCharcoal)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Favorites Only",
                    color = PlatinumWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Switch(
                    checked = filterDraft.favoritesOnly,
                    onCheckedChange = { filterDraft = filterDraft.copy(favoritesOnly = it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ObsidianBlack,
                        checkedTrackColor = GoldAccent
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Availability Selector
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AvailabilityFilter.values().forEach { avail ->
                    val isSelected = filterDraft.availability == avail
                    FilterChip(
                        selected = isSelected,
                        onClick = { filterDraft = filterDraft.copy(availability = avail) },
                        label = { Text(avail.displayName, fontSize = 12.sp) },
                        colors = customChipColors(isSelected),
                        border = customChipBorder(isSelected)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(18.dp))

            // 3. Color Filter
            SectionTitle(title = "Primary & Accent Colors")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                WardrobeTaxonomy.PrimaryColors.forEach { color ->
                    val isSelected = filterDraft.selectedColors.contains(color)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            val newSet = if (isSelected) {
                                filterDraft.selectedColors - color
                            } else {
                                filterDraft.selectedColors + color
                            }
                            filterDraft = filterDraft.copy(selectedColors = newSet)
                        },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(WardrobeTaxonomy.getColorPreview(color))
                                    .border(0.5.dp, SlateBorder, CircleShape)
                            )
                        },
                        label = { Text(color, fontSize = 12.sp) },
                        colors = customChipColors(isSelected),
                        border = customChipBorder(isSelected)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(18.dp))

            // 4. Fit Filter
            SectionTitle(title = "Fit")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                WardrobeTaxonomy.Fits.forEach { fit ->
                    val isSelected = filterDraft.selectedFits.contains(fit)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            val newSet = if (isSelected) filterDraft.selectedFits - fit else filterDraft.selectedFits + fit
                            filterDraft = filterDraft.copy(selectedFits = newSet)
                        },
                        label = { Text(fit, fontSize = 12.sp) },
                        colors = customChipColors(isSelected),
                        border = customChipBorder(isSelected)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(18.dp))

            // 5. Style Aesthetics
            SectionTitle(title = "Style Aesthetic")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                WardrobeTaxonomy.Styles.forEach { style ->
                    val isSelected = filterDraft.selectedStyles.contains(style)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            val newSet = if (isSelected) filterDraft.selectedStyles - style else filterDraft.selectedStyles + style
                            filterDraft = filterDraft.copy(selectedStyles = newSet)
                        },
                        label = { Text(style, fontSize = 12.sp) },
                        colors = customChipColors(isSelected),
                        border = customChipBorder(isSelected)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(18.dp))

            // 6. Formality
            SectionTitle(title = "Formality")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                WardrobeTaxonomy.Formalities.forEach { formality ->
                    val isSelected = filterDraft.selectedFormalities.contains(formality)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            val newSet = if (isSelected) filterDraft.selectedFormalities - formality else filterDraft.selectedFormalities + formality
                            filterDraft = filterDraft.copy(selectedFormalities = newSet)
                        },
                        label = { Text(formality, fontSize = 12.sp) },
                        colors = customChipColors(isSelected),
                        border = customChipBorder(isSelected)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(18.dp))

            // 7. Season
            SectionTitle(title = "Season")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                WardrobeTaxonomy.Seasons.forEach { season ->
                    val isSelected = filterDraft.selectedSeasons.contains(season)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            val newSet = if (isSelected) filterDraft.selectedSeasons - season else filterDraft.selectedSeasons + season
                            filterDraft = filterDraft.copy(selectedSeasons = newSet)
                        },
                        label = { Text(season, fontSize = 12.sp) },
                        colors = customChipColors(isSelected),
                        border = customChipBorder(isSelected)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(18.dp))

            // 8. Occasions
            SectionTitle(title = "Occasion")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                WardrobeTaxonomy.Occasions.forEach { occasion ->
                    val isSelected = filterDraft.selectedOccasions.contains(occasion)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            val newSet = if (isSelected) filterDraft.selectedOccasions - occasion else filterDraft.selectedOccasions + occasion
                            filterDraft = filterDraft.copy(selectedOccasions = newSet)
                        },
                        label = { Text(occasion, fontSize = 12.sp) },
                        colors = customChipColors(isSelected),
                        border = customChipBorder(isSelected)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Apply Button
            Button(
                onClick = { onApply(filterDraft) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("apply_filters_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldAccent,
                    contentColor = ObsidianBlack
                )
            ) {
                Text(
                    text = "Apply Filters (${filterDraft.activeFilterCount})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = GoldAccent,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun customChipColors(isSelected: Boolean) = FilterChipDefaults.filterChipColors(
    containerColor = DarkCharcoal,
    labelColor = PlatinumWhite,
    selectedContainerColor = GoldAccent,
    selectedLabelColor = ObsidianBlack,
    selectedLeadingIconColor = ObsidianBlack
)

@Composable
private fun customChipBorder(isSelected: Boolean) = FilterChipDefaults.filterChipBorder(
    borderColor = SlateBorder,
    selectedBorderColor = GoldAccent,
    enabled = true,
    selected = isSelected
)
