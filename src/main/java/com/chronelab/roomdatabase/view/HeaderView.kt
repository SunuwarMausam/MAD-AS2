package com.chronelab.roomdatabase.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chronelab.roomdatabase.ui.theme.RoomAppTheme

@Composable
fun HeaderView(
    title: String,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF28B9A6)) // Custom background color
            .padding(16.dp) // Padding around the entire header
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Vertically center items in the row
        ) {
            // Profile button
            IconButton(onClick = {
                onProfileClick() // Trigger profile click action
            }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    tint = Color.White // Icon color
                )
            }

            // Spacer to push title to the center
            Spacer(modifier = Modifier.weight(1f))

            // Title text
            Text(
                text = title,
                color = Color.White, // Text color
                fontSize = 20.sp, // Font size
                fontWeight = FontWeight.Bold, // Font weight
                modifier = Modifier.padding(top = 8.dp) // Padding at the top of the title
            )

            // Spacer to push logout button to the end
            Spacer(modifier = Modifier.weight(1f))

            // Logout button
            IconButton(onClick = {
                onLogoutClick() // Trigger logout click action
            }) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.White // Icon color
                )
            }
        }
    }
}

// Preview function to visualize HeaderView
@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    RoomAppTheme {
        HeaderView(
            title = "Home",
            onProfileClick = {},
            onLogoutClick = {}
        )
    }
}
