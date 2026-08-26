package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.WardrobeViewModel
import com.example.ui.theme.CrimsonUnavailable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: WardrobeViewModel
) {
    val context = LocalContext.current
    var showClearConfirmDialog by remember { mutableStateOf(false) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = ObsidianBlack,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SETTINGS & SYSTEM",
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
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp)
        ) {
            // App Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGunmetal),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SlateBorder))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(DarkCharcoal)
                                .border(1.dp, GoldAccentDark, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Vanguard Wardrobe",
                                color = PlatinumWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Version 1.0.0 (Phase 1 Foundation)",
                                color = GoldAccent,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Architected for high-fidelity personal style management, structured taxonomy filtering, and future AI intelligence systems.",
                        color = PlatinumMuted,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Data Management Section
            Text(
                text = "DATA MANAGEMENT",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGunmetal),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SlateBorder))
            ) {
                Column {
                    SettingsActionRow(
                        title = "Reset to Curated Sample Data",
                        subtitle = "Restores sample menswear pieces and default style profile",
                        icon = Icons.Default.RestartAlt,
                        iconTint = GoldAccent,
                        onClick = { showResetConfirmDialog = true },
                        testTag = "settings_reset_data_btn"
                    )

                    HorizontalDivider(color = SlateBorder)

                    SettingsActionRow(
                        title = "Clear All Wardrobe Data",
                        subtitle = "Permanently removes all clothing items and records",
                        icon = Icons.Default.DeleteSweep,
                        iconTint = CrimsonUnavailable,
                        onClick = { showClearConfirmDialog = true },
                        testTag = "settings_clear_data_btn"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Future Cloud & Export Architecture
            Text(
                text = "INTELLIGENCE & SYNC PIPELINES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGunmetal),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SlateBorder))
            ) {
                Column {
                    SettingsActionRow(
                        title = "Export Wardrobe JSON",
                        subtitle = "Schema ready for Phase 2 data export",
                        icon = Icons.Default.FileDownload,
                        iconTint = PlatinumMuted,
                        onClick = {
                            Toast.makeText(context, "Wardrobe data model ready for JSON serialization", Toast.LENGTH_SHORT).show()
                        },
                        testTag = "settings_export_json"
                    )

                    HorizontalDivider(color = SlateBorder)

                    SettingsActionRow(
                        title = "Cloud & Cross-Device Sync",
                        subtitle = "Offline-first Room database with cloud-ready UUID architecture",
                        icon = Icons.Default.CloudSync,
                        iconTint = PlatinumMuted,
                        onClick = {
                            Toast.makeText(context, "Offline-first local storage active", Toast.LENGTH_SHORT).show()
                        },
                        testTag = "settings_cloud_sync"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Architecture Info
            Text(
                text = "PHASE 1 ARCHITECTURAL NOTES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGunmetal),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SlateBorder))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Clean Data Modeling: Fully typed entities for Garments and Style Profiles with zero loss of fidelity.",
                        color = PlatinumMuted,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "2. No Hallucinations: App strictly references on-device database records.",
                        color = PlatinumMuted,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "3. Modular Extensibility: Ready for seamless addition of AI Outfit Generation and Shopping Gap Analysis.",
                        color = PlatinumMuted,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }

    // Clear Confirmation Dialog
    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            containerColor = DarkGunmetal,
            title = {
                Text(
                    text = "Clear All Wardrobe Data?",
                    color = PlatinumWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "This will permanently remove all cataloged wardrobe pieces and items from local storage. Are you sure?",
                    color = PlatinumMuted,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showClearConfirmDialog = false
                        viewModel.clearAllData {
                            Toast.makeText(context, "Wardrobe data cleared", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CrimsonUnavailable,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("confirm_clear_all_btn")
                ) {
                    Text("Clear All", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text("Cancel", color = PlatinumMuted)
                }
            }
        )
    }

    // Reset Confirmation Dialog
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            containerColor = DarkGunmetal,
            title = {
                Text(
                    text = "Reset to Sample Data?",
                    color = PlatinumWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "This will replace your current items with curated Vanguard sample wardrobe essentials.",
                    color = PlatinumMuted,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResetConfirmDialog = false
                        viewModel.resetToSampleData {
                            Toast.makeText(context, "Restored curated sample wardrobe", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldAccent,
                        contentColor = ObsidianBlack
                    ),
                    modifier = Modifier.testTag("confirm_reset_sample_btn")
                ) {
                    Text("Reset", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
                    Text("Cancel", color = PlatinumMuted)
                }
            }
        )
    }
}

@Composable
private fun SettingsActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(DarkCharcoal),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = PlatinumWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = PlatinumMuted,
                fontSize = 12.sp
            )
        }
    }
}
