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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.WardrobeItem
import com.example.data.model.WardrobeTaxonomy
import com.example.ui.WardrobeViewModel
import com.example.ui.components.ItemImagePreview
import com.example.ui.theme.AmberFavorite
import com.example.ui.theme.CrimsonUnavailable
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.DarkGunmetal
import com.example.ui.theme.EmeraldAvailable
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentDark
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PlatinumDark
import com.example.ui.theme.PlatinumMuted
import com.example.ui.theme.PlatinumWhite
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateLightBorder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ItemDetailsScreen(
    itemId: Long,
    viewModel: WardrobeViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit
) {
    val context = LocalContext.current
    val allItems by viewModel.allItems.collectAsStateWithLifecycle()
    val item = allItems.find { it.id == itemId }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    if (item == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianBlack),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Item not found or deleted", color = PlatinumMuted)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(onClick = onNavigateBack) {
                    Text("Return", color = GoldAccent)
                }
            }
        }
        return
    }

    val dateFormatter = remember { SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()) }
    val dateAddedStr = dateFormatter.format(Date(item.createdAt))
    val dateUpdatedStr = dateFormatter.format(Date(item.updatedAt))

    Scaffold(
        containerColor = ObsidianBlack,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = PlatinumWhite,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("item_details_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PlatinumWhite
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleFavorite(item) },
                        modifier = Modifier.testTag("details_favorite_toggle_btn")
                    ) {
                        Icon(
                            imageVector = if (item.favorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Toggle Favorite",
                            tint = if (item.favorite) AmberFavorite else PlatinumMuted
                        )
                    }
                    IconButton(
                        onClick = { onNavigateToEdit(item.id) },
                        modifier = Modifier.testTag("details_edit_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Item",
                            tint = GoldAccent
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
                .padding(bottom = 60.dp)
        ) {
            // 1. Hero Large Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.05f)
                    .background(DarkGunmetal)
            ) {
                ItemImagePreview(
                    item = item,
                    modifier = Modifier.fillMaxSize()
                )

                // Availability Badge
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (item.available) EmeraldAvailable.copy(alpha = 0.9f) else CrimsonUnavailable.copy(alpha = 0.9f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (item.available) "ACTIVE IN WARDROBE" else "CURRENTLY UNAVAILABLE",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                // Category & Name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.category.uppercase(),
                        color = GoldAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    )
                    if (item.subcategory.isNotBlank()) {
                        Text(
                            text = item.subcategory,
                            color = PlatinumMuted,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.name,
                    color = PlatinumWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Color Chips
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Colors:", color = PlatinumMuted, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(WardrobeTaxonomy.getColorPreview(item.primaryColor))
                            .border(1.dp, SlateBorder, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.primaryColor,
                        color = PlatinumWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (item.secondaryColors.isNotBlank()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "· ${item.secondaryColors}",
                            color = PlatinumMuted,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = SlateBorder)
                Spacer(modifier = Modifier.height(20.dp))

                // 2. Structured Attribute Grid
                Text(
                    text = "ITEM SPECIFICATIONS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SpecCard(title = "FIT", value = item.fit, modifier = Modifier.weight(1f))
                        SpecCard(title = "STYLE", value = item.style, modifier = Modifier.weight(1f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SpecCard(title = "FORMALITY", value = item.formality, modifier = Modifier.weight(1f))
                        SpecCard(title = "SEASON", value = item.season, modifier = Modifier.weight(1f))
                    }
                }

                if (item.occasions.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Occasions", color = PlatinumMuted, fontSize = 12.sp, modifier = Modifier.padding(bottom = 6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item.occasions.split(",").map { it.trim() }.filter { it.isNotBlank() }.forEach { occ ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DarkGunmetal)
                                    .border(1.dp, SlateBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(text = occ, color = PlatinumWhite, fontSize = 12.sp)
                            }
                        }
                    }
                }

                // 3. Notes Section
                if (item.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = SlateBorder)
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "NOTES & FABRIC CARE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.notes,
                        color = PlatinumWhite,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }

                // 4. Metadata Timestamps
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Cataloged: $dateAddedStr", color = PlatinumDark, fontSize = 11.sp)
                    Text(text = "Updated: $dateUpdatedStr", color = PlatinumDark, fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = SlateBorder)
                Spacer(modifier = Modifier.height(24.dp))

                // 5. Future AI Intelligence Placeholders
                Text(
                    text = "FUTURE AI EXPANSION SLOTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkGunmetal),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SlateBorder))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        FutureSlotRow(
                            icon = Icons.Default.AutoAwesome,
                            title = "AI Outfit Combinations",
                            status = "Phase 2 AI Engine Hook Ready"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        FutureSlotRow(
                            icon = Icons.Default.History,
                            title = "Wear Frequency & Cost-Per-Wear",
                            status = "Tracking Schema Enabled (0 wears)"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        FutureSlotRow(
                            icon = Icons.Default.Style,
                            title = "Color & Silhouette Harmony Score",
                            status = "Vector Embedding Ready"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // 6. Action Buttons: Edit, Toggle Availability, Delete
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onNavigateToEdit(item.id) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("action_edit_garment"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldAccent,
                            contentColor = ObsidianBlack
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Edit Item", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.toggleAvailability(item)
                            val status = if (!item.available) "active" else "marked unavailable"
                            Toast.makeText(context, "Item $status", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("action_toggle_availability"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = PlatinumWhite
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SlateLightBorder)
                    ) {
                        Icon(
                            imageVector = if (item.available) Icons.Default.NotInterested else Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (item.available) CrimsonUnavailable else EmeraldAvailable
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (item.available) "Deactivate" else "Activate",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Delete Permanently
                OutlinedButton(
                    onClick = { showDeleteConfirmDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("action_delete_item"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonUnavailable),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonUnavailable.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(16.dp),
                        tint = CrimsonUnavailable
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Delete Permanently",
                        fontWeight = FontWeight.Bold,
                        color = CrimsonUnavailable
                    )
                }
            }
        }
    }

    // Deletion Confirmation Dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            containerColor = DarkGunmetal,
            title = {
                Text(
                    text = "Delete Garment?",
                    color = PlatinumWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to permanently delete \"${item.name}\"? This action cannot be undone.",
                    color = PlatinumMuted,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        viewModel.deleteItem(item) {
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CrimsonUnavailable,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("confirm_delete_btn")
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmDialog = false }
                ) {
                    Text("Cancel", color = PlatinumMuted)
                }
            }
        )
    }
}

@Composable
private fun SpecCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(DarkGunmetal)
            .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = title,
                color = PlatinumMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = PlatinumWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun FutureSlotRow(
    icon: ImageVector,
    title: String,
    status: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(DarkCharcoal),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GoldAccent,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                color = PlatinumWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = status,
                color = PlatinumMuted,
                fontSize = 11.sp
            )
        }
    }
}
