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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chronelab.roomdatabase.RoomApp
import com.chronelab.roomdatabase.model.User
import com.chronelab.roomdatabase.ui.theme.RoomAppTheme
import kotlinx.coroutines.launch

// RegistrationActivity is responsible for handling user registration
class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content of the activity to RegisterScreen composable
        setContent {
            RoomAppTheme {
                RegisterScreen()
            }
        }
    }

    // Composable function for the registration screen
    @Composable
    fun RegisterScreen() {
        val coroutineScope = rememberCoroutineScope() // Coroutine scope for asynchronous operations
        // State variables to hold user input
        var fullName by remember { mutableStateOf("") }
        var dateOfBirth by remember { mutableStateOf("") }
        var secondarySchoolEmail by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Title at the top of the registration screen
            Text(
                text = "Register",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TextField for user to input full name
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name", fontWeight = FontWeight.Bold, color = Color.Black) },
                textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )
            // TextField for user to input date of birth
            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = { Text("Date of Birth", fontWeight = FontWeight.Bold, color = Color.Black) },
                textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            // TextField for user to input secondary school email
            OutlinedTextField(
                value = secondarySchoolEmail,
                onValueChange = { secondarySchoolEmail = it },
                label = { Text("Email Address", fontWeight = FontWeight.Bold, color = Color.Black) },
                textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            // TextField for user to input password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", fontWeight = FontWeight.Bold, color = Color.Black) },
                textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            // Button to submit the registration form
            Button(
                onClick = {
                    if (fullName.isNotBlank() && dateOfBirth.isNotBlank() &&
                        secondarySchoolEmail.isNotBlank() && password.isNotBlank()) {

                        // Create a new User object with the provided details
                        val user = User(
                            id = 0,
                            fullName = fullName,
                            dateOfBirth = dateOfBirth,
                            secondarySchoolEmail = secondarySchoolEmail,
                            password = password,
                            dateCreated = System.currentTimeMillis(),
                            dateUpdated = System.currentTimeMillis()
                        )
                        // Launch a coroutine to perform the registration
                        coroutineScope.launch {
                            val app = application as RoomApp
                            app.databaseContainer.userRepository.insertUser(user)

                            // Display a success message
                            Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_SHORT).show()

                            // Navigate to the login screen
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish() // Finish the current activity
                        }
                    } else {
                        // Show an error message if any field is blank
                        Toast.makeText(applicationContext, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADD8E6)), // Light blue button color
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Register", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }

    // Preview function to visualize the registration screen design
    @Preview(showBackground = true)
    @Composable
    fun RegisterScreenPreview() {
        RoomAppTheme {
            RegisterScreen()
        }
    }
}
