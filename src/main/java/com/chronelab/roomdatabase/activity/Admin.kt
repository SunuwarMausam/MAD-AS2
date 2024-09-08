package com.chronelab.roomdatabase.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chronelab.roomdatabase.ui.theme.RoomAppTheme

/**
 * Admin activity responsible for admin login.
 */
class Admin : ComponentActivity() {

    // Hardcoded email and password for admin login
    private val AdminEmail = "admin123@gmail.com"
    private val AdminPassword = "123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view for this activity using Jetpack Compose
        setContent {
            RoomAppTheme {
                AdminLoginScreen()
            }
        }
    }

    /**
     * Composable function to display the admin login screen.
     */
    @Composable
    fun AdminLoginScreen() {
        // State variables to store the email and password entered by the user
        var adminEmail by remember { mutableStateOf("") }
        var adminPassword by remember { mutableStateOf("") }
        // Context for displaying Toast messages
        val context = LocalContext.current

        // Column layout for the login screen
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            // Spacer to add vertical space
            Spacer(modifier = Modifier.height(50.dp))

            // Title of the login screen
            Text(
                text = "Admin Login",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email input field
            OutlinedTextField(
                value = adminEmail,
                onValueChange = { adminEmail = it },
                label = { Text("Email", fontWeight = FontWeight.Bold, color = Color.Black) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password input field
            OutlinedTextField(
                value = adminPassword,
                onValueChange = { adminPassword = it },
                label = { Text("Password", fontWeight = FontWeight.Bold, color = Color.Black) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login button
            Button(
                onClick = {
                    // Check if the entered email and password match the admin credentials
                    if (adminEmail == AdminEmail && adminPassword == AdminPassword) {
                        Toast.makeText(context, "Admin Login Successful", Toast.LENGTH_SHORT).show()
                        // Start the AdminHome activity if login is successful
                        val intent = Intent(context, AdminHome::class.java)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADD8E6)) // Light Blue
            ) {
                Text(text = "Login", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }

    /**
     * Preview of the AdminLoginScreen composable.
     */
    @Preview(showBackground = true)
    @Composable
    fun AdminLogin() {
        RoomAppTheme {
            AdminLoginScreen()
        }
    }
}
