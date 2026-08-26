package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.WardrobeItem
import com.example.data.model.WardrobeTaxonomy
import com.example.ui.WardrobeViewModel
import com.example.ui.components.ItemCard
import com.example.ui.components.ItemImagePreview
import com.example.ui.theme.AmberFavorite
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.DarkGunmetal
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentDark
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PlatinumMuted
import com.example.ui.theme.PlatinumWhite
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateLightBorder

@Composable
fun HomeScreen(
    viewModel: WardrobeViewModel,
    onNavigateToWardrobe: () -> Unit,
    onNavigateToAddItem: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToItemDetails: (Long) -> Unit
) {
    val totalItems by viewModel.totalItemCount.collectAsStateWithLifecycle()
    val totalCategories by viewModel.categoryCount.collectAsStateWithLifecycle()
    val favoriteCount by viewModel.favoriteCount.collectAsStateWithLifecycle()
    val recentItems by viewModel.recentItems.collectAsStateWithLifecycle()
    val favoriteItems by viewModel.favoriteItems.collectAsStateWithLifecycle()
    val styleProfile by viewModel.styleProfile.collectAsStateWithLifecycle()

    var futureFeatureDialogTitle by remember { mutableStateOf<String?>(null) }
    var futureFeatureDialogDesc by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        // 1. Header with Brand & Greeting
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "VANGUARD",
                    color = GoldAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Personal Style System",
                    color = PlatinumWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(DarkGunmetal)
                    .border(1.dp, SlateLightBorder, CircleShape)
                    .clickable { onNavigateToProfile() }
                    .padding(8.dp)
                    .testTag("home_profile_avatar_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Style Profile",
                    tint = GoldAccent,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 2. Summary Metrics Dashboard Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .testTag("wardrobe_summary_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkGunmetal),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(GoldAccentDark.copy(alpha = 0.5f), SlateBorder))
            )
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "WARDROBE METRICS",
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Phase 1 Foundation",
                        color = PlatinumMuted,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricColumn(
                        label = "Total Items",
                        value = totalItems.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    MetricColumn(
                        label = "Categories",
                        value = totalCategories.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    MetricColumn(
                        label = "Favorites",
                        value = favoriteCount.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Quick Action Buttons
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "QUICK ACTIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = PlatinumMuted,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionButton(
                    title = "Add Item",
                    subtitle = "Catalog Garment",
                    icon = Icons.Default.Add,
                    isPrimary = true,
                    onClick = onNavigateToAddItem,
                    modifier = Modifier.weight(1f),
                    testTag = "quick_action_add"
                )

                QuickActionButton(
                    title = "Wardrobe",
                    subtitle = "Browse & Filter",
                    icon = Icons.Default.Checkroom,
                    isPrimary = false,
                    onClick = onNavigateToWardrobe,
                    modifier = Modifier.weight(1f),
                    testTag = "quick_action_browse"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Style Profile Quick Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkGunmetal)
                    .border(1.dp, SlateBorder, RoundedCornerShape(12.dp))
                    .clickable { onNavigateToProfile() }
                    .padding(14.dp)
                    .testTag("quick_action_style_profile"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(DarkCharcoal),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Style,
                        contentDescription = "Style Profile",
                        tint = GoldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Manage Style Profile",
                        color = PlatinumWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Aesthetics: ${styleProfile.styleAesthetics.take(28)}...",
                        color = PlatinumMuted,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Open",
                    tint = PlatinumMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Future AI Capabilities Placeholders (Explicitly Marked Coming in Phase 2)
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AI INTELLIGENCE MODULES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlatinumMuted,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "PHASE 2 ROADMAP",
                    color = GoldAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FutureFeatureCard(
                    title = "What Should I Wear?",
                    subtitle = "Context Outfit Generator",
                    icon = Icons.Default.AutoAwesome,
                    badgeText = "COMING SOON",
                    onClick = {
                        futureFeatureDialogTitle = "AI Outfit Generator (Phase 2)"
                        futureFeatureDialogDesc = "In Phase 2, the AI Outfit Engine will synthesize your wardrobe database, style profile aesthetics, current weather, and event formality to craft harmonic daily outfit combinations. All Phase 1 data models are already structured for this."
                    },
                    modifier = Modifier.weight(1f),
                    testTag = "future_feature_outfit"
                )

                FutureFeatureCard(
                    title = "Should I Buy This?",
                    subtitle = "Wardrobe Gap Advisor",
                    icon = Icons.Default.ShoppingBag,
                    badgeText = "COMING SOON",
                    onClick = {
                        futureFeatureDialogTitle = "AI Shopping Advisor (Phase 2)"
                        futureFeatureDialogDesc = "In Phase 2, upload any prospective garment photo to evaluate compatibility scores against your existing wardrobe, detect color harmony, identify wardrobe gaps, and calculate versatility index before purchasing."
                    },
                    modifier = Modifier.weight(1f),
                    testTag = "future_feature_shopping"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Recently Added Garments
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECENTLY ADDED",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlatinumMuted,
                    letterSpacing = 1.sp
                )
                if (recentItems.isNotEmpty()) {
                    Text(
                        text = "View All",
                        color = GoldAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable { onNavigateToWardrobe() }
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (recentItems.isEmpty()) {
                EmptySectionCard(
                    title = "No items in wardrobe yet",
                    desc = "Add your first piece of clothing to initiate your personal collection.",
                    buttonText = "+ Add First Item",
                    onAction = onNavigateToAddItem
                )
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recentItems, key = { it.id }) { item ->
                        MiniItemCard(
                            item = item,
                            onClick = { onNavigateToItemDetails(item.id) },
                            onToggleFavorite = { viewModel.toggleFavorite(item) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 6. Favorite Items Section
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FAVORITE PIECES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlatinumMuted,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (favoriteItems.isEmpty()) {
                EmptySectionCard(
                    title = "No favorite items yet",
                    desc = "Tap the heart icon on any wardrobe piece to pin your key foundation items here.",
                    buttonText = "Browse Wardrobe",
                    onAction = onNavigateToWardrobe
                )
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favoriteItems, key = { it.id }) { item ->
                        MiniItemCard(
                            item = item,
                            onClick = { onNavigateToItemDetails(item.id) },
                            onToggleFavorite = { viewModel.toggleFavorite(item) }
                        )
                    }
                }
            }
        }
    }

    // Modal explaining future Phase 2 capability
    if (futureFeatureDialogTitle != null && futureFeatureDialogDesc != null) {
        AlertDialog(
            onDismissRequest = {
                futureFeatureDialogTitle = null
                futureFeatureDialogDesc = null
            },
            containerColor = DarkGunmetal,
            icon = {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Phase 2 AI",
                    tint = GoldAccent
                )
            },
            title = {
                Text(
                    text = futureFeatureDialogTitle ?: "",
                    color = PlatinumWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = futureFeatureDialogDesc ?: "",
                    color = PlatinumMuted,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        futureFeatureDialogTitle = null
                        futureFeatureDialogDesc = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldAccent,
                        contentColor = ObsidianBlack
                    )
                ) {
                    Text("Understood", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun MetricColumn(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PlatinumWhite
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = PlatinumMuted
        )
    }
}

@Composable
private fun QuickActionButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isPrimary: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isPrimary) GoldAccent else DarkGunmetal)
            .border(
                1.dp,
                if (isPrimary) GoldAccentDark else SlateBorder,
                RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(14.dp)
            .testTag(testTag)
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isPrimary) ObsidianBlack else GoldAccent,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = if (isPrimary) ObsidianBlack else PlatinumWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = if (isPrimary) ObsidianBlack.copy(alpha = 0.8f) else PlatinumMuted,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun FutureFeatureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(DarkGunmetal.copy(alpha = 0.6f))
            .border(1.dp, SlateBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(12.dp)
            .testTag(testTag)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = GoldAccent.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(DarkCharcoal)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = GoldAccent,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = PlatinumWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = PlatinumMuted,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MiniItemCard(
    item: WardrobeItem,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(DarkGunmetal)
            .border(1.dp, SlateBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                ItemImagePreview(
                    item = item,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = if (item.favorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (item.favorite) AmberFavorite else PlatinumMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = item.category.uppercase(),
                    color = GoldAccent,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.name,
                    color = PlatinumWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun EmptySectionCard(
    title: String,
    desc: String,
    buttonText: String,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGunmetal),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SlateBorder))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = PlatinumWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                color = PlatinumMuted,
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onAction,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldAccent),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccentDark)
            ) {
                Text(text = buttonText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
