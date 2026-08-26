package com.example.ui.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.DarkGunmetal
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PlatinumDark
import com.example.ui.theme.PlatinumMuted
import com.example.ui.theme.PlatinumWhite
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateLightBorder
import java.io.File
import java.io.FileOutputStream

@Composable
fun ImagePickerSection(
    imageUri: String,
    onImageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showPresetDialog by remember { mutableStateOf(false) }

    // Gallery Picker launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            onImageSelected(it.toString())
        }
    }

    // Camera Capture launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            try {
                val file = File(context.cacheDir, "wardrobe_captured_${System.currentTimeMillis()}.jpg")
                val fos = FileOutputStream(file)
                it.compress(Bitmap.CompressFormat.JPEG, 90, fos)
                fos.flush()
                fos.close()
                onImageSelected(file.absolutePath)
            } catch (e: Exception) {
                // Fallback to memory
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "ITEM IMAGE",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAccent,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(DarkCharcoal)
                .border(1.dp, SlateBorder, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (imageUri.isNotBlank()) {
                // Image preview with resolved source
                val resolvedModel: Any = when {
                    imageUri.startsWith("res://drawable/") -> {
                        val resName = imageUri.removePrefix("res://drawable/")
                        val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
                        if (resId != 0) resId else imageUri
                    }
                    else -> imageUri
                }

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(resolvedModel)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Selected Item Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Overlay Controls
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(DarkGunmetal.copy(alpha = 0.85f))
                            .size(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { onImageSelected("") },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove Image",
                                tint = PlatinumWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            } else {
                // Placeholder prompt
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(DarkGunmetal)
                            .border(1.dp, SlateLightBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Add image",
                            tint = GoldAccent,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Upload garment or accessory photo",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = PlatinumWhite
                    )
                    Text(
                        text = "Choose from gallery, camera, or studio presets",
                        fontSize = 11.sp,
                        color = PlatinumMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .testTag("upload_gallery_btn"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PlatinumWhite
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "Gallery",
                    modifier = Modifier.size(16.dp),
                    tint = GoldAccent
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Gallery", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            OutlinedButton(
                onClick = { cameraLauncher.launch() },
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .testTag("take_photo_btn"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PlatinumWhite
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera",
                    modifier = Modifier.size(16.dp),
                    tint = GoldAccent
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Camera", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            OutlinedButton(
                onClick = { showPresetDialog = true },
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .testTag("preset_photos_btn"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PlatinumWhite
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Icon(
                    imageVector = Icons.Default.Collections,
                    contentDescription = "Presets",
                    modifier = Modifier.size(16.dp),
                    tint = GoldAccent
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Presets", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }

    if (showPresetDialog) {
        val presets = listOf(
            Triple("Heavyweight Black Tee", "res://drawable/item_black_tee_1787752237704", "T-Shirt"),
            Triple("Leather Biker Jacket", "res://drawable/item_leather_jacket_1787752250093", "Outerwear"),
            Triple("Suede Chelsea Boots", "res://drawable/item_chelsea_boots_1787752265350", "Shoes")
        )

        AlertDialog(
            onDismissRequest = { showPresetDialog = false },
            containerColor = DarkGunmetal,
            title = {
                Text(
                    text = "Select Studio Preset Asset",
                    color = PlatinumWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    presets.forEach { (name, resUri, cat) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkCharcoal)
                                .border(1.dp, if (imageUri == resUri) GoldAccent else SlateBorder, RoundedCornerShape(12.dp))
                                .clickable {
                                    onImageSelected(resUri)
                                    showPresetDialog = false
                                }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val resName = resUri.removePrefix("res://drawable/")
                            val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)

                            AsyncImage(
                                model = ImageRequest.Builder(context).data(resId).build(),
                                contentDescription = name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = name, color = PlatinumWhite, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text(text = cat, color = GoldAccent, fontSize = 11.sp)
                            }
                            if (imageUri == resUri) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = GoldAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPresetDialog = false }) {
                    Text("Close", color = GoldAccent)
                }
            }
        )
    }
}
