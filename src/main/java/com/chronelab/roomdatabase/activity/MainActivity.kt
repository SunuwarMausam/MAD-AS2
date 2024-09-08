package com.chronelab.roomdatabase.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.chronelab.roomdatabase.RoomApp
import com.chronelab.roomdatabase.model.Comment
import com.chronelab.roomdatabase.model.Post
import com.chronelab.roomdatabase.model.User
import com.chronelab.roomdatabase.ui.theme.RoomAppTheme
import com.chronelab.roomdatabase.view.HeaderView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

// MainActivity is the entry point for the application and displays the main screen after login
class MainActivity : ComponentActivity() {
    private lateinit var fullName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the user's full name from the intent extras
        fullName = intent.getStringExtra("key_user_full_name") ?: "Unknown User"

        setContent {
            RoomAppTheme {
                Home(fullName)
            }
        }
    }

    // Composable function for the home screen
    @Composable
    fun Home(fullName: String) {
        var postContent by remember { mutableStateOf("") }
        val context = LocalContext.current

        // Fetch posts from the database
        val postDao = (context.applicationContext as RoomApp).databaseContainer.postDao
        val posts by postDao.getAllPosts().collectAsState(initial = emptyList()) // Initialize with an empty list

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // HeaderView to handle profile and logout actions
            HeaderView(
                title = "Home",
                onProfileClick = {
                    // Handle profile click, e.g., show a dialog
                    Toast.makeText(context, "Profile Clicked", Toast.LENGTH_SHORT).show()
                },
                onLogoutClick = {
                    // Handle logout action
                    logout(context)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // TextField for user to create a post
            TextField(
                value = postContent,
                onValueChange = { postContent = it },
                label = { Text("What's on your mind? Create your post.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
            )

            if (postContent.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                // Button to create a post
                Button(
                    onClick = {
                        val post = Post(
                            title = "",
                            content = postContent,
                            author = fullName,
                            timestamp = System.currentTimeMillis()
                        )
                        lifecycleScope.launch {
                            val app = context.applicationContext as RoomApp
                            app.databaseContainer.postDao.insert(post)
                            Toast.makeText(context, "Post created", Toast.LENGTH_SHORT).show()
                            postContent = "" // Clear the input field
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create Post")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Display all posts
            posts.forEach { post ->
                PostItem(post = post, fullName)
                Spacer(modifier = Modifier.height(12.dp)) // Add space between posts
            }
        }
    }

    // Function to handle user logout
    private fun logout(context: android.content.Context) {
        // Clear user session data
        val sharedPreferences = context.getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // Redirect to LoginActivity
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    // Composable function to display a list of posts
    @Composable
    fun Post(fullName: String) {
        val context = LocalContext.current
        val postDao = (context.applicationContext as RoomApp).databaseContainer.postDao
        val posts by postDao.getAllPosts().collectAsState(initial = emptyList())

        Column {
            posts.forEach { post ->
                PostItem(post, fullName)
            }
        }
    }

    // Composable function to display an individual post and its comments
    @Composable
    fun PostItem(post: Post, fullName: String) {
        var commentText by remember { mutableStateOf(TextFieldValue("")) }
        var showEdit by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val postDao = (context.applicationContext as RoomApp).databaseContainer.postDao
        val commentDao = (context.applicationContext as RoomApp).databaseContainer.commentDao
        val comments by commentDao.getCommentsForPost(post.id).collectAsState(initial = emptyList())

        if (showEdit) {
            EditPost(post = post, onDismiss = { showEdit = false })
        }
        Column(modifier = Modifier.padding(4.dp)) {
            Text(
                text = "Posted by ${post.author}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 5.dp)
            )
            Text(
                text = "On ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(post.timestamp)}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 5.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Button to like a post
                    OutlinedButton(
                        onClick = {
                            lifecycleScope.launch {
                                postDao.likePost(post.id)
                            }
                        },
                        modifier = Modifier.size(width = 93.dp, height = 36.dp)
                    ) {
                        Text("Like (${post.likes})", fontSize = 10.sp)
                    }
                    // Button to dislike a post
                    OutlinedButton(
                        onClick = {
                            lifecycleScope.launch {
                                postDao.dislikePost(post.id)
                            }
                        },
                        modifier = Modifier.size(width = 93.dp, height = 36.dp)
                    ) {
                        Text("Dislike (${post.dislikes})", fontSize = 10.sp)
                    }
                }

                // Show edit and delete buttons only if the user is the author of the post
                if (post.author == fullName) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Button to edit the post
                        OutlinedButton(
                            onClick = { showEdit = true },
                            modifier = Modifier.size(width = 80.dp, height = 36.dp)
                        ) {
                            Text("Edit", fontSize = 10.sp)
                        }
                        // Button to delete the post
                        OutlinedButton(
                            onClick = {
                                lifecycleScope.launch {
                                    postDao.delete(post)
                                    Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.size(width = 80.dp, height = 36.dp)
                        ) {
                            Text("Delete", fontSize = 10.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Display comments for the post
            comments.forEach { comment ->
                Column(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(
                        text = "Commented by ${comment.author}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Gray
                    )
                    Text(
                        text = comment.content,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = "On ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(comment.timestamp)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            // TextField for adding a comment
            TextField(
                value = commentText,
                onValueChange = { commentText = it },
                label = { Text("Add a comment") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Button to add a comment
            Button(
                onClick = {
                    val comment = Comment(
                        title = "",
                        postId = post.id,
                        content = commentText.text,
                        author = fullName,
                        timestamp = System.currentTimeMillis()
                    )
                    lifecycleScope.launch {
                        commentDao.insert(comment)
                        Toast.makeText(context, "Comment added", Toast.LENGTH_SHORT).show()
                        commentText = TextFieldValue("") // Clear the input field
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Comment")
            }
        }
    }

    // Composable function to edit a post
    @Composable
    fun EditPost(post: Post, onDismiss: () -> Unit) {
        var postContent by remember { mutableStateOf(TextFieldValue(post.content)) }
        val context = LocalContext.current
        val postDao = (context.applicationContext as RoomApp).databaseContainer.postDao

        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = {
                        lifecycleScope.launch {
                            val updatedPost = post.copy(
                                content = postContent.text
                            )
                            postDao.update(updatedPost)
                            Toast.makeText(context, "Post updated", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            },
            title = { Text(text = "Edit Post") },
            text = {
                Column {
                    TextField(
                        value = postContent,
                        onValueChange = { postContent = it },
                        label = { Text("What's on your mind?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }

    // Composable function to display user initials as a logo
    @Composable
    fun Logo(fullName: String) {
        // Extract initials from the full name
        val initials = fullName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
        val context = LocalContext.current
        var showProfileDialog by remember { mutableStateOf(false) }
        var user by remember { mutableStateOf<User?>(null) }
        val userDao = (context.applicationContext as RoomApp).databaseContainer.userRepository

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    lifecycleScope.launch {
                        user = userDao.getUserByFullName(fullName)
                        showProfileDialog = true
                    }
                }
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }

        if (showProfileDialog && user != null) {
            UserProfile(user = user!!, onDismiss = { showProfileDialog = false })
        }
    }

    // Composable function to display user profile details
    @Composable
    fun UserProfile(user: User, onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("Close")
                }
            },
            title = { Text(text = "Profile Details") },
            text = {
                Column {
                    Text(text = "Full Name: ${user.fullName}", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Date of Birth: ${user.dateOfBirth}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Email: ${user.secondarySchoolEmail}")
                }
            }
        )
    }

    // Preview function to show a preview of the Home screen
    @Preview(showBackground = true)
    @Composable
    fun HomeScreenPreview() {
        RoomAppTheme {
            Home(fullName = "Preview User")
        }
    }
}
