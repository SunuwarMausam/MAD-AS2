package com.chronelab.roomdatabase.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Data class representing a User entity in the Room database
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Unique identifier for each user, auto-generated
    var fullName: String, // Full name of the user
    var dateOfBirth: String, // Date of birth of the user, stored as a string
    var secondarySchoolEmail: String, // Email address of the user
    var password: String, // Password of the user
    val dateCreated: Long = System.currentTimeMillis(), // Timestamp indicating when the user was created, default to the current time
    var dateUpdated: Long = System.currentTimeMillis() // Timestamp indicating when the user details were last updated, default to the current time
)
