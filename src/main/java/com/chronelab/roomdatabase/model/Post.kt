package com.chronelab.roomdatabase.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// Data class representing a Post entity in the Room database
@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Unique identifier for each post, auto-generated
    val title: String, // Title of the post
    val content: String, // Content of the post
    val author: String, // Author of the post
    val timestamp: Long, // Timestamp indicating when the post was created or posted
    var likes: Int = 0, // Number of likes for the post, default value is 0
    var dislikes: Int = 0 // Number of dislikes for the post, default value is 0
) : Serializable // Serializable interface allows this class to be serialized
