package com.chronelab.roomdatabase.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// Data class representing a Comment entity in the Room database
@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Unique identifier for each comment, auto-generated
    val postId: Int, // Foreign key referencing the post to which this comment belongs
    val title: String, // Title of the comment
    val content: String, // Content of the comment
    val author: String, // Author of the comment
    val timestamp: Long // Timestamp indicating when the comment was created or posted
) : Serializable // Serializable interface allows this class to be serialized
