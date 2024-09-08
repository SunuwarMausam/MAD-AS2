package com.chronelab.roomdatabase.roomdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.chronelab.roomdatabase.model.User

// Data Access Object (DAO) for managing User entities in the database
@Dao
interface UserDao {

    // Inserts a new User into the database
    @Insert
    suspend fun insertUser(user: User)

    // Updates an existing User in the database
    @Update
    suspend fun updateUser(user: User)

    // Deletes a User from the database
    @Delete
    suspend fun deleteUser(user: User)

    // Retrieves a User from the database based on their email and password.
    // If a match is found, returns a User object; otherwise, returns null.
    @Query("SELECT * FROM users WHERE secondarySchoolEmail = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?

    // Retrieves a User from the database based on their full name.
    // If a match is found, returns a User object; otherwise, returns null.
    @Query("SELECT * FROM users WHERE fullName = :fullName LIMIT 1")
    suspend fun getUserByFullName(fullName: String): User?

    // Retrieves all Users from the database as a List of User objects
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User> // Added method
}
