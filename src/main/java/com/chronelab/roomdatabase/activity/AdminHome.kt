package com.chronelab.roomdatabase.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chronelab.roomdatabase.RoomApp
import com.chronelab.roomdatabase.model.User
import com.chronelab.roomdatabase.ui.theme.RoomAppTheme
import kotlinx.coroutines.launch

/**
 * AdminHome activity that allows the admin to manage users.
 */
class AdminHome : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view using Jetpack Compose
        setContent {
            RoomAppTheme {
                AdminHomeScreen()
            }
        }
    }

    /**
     * Composable function for the admin home screen.
     */
    @Composable
    fun AdminHomeScreen() {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val userRepository = (context.applicationContext as RoomApp).databaseContainer.userRepository

        // State variables for managing user data and UI visibility
        var users by remember { mutableStateOf<List<User>>(emptyList()) }
        var selectedUser by remember { mutableStateOf<User?>(null) }
        var showUpdateForm by remember { mutableStateOf(false) }
        var showAddUserForm by remember { mutableStateOf(false) }
        var fullName by remember { mutableStateOf("") }
        var dob by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var newUserFullName by remember { mutableStateOf("") }
        var newUserDob by remember { mutableStateOf("") }
        var newUserEmail by remember { mutableStateOf("") }
        var newUserPassword by remember { mutableStateOf("") }

        // Fetch users from the database on the first composition
        LaunchedEffect(Unit) {
            users = userRepository.getAllUsers()
        }

        // Column layout for the admin home screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Row with buttons for adding users and logging out
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Add button to show the form for adding a new user
                Button(
                    onClick = {
                        showAddUserForm = true
                    }
                ) {
                    Text(text = "Add")
                }

                // Logout button to navigate back to the login screen
                Button(
                    onClick = {
                        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, LoginActivity::class.java))
                    }
                ) {
                    Text(text = "Logout")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title for the user management section
            Text(
                text = "Admin - Manage Users",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display list of users if available
            if (users.isNotEmpty()) {
                LazyColumn {
                    items(users) { user ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Display user email
                            Text(
                                text = user.secondarySchoolEmail,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )

                            // Row with buttons for updating or deleting a user
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Update button to show the form for updating user details
                                Button(
                                    onClick = {
                                        selectedUser = user
                                        fullName = user.fullName
                                        dob = user.dateOfBirth
                                        email = user.secondarySchoolEmail
                                        showUpdateForm = true
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "Update")
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // Delete button to remove the user from the database
                                Button(
                                    onClick = {
                                        scope.launch {
                                            userRepository.deleteUser(user)
                                            Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show()
                                            users = userRepository.getAllUsers() // Refresh user list
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "Delete")
                                }
                            }
                        }
                    }
                }
            } else {
                // Display message if no users are found
                Text(
                    text = "No users found",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Show update user form if triggered
            if (showUpdateForm) {
                UpdateUserForm(
                    fullName = fullName,
                    dob = dob,
                    email = email,
                    newPassword = newPassword,
                    onFullNameChange = { fullName = it },
                    onDobChange = { dob = it },
                    onEmailChange = { email = it },
                    onNewPasswordChange = { newPassword = it },
                    onUpdateClick = {
                        selectedUser?.let {
                            scope.launch {
                                userRepository.updateUser(
                                    it.copy(fullName = fullName, dateOfBirth = dob, secondarySchoolEmail = email, password = newPassword)
                                )
                                Toast.makeText(context, "User updated successfully", Toast.LENGTH_SHORT).show()
                                users = userRepository.getAllUsers() // Refresh user list
                                showUpdateForm = false
                            }
                        }
                    },
                    onCancelClick = { showUpdateForm = false }
                )
            }

            // Show add user form if triggered
            if (showAddUserForm) {
                AddUserForm(
                    fullName = newUserFullName,
                    dob = newUserDob,
                    email = newUserEmail,
                    password = newUserPassword,
                    onFullNameChange = { newUserFullName = it },
                    onDobChange = { newUserDob = it },
                    onEmailChange = { newUserEmail = it },
                    onPasswordChange = { newUserPassword = it },
                    onAddClick = {
                        scope.launch {
                            userRepository.insertUser(
                                User(fullName = newUserFullName, dateOfBirth = newUserDob, secondarySchoolEmail = newUserEmail, password = newUserPassword)
                            )
                            Toast.makeText(context, "User added successfully", Toast.LENGTH_SHORT).show()
                            users = userRepository.getAllUsers() // Refresh user list
                            showAddUserForm = false
                        }
                    },
                    onCancelClick = { showAddUserForm = false }
                )
            }
        }
    }

    /**
     * Composable function to display the form for adding a new user.
     */
    @Composable
    fun AddUserForm(
        fullName: String,
        dob: String,
        email: String,
        password: String,
        onFullNameChange: (String) -> Unit,
        onDobChange: (String) -> Unit,
        onEmailChange: (String) -> Unit,
        onPasswordChange: (String) -> Unit,
        onAddClick: () -> Unit,
        onCancelClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title for the add user form
            Text(
                text = "Add New User",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input field for full name
            OutlinedTextField(
                value = fullName,
                onValueChange = onFullNameChange,
                label = { Text("Full Name", fontWeight = FontWeight.Bold, color = Color.Black) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input field for date of birth
            OutlinedTextField(
                value = dob,
                onValueChange = onDobChange,
                label = { Text("Date of Birth", fontWeight = FontWeight.Bold, color = Color.Black) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input field for email
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email", fontWeight = FontWeight.Bold, color = Color.Black) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input field for password
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password", fontWeight = FontWeight.Bold, color = Color.Black) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onAddClick() }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add button to submit the form
            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cancel button to close the form
            Button(
                onClick = onCancelClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cancel")
            }
        }
    }

    /**
     * Composable function to display the form for updating user details.
     */
    @Composable
    fun UpdateUserForm(
        fullName: String,
        dob: String,
        email: String,
        newPassword: String,
        onFullNameChange: (String) -> Unit,
        onDobChange: (String) -> Unit,
        onEmailChange: (String) -> Unit,
        onNewPasswordChange: (String) -> Unit,
        onUpdateClick: () -> Unit,
        onCancelClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title for the update user form
            Text(
                text = "Update User Details",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input field for full name
            OutlinedTextField(
                value = fullName,
                onValueChange = onFullNameChange,
                label = { Text("Full Name", fontWeight = FontWeight.Bold, color = Color.Black) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input field for date of birth
            OutlinedTextField(
                value = dob,
                onValueChange = onDobChange,
                label = { Text("Date of Birth", fontWeight = FontWeight.Bold, color = Color.Black) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input field for email
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email", fontWeight = FontWeight.Bold, color = Color.Black) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input field for new password
            OutlinedTextField(
                value = newPassword,
                onValueChange = onNewPasswordChange,
                label = { Text("New Password", fontWeight = FontWeight.Bold, color = Color.Black) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onUpdateClick() }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Update button to submit the form
            Button(
                onClick = onUpdateClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Update")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cancel button to close the form
            Button(
                onClick = onCancelClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cancel")
            }
        }
    }
}
