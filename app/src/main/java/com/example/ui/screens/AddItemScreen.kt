package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WardrobeItem
import com.example.data.model.WardrobeTaxonomy
import com.example.ui.WardrobeViewModel
import com.example.ui.components.ImagePickerSection
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddItemScreen(
    viewModel: WardrobeViewModel,
    onNavigateBack: () -> Unit,
    onItemSaved: (Long) -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(WardrobeTaxonomy.Categories.first()) }
    var selectedSubcategory by remember { mutableStateOf(WardrobeTaxonomy.SubcategoriesByCategory[WardrobeTaxonomy.Categories.first()]?.first() ?: "") }
    var primaryColor by remember { mutableStateOf("Black") }
    var secondaryColor by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf("") }

    var fit by remember { mutableStateOf("Regular") }
    var style by remember { mutableStateOf("Minimal") }
    var formality by remember { mutableStateOf("Casual") }
    var season by remember { mutableStateOf("All Season") }
    var selectedOccasions by remember { mutableStateOf(setOf("Daily")) }
    var isFavorite by remember { mutableStateOf(false) }
    var isAvailable by remember { mutableStateOf(true) }
    var notes by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var subcategoryDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = ObsidianBlack,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ADD WARDROBE ITEM",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = PlatinumWhite
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("add_item_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PlatinumWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkGunmetal)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(20.dp)
        ) {
            // 1. Image Upload
            ImagePickerSection(
                imageUri = imageUri,
                onImageSelected = { imageUri = it }
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(20.dp))

            // 2. Required Basic Information
            Text(
                text = "BASIC INFORMATION",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Item Name
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (it.isNotBlank()) nameError = false
                },
                label = { Text("Item Name *") },
                placeholder = { Text("e.g. Heavyweight Cashmere Knit") },
                isError = nameError,
                supportingText = {
                    if (nameError) {
                        Text("Item name is required", color = Color(0xFFEF4444))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_item_name"),
                shape = RoundedCornerShape(12.dp),
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

            // Category Selector Dropdown
            Text(
                text = "Category *",
                color = PlatinumMuted,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkGunmetal)
                        .border(1.dp, SlateBorder, RoundedCornerShape(12.dp))
                        .clickable { categoryDropdownExpanded = true }
                        .padding(16.dp)
                        .testTag("select_category_dropdown"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = selectedCategory, color = PlatinumWhite, fontWeight = FontWeight.SemiBold)
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown", tint = GoldAccent)
                }

                DropdownMenu(
                    expanded = categoryDropdownExpanded,
                    onDismissRequest = { categoryDropdownExpanded = false },
                    modifier = Modifier.background(DarkGunmetal)
                ) {
                    WardrobeTaxonomy.Categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat, color = PlatinumWhite) },
                            onClick = {
                                selectedCategory = cat
                                selectedSubcategory = WardrobeTaxonomy.SubcategoriesByCategory[cat]?.firstOrNull() ?: ""
                                categoryDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Subcategory Selector
            val subcategories = WardrobeTaxonomy.SubcategoriesByCategory[selectedCategory] ?: emptyList()
            if (subcategories.isNotEmpty()) {
                Text(
                    text = "Subcategory",
                    color = PlatinumMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkGunmetal)
                            .border(1.dp, SlateBorder, RoundedCornerShape(12.dp))
                            .clickable { subcategoryDropdownExpanded = true }
                            .padding(16.dp)
                            .testTag("select_subcategory_dropdown"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedSubcategory.isNotBlank()) selectedSubcategory else "Select Subcategory",
                            color = PlatinumWhite
                        )
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown", tint = GoldAccent)
                    }

                    DropdownMenu(
                        expanded = subcategoryDropdownExpanded,
                        onDismissRequest = { subcategoryDropdownExpanded = false },
                        modifier = Modifier.background(DarkGunmetal)
                    ) {
                        subcategories.forEach { sub ->
                            DropdownMenuItem(
                                text = { Text(sub, color = PlatinumWhite) },
                                onClick = {
                                    selectedSubcategory = sub
                                    subcategoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Color
            Text(
                text = "Primary Color *",
                color = PlatinumMuted,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                WardrobeTaxonomy.PrimaryColors.forEach { color ->
                    val isSelected = primaryColor == color
                    FilterChip(
                        selected = isSelected,
                        onClick = { primaryColor = color },
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
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = DarkCharcoal,
                            labelColor = PlatinumWhite,
                            selectedContainerColor = GoldAccent,
                            selectedLabelColor = ObsidianBlack
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = SlateBorder,
                            selectedBorderColor = GoldAccent,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Secondary / Accent Color (Optional text/chip)
            OutlinedTextField(
                value = secondaryColor,
                onValueChange = { secondaryColor = it },
                label = { Text("Secondary / Accent Color (Optional)") },
                placeholder = { Text("e.g. Silver, Camel, Gold, White") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_secondary_color"),
                shape = RoundedCornerShape(12.dp),
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

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(20.dp))

            // 3. Optional Structured Attributes
            Text(
                text = "ATTRIBUTES & STYLE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fit
            Text(text = "Fit", color = PlatinumMuted, fontSize = 12.sp, modifier = Modifier.padding(bottom = 6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WardrobeTaxonomy.Fits.forEach { f ->
                    val isSelected = fit == f
                    FilterChip(
                        selected = isSelected,
                        onClick = { fit = f },
                        label = { Text(f, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = DarkCharcoal,
                            labelColor = PlatinumWhite,
                            selectedContainerColor = GoldAccent,
                            selectedLabelColor = ObsidianBlack
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = SlateBorder,
                            selectedBorderColor = GoldAccent,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Style Aesthetic
            Text(text = "Style Aesthetic", color = PlatinumMuted, fontSize = 12.sp, modifier = Modifier.padding(bottom = 6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WardrobeTaxonomy.Styles.forEach { s ->
                    val isSelected = style == s
                    FilterChip(
                        selected = isSelected,
                        onClick = { style = s },
                        label = { Text(s, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = DarkCharcoal,
                            labelColor = PlatinumWhite,
                            selectedContainerColor = GoldAccent,
                            selectedLabelColor = ObsidianBlack
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = SlateBorder,
                            selectedBorderColor = GoldAccent,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Formality
            Text(text = "Formality", color = PlatinumMuted, fontSize = 12.sp, modifier = Modifier.padding(bottom = 6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WardrobeTaxonomy.Formalities.forEach { form ->
                    val isSelected = formality == form
                    FilterChip(
                        selected = isSelected,
                        onClick = { formality = form },
                        label = { Text(form, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = DarkCharcoal,
                            labelColor = PlatinumWhite,
                            selectedContainerColor = GoldAccent,
                            selectedLabelColor = ObsidianBlack
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = SlateBorder,
                            selectedBorderColor = GoldAccent,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Season
            Text(text = "Season", color = PlatinumMuted, fontSize = 12.sp, modifier = Modifier.padding(bottom = 6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WardrobeTaxonomy.Seasons.forEach { se ->
                    val isSelected = season == se
                    FilterChip(
                        selected = isSelected,
                        onClick = { season = se },
                        label = { Text(se, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = DarkCharcoal,
                            labelColor = PlatinumWhite,
                            selectedContainerColor = GoldAccent,
                            selectedLabelColor = ObsidianBlack
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = SlateBorder,
                            selectedBorderColor = GoldAccent,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Occasions (multi-select)
            Text(text = "Occasions", color = PlatinumMuted, fontSize = 12.sp, modifier = Modifier.padding(bottom = 6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WardrobeTaxonomy.Occasions.forEach { occ ->
                    val isSelected = selectedOccasions.contains(occ)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedOccasions = if (isSelected) selectedOccasions - occ else selectedOccasions + occ
                        },
                        label = { Text(occ, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = DarkCharcoal,
                            labelColor = PlatinumWhite,
                            selectedContainerColor = GoldAccent,
                            selectedLabelColor = ObsidianBlack
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = SlateBorder,
                            selectedBorderColor = GoldAccent,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Status Toggles (Favorite & Available)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkGunmetal)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Mark as Favorite", color = PlatinumWhite, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text(text = "Highlight this core staple in your wardrobe", color = PlatinumMuted, fontSize = 11.sp)
                }
                Switch(
                    checked = isFavorite,
                    onCheckedChange = { isFavorite = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ObsidianBlack,
                        checkedTrackColor = GoldAccent
                    ),
                    modifier = Modifier.testTag("switch_favorite")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkGunmetal)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Active Availability", color = PlatinumWhite, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text(text = "Uncheck if garment is at dry cleaners or archived", color = PlatinumMuted, fontSize = 11.sp)
                }
                Switch(
                    checked = isAvailable,
                    onCheckedChange = { isAvailable = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ObsidianBlack,
                        checkedTrackColor = GoldAccent
                    ),
                    modifier = Modifier.testTag("switch_available")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Personal Style & Fabric Notes") },
                placeholder = { Text("e.g. Dry clean only. Best paired with straight wool trousers.") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_notes"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkGunmetal,
                    unfocusedContainerColor = DarkGunmetal,
                    focusedBorderColor = GoldAccent,
                    unfocusedBorderColor = SlateBorder,
                    focusedTextColor = PlatinumWhite,
                    unfocusedTextColor = PlatinumWhite
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Save Action Button
            Button(
                onClick = {
                    if (name.isBlank()) {
                        nameError = true
                        Toast.makeText(context, "Please enter an item name", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val newItem = WardrobeItem(
                        name = name.trim(),
                        category = selectedCategory,
                        subcategory = selectedSubcategory.trim(),
                        primaryColor = primaryColor,
                        secondaryColors = secondaryColor.trim(),
                        imageUri = imageUri,
                        fit = fit,
                        style = style,
                        formality = formality,
                        season = season,
                        occasions = selectedOccasions.joinToString(", "),
                        favorite = isFavorite,
                        available = isAvailable,
                        notes = notes.trim(),
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )

                    viewModel.addItem(newItem) { savedId ->
                        Toast.makeText(context, "Garment saved to wardrobe", Toast.LENGTH_SHORT).show()
                        onItemSaved(savedId)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("submit_add_item_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldAccent,
                    contentColor = ObsidianBlack
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Save Garment to Wardrobe",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
