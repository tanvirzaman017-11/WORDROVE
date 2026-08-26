package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.DryCleaning
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.LocalMall
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.RollerSkating
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.WardrobeItem
import com.example.data.model.WardrobeTaxonomy
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.DarkGunmetal
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.PlatinumMuted

@Composable
fun ItemImagePreview(
    item: WardrobeItem,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val imageUri = item.imageUri

    val resolvedModel: Any? = when {
        imageUri.startsWith("res://drawable/") -> {
            val resName = imageUri.removePrefix("res://drawable/")
            val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
            if (resId != 0) resId else null
        }
        imageUri.isNotBlank() -> imageUri
        else -> null
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(DarkGunmetal),
        contentAlignment = Alignment.Center
    ) {
        if (resolvedModel != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(resolvedModel)
                    .crossfade(true)
                    .build(),
                contentDescription = item.name,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Elegant Luxury Placeholder with Category Icon & Color swatch
            val categoryIcon = getCategoryIcon(item.category)
            val colorPreview = WardrobeTaxonomy.getColorPreview(item.primaryColor)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                DarkCharcoal,
                                colorPreview.copy(alpha = 0.25f),
                                DarkGunmetal
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcon,
                    contentDescription = item.category,
                    tint = GoldAccent.copy(alpha = 0.8f),
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

fun getCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        "t-shirts" -> Icons.Outlined.Checkroom
        "shirts" -> Icons.Outlined.Checkroom
        "pants / jeans", "pants", "jeans" -> Icons.Outlined.DryCleaning
        "outerwear" -> Icons.Outlined.Checkroom
        "shoes" -> Icons.Outlined.RollerSkating
        "eyewear" -> Icons.Outlined.Visibility
        "watches" -> Icons.Outlined.Watch
        "belts" -> Icons.Outlined.FitnessCenter
        "bags" -> Icons.Outlined.ShoppingBag
        "accessories" -> Icons.Outlined.Diamond
        "fragrance" -> Icons.Outlined.Opacity
        else -> Icons.Outlined.LocalMall
    }
}
