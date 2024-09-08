package com.chronelab.roomdatabase.model

import com.chronelab.roomdatabase.roomdatabase.dao.UserDao

// Repository class that abstracts the data access layer for User entities
class UserRepository(private val userDao: UserDao) {

    // Inserts a new user into the database
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    // Updates an existing user's details in the database
    suspend fun updateUser(user: User) = userDao.updateUser(user)

    // Deletes a user from the database
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    // Retrieves a user from the database based on their email and password
    suspend fun getUserByEmailAndPassword(email: String, password: String): User? =
        userDao.getUserByEmailAndPassword(email, password)

    // Retrieves a user from the database based on their full name
    suspend fun getUserByFullName(fullName: String): User? =
        userDao.getUserByFullName(fullName)

    // Retrieves a list of all users from the database
    suspend fun getAllUsers(): List<User> = userDao.getAllUsers() // Added method for retrieving all users
}
