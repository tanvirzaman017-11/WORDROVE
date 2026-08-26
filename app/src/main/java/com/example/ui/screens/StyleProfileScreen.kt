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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.StyleProfile
import com.example.data.model.WardrobeTaxonomy
import com.example.ui.WardrobeViewModel
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
fun StyleProfileScreen(
    viewModel: WardrobeViewModel
) {
    val context = LocalContext.current
    val currentProfile by viewModel.styleProfile.collectAsStateWithLifecycle()

    var selectedAesthetics by remember(currentProfile) {
        mutableStateOf(currentProfile.styleAesthetics.split(",").map { it.trim() }.filter { it.isNotBlank() }.toSet())
    }
    var selectedPreferredColors by remember(currentProfile) {
        mutableStateOf(currentProfile.preferredColors.split(",").map { it.trim() }.filter { it.isNotBlank() }.toSet())
    }
    var avoidColorsText by remember(currentProfile) { mutableStateOf(currentProfile.avoidColors) }
    var selectedFits by remember(currentProfile) {
        mutableStateOf(currentProfile.preferredFits.split(",").map { it.trim() }.filter { it.isNotBlank() }.toSet())
    }

    var preferredSilhouettes by remember(currentProfile) { mutableStateOf(currentProfile.preferredSilhouettes) }
    var preferredClothingTypes by remember(currentProfile) { mutableStateOf(currentProfile.preferredClothingTypes) }
    var preferredFootwear by remember(currentProfile) { mutableStateOf(currentProfile.preferredFootwear) }
    var preferredAccessories by remember(currentProfile) { mutableStateOf(currentProfile.preferredAccessories) }
    var styleGoals by remember(currentProfile) { mutableStateOf(currentProfile.styleGoals) }
    var personalRules by remember(currentProfile) { mutableStateOf(currentProfile.personalRules) }

    Scaffold(
        containerColor = ObsidianBlack,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "STYLE PROFILE & PREFERENCES",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = PlatinumWhite
                    )
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
                .padding(bottom = 100.dp, start = 20.dp, end = 20.dp, top = 16.dp)
        ) {
            // Header card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGunmetal),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(GoldAccentDark.copy(alpha = 0.5f), SlateBorder))
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(DarkCharcoal),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Style,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "AI Style Architecture",
                            color = PlatinumWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "These parameters anchor future AI outfit curation and wardrobe gap analysis.",
                            color = PlatinumMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Style Aesthetics
            SectionHeader(title = "CORE STYLE AESTHETICS")
            Text(
                text = "Select your target aesthetic archetypes:",
                color = PlatinumMuted,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                WardrobeTaxonomy.Styles.forEach { style ->
                    val isSelected = selectedAesthetics.contains(style)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedAesthetics = if (isSelected) selectedAesthetics - style else selectedAesthetics + style
                        },
                        label = { Text(style, fontSize = 12.sp) },
                        colors = customChipColors(isSelected),
                        border = customChipBorder(isSelected)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(24.dp))

            // 2. Preferred Colors & Avoid Colors
            SectionHeader(title = "COLOR PALETTE PREFERENCES")
            Text(
                text = "Preferred Palette:",
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
                    val isSelected = selectedPreferredColors.contains(color)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedPreferredColors = if (isSelected) selectedPreferredColors - color else selectedPreferredColors + color
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

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = avoidColorsText,
                onValueChange = { avoidColorsText = it },
                label = { Text("Colors to Avoid") },
                placeholder = { Text("e.g. Neon Yellow, Bright Orange, Hot Pink") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_avoid_colors"),
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

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(24.dp))

            // 3. Preferred Fits
            SectionHeader(title = "PREFERRED FITS")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                WardrobeTaxonomy.Fits.forEach { fit ->
                    val isSelected = selectedFits.contains(fit)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedFits = if (isSelected) selectedFits - fit else selectedFits + fit
                        },
                        label = { Text(fit, fontSize = 12.sp) },
                        colors = customChipColors(isSelected),
                        border = customChipBorder(isSelected)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(24.dp))

            // 4. Silhouettes & Garment Preferences
            SectionHeader(title = "SILHOUETTES & PIECES")

            OutlinedTextField(
                value = preferredSilhouettes,
                onValueChange = { preferredSilhouettes = it },
                label = { Text("Preferred Silhouettes") },
                placeholder = { Text("e.g. Boxy Heavyweight Top + Straight Trousers, Cropped Jacket + Wide Leg Denim") },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = preferredClothingTypes,
                onValueChange = { preferredClothingTypes = it },
                label = { Text("Key Clothing Essentials") },
                placeholder = { Text("e.g. Heavyweight Boxy Tees, Wool Overcoats, Oxford Shirts, Raw Denim") },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = preferredFootwear,
                onValueChange = { preferredFootwear = it },
                label = { Text("Preferred Footwear") },
                placeholder = { Text("e.g. Chelsea Boots, Minimal White Leather Sneakers, Penny Loafers") },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = preferredAccessories,
                onValueChange = { preferredAccessories = it },
                label = { Text("Preferred Accessories") },
                placeholder = { Text("e.g. Minimalist Steel Chronograph, Matte Leather Belt, Silver Ring") },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(24.dp))

            // 5. Goals & Personal Rules
            SectionHeader(title = "STYLE GOALS & PERSONAL RULES")

            OutlinedTextField(
                value = styleGoals,
                onValueChange = { styleGoals = it },
                label = { Text("Style Goals") },
                placeholder = { Text("e.g. Clean, Premium, Masculine, Modern, Attractive, Intentional") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_style_goals"),
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

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = personalRules,
                onValueChange = { personalRules = it },
                label = { Text("Personal Fashion Rules") },
                placeholder = { Text("e.g.\n1. Always prioritize heavyweight natural fabrics.\n2. Ensure footwear is clean and structured.") },
                minLines = 3,
                maxLines = 6,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_personal_rules"),
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

            Spacer(modifier = Modifier.height(30.dp))

            // Save Profile Button
            Button(
                onClick = {
                    val updated = StyleProfile(
                        id = 1,
                        styleAesthetics = selectedAesthetics.joinToString(", "),
                        preferredColors = selectedPreferredColors.joinToString(", "),
                        avoidColors = avoidColorsText.trim(),
                        preferredFits = selectedFits.joinToString(", "),
                        preferredSilhouettes = preferredSilhouettes.trim(),
                        preferredClothingTypes = preferredClothingTypes.trim(),
                        preferredFootwear = preferredFootwear.trim(),
                        preferredAccessories = preferredAccessories.trim(),
                        styleGoals = styleGoals.trim(),
                        personalRules = personalRules.trim(),
                        updatedAt = System.currentTimeMillis()
                    )

                    viewModel.saveStyleProfile(updated) {
                        Toast.makeText(context, "Style Profile updated successfully", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("save_style_profile_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldAccent,
                    contentColor = ObsidianBlack
                )
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Save Style Profile", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = GoldAccent,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 6.dp)
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
