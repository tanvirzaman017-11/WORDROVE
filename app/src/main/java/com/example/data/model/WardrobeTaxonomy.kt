package com.example.data.model

import androidx.compose.ui.graphics.Color

object WardrobeTaxonomy {
    val Categories = listOf(
        "T-Shirts",
        "Shirts",
        "Pants / Jeans",
        "Outerwear",
        "Shoes",
        "Eyewear",
        "Watches",
        "Belts",
        "Bags",
        "Accessories",
        "Fragrance",
        "Other"
    )

    val SubcategoriesByCategory = mapOf(
        "T-Shirts" to listOf("Heavyweight Crewneck", "Oversized Tee", "Long Sleeve", "Henley", "Graphic Tee", "Tank Top", "Polo Tee"),
        "Shirts" to listOf("Oxford Button-Down", "Linen Shirt", "Flannel Overshirt", "Silk / Camp Collar", "Dress Shirt", "Denim Shirt", "Mandarin Collar"),
        "Pants / Jeans" to listOf("Raw Denim Jeans", "Straight Leg Jeans", "Pleated Trousers", "Tailored Chinos", "Cargo Pants", "Drawstring Trousers", "Shorts"),
        "Outerwear" to listOf("Leather Biker Jacket", "Wool Overcoat", "Trench Coat", "Bomber Jacket", "Denim Jacket", "Tailored Blazer", "Puffer Jacket", "Cardigan"),
        "Shoes" to listOf("Chelsea Boots", "Combat / Work Boots", "Minimal White Sneakers", "Retro Runners", "Penny Loafers", "Dress Oxfords", "Mules / Slides"),
        "Eyewear" to listOf("Classic Wayfarer", "Aviator Sunglasses", "Round Frame", "Acetate Optical", "Square Metal Frame"),
        "Watches" to listOf("Minimalist Chronograph", "Steel Diver", "Leather Dress Watch", "Skeleton Automatic", "Luxury Sports Watch"),
        "Belts" to listOf("Matte Black Leather Belt", "Textured Suede Belt", "Silver Buckle Dress Belt", "Woven Tactical Belt"),
        "Bags" to listOf("Leather Weekender Duffle", "Structured Briefcase", "Crossbody Sling", "Minimal Canvas Tote", "Leather Backpack"),
        "Accessories" to listOf("Silver Ring", "Sterling Silver Cuff", "Cashmere Scarf", "Silk Pocket Square", "Wool Beanie", "Baseball Cap"),
        "Fragrance" to listOf("Woody / Oud Eau de Parfum", "Fresh Citrus / Aquatic", "Warm Amber & Vanilla", "Smoky Tobacco & Leather", "Spicy Oriental"),
        "Other" to listOf("Athletic Set", "Lounge Robe", "Swimwear", "Undergarment")
    )

    val PrimaryColors = listOf(
        "Black",
        "White",
        "Charcoal",
        "Grey",
        "Navy",
        "Olive Green",
        "Cream / Off-White",
        "Camel / Tan",
        "Espresso Brown",
        "Burgundy",
        "Khaki",
        "Slate Blue",
        "Silver",
        "Gold",
        "Other"
    )

    val Fits = listOf(
        "Slim",
        "Regular",
        "Relaxed",
        "Oversized",
        "Cropped",
        "Baggy",
        "Tailored"
    )

    val Styles = listOf(
        "Minimal",
        "Italian Classy",
        "Old Money",
        "Streetwear",
        "Starboy",
        "Smart Casual",
        "Casual",
        "Elegant",
        "Modern",
        "Classic",
        "Techwear",
        "Luxury"
    )

    val Formalities = listOf(
        "Casual",
        "Smart Casual",
        "Business Casual",
        "Formal",
        "Black Tie",
        "Lounge / Athletic"
    )

    val Seasons = listOf(
        "All Season",
        "Spring",
        "Summer",
        "Fall",
        "Winter",
        "Spring / Summer",
        "Fall / Winter"
    )

    val Occasions = listOf(
        "Daily",
        "Work / Office",
        "Date Night",
        "Dinner & Drinks",
        "Travel / Commute",
        "Gym & Sport",
        "Party & Nightout",
        "Special Event",
        "Vacation / Resort"
    )

    val StyleGoals = listOf(
        "Attractive",
        "Clean",
        "Premium",
        "Masculine",
        "Elegant",
        "Modern",
        "Minimal",
        "Youthful",
        "Confident",
        "Standout",
        "Approachable",
        "Timeless",
        "Sophisticated"
    )

    val FootwearPreferences = listOf(
        "Chelsea Boots",
        "Minimal White Sneakers",
        "Leather Penny Loafers",
        "Combat / Derby Boots",
        "Dress Oxfords / Derbies",
        "Retro High-Tops",
        "Suede Slip-Ons"
    )

    val AccessoryPreferences = listOf(
        "Silver Chronograph Watch",
        "Matte Black Leather Belt",
        "Acetate Sunglasses",
        "Minimal Silver Ring",
        "Leather Crossbody Bag",
        "Cashmere Scarf",
        "Signature Fragrance"
    )

    fun getColorPreview(colorName: String): Color {
        return when (colorName.lowercase()) {
            "black" -> Color(0xFF171717)
            "white" -> Color(0xFFF8FAFC)
            "charcoal" -> Color(0xFF334155)
            "grey", "gray" -> Color(0xFF64748B)
            "navy" -> Color(0xFF1E293B)
            "olive green", "olive" -> Color(0xFF4D5B3E)
            "cream / off-white", "cream", "off-white" -> Color(0xFFF5EFEB)
            "camel / tan", "camel", "tan" -> Color(0xFFC19A6B)
            "espresso brown", "brown", "espresso" -> Color(0xFF3E2723)
            "burgundy" -> Color(0xFF581845)
            "khaki" -> Color(0xFFC3B091)
            "slate blue" -> Color(0xFF4A6984)
            "silver" -> Color(0xFFCBD5E1)
            "gold" -> Color(0xFFC5A059)
            else -> Color(0xFF475569)
        }
    }
}
