package com.chronelab.roomdatabase.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chronelab.roomdatabase.R
import com.chronelab.roomdatabase.RoomApp
import com.chronelab.roomdatabase.ui.theme.RoomAppTheme
import kotlinx.coroutines.launch

/**
 * LoginActivity is the entry point for user authentication in the application.
 */
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view using Jetpack Compose
        setContent {
            RoomAppTheme {
                Login()
            }
        }
    }
}

/**
 * Composable function that displays the login screen.
 */
@Composable
fun Login() {
    // State and coroutine scope for managing user interactions and asynchronous operations
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val errorMessage by remember { mutableStateOf("") }

    // Column layout for the login screen
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Spacer(modifier = Modifier.height(50.dp)) // Add space at the top

        // Title text for the login screen
        Text(
            text = stringResource(id = R.string.txt_login),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input field for email address
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address", fontWeight = FontWeight.Bold, color = Color.Black) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input field for password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
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

        // Display error message if it's not empty
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Login button
        Button(
            onClick = {
                coroutineScope.launch {
                    // Access the user repository from the application context
                    val app = context.applicationContext as RoomApp
                    val userRepository = app.databaseContainer.userRepository
                    // Check user credentials
                    val user = userRepository.getUserByEmailAndPassword(email, password)
                    if (user != null) {
                        Log.i("LoginActivity", "Moving to Home Activity")
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()
                        // Start MainActivity and pass user full name as an extra
                        val intent = Intent(context, MainActivity::class.java).apply {
                            putExtra("key_user_full_name", user.fullName)
                        }
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Invalid email or password", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), // Set button height to 50dp
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADD8E6)) // Light blue button
        ) {
            Text(text = stringResource(id = R.string.txt_login), fontWeight = FontWeight.Bold, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Register link
        Text(
            text = "Don't have an account? Register",
            style = MaterialTheme.typography.bodyMedium.copy(
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier.clickable {
                // Navigate to RegistrationActivity when clicked
                val intent = Intent(context, RegistrationActivity::class.java)
                context.startActivity(intent)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Admin login button
        Button(
            onClick = {
                // Navigate to Admin activity when clicked
                val intent = Intent(context, Admin::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), // Set button height to 50dp
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADD8E6)) // Light blue button
        ) {
            Text(text = "Admin Login", fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

/**
 * Preview function for the Login composable.
 */
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    RoomAppTheme {
        Login()
    }
}
