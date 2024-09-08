package com.chronelab.roomdatabase.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Define a set of Material typography styles
val Typography = Typography(
    // Define the style for large body text
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // Use default font family
        fontWeight = FontWeight.Normal,   // Use normal font weight
        fontSize = 18.sp,                 // Set font size to 18 sp
        lineHeight = 24.sp,               // Set line height to 24 sp
        letterSpacing = 0.5.sp            // Set letter spacing to 0.5 sp
    )
)
