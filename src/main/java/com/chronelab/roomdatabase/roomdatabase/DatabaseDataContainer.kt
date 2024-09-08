package com.chronelab.roomdatabase.roomdatabase

import android.content.Context
import com.chronelab.roomdatabase.model.UserRepository
import com.chronelab.roomdatabase.roomdatabase.dao.CommentDao
import com.chronelab.roomdatabase.roomdatabase.dao.PostDao
import com.chronelab.roomdatabase.roomdatabase.repository.ItemsRepository
import com.chronelab.roomdatabase.roomdatabase.repository.ItemsRepositoryInterface

// Interface defining the essential data access objects and repositories for the application
interface DatabaseContainer {
    val itemsRepositoryInterface: ItemsRepositoryInterface
    val userRepository: UserRepository
    val postDao: PostDao
    val commentDao: CommentDao
}

// Implementation of DatabaseContainer that provides access to various data access objects and repositories
class DatabaseDataContainer(private val context: Context) : DatabaseContainer {

    // Lazy initialization of the ItemsRepositoryInterface using the itemDao from the database
    override val itemsRepositoryInterface: ItemsRepositoryInterface by lazy {
        ItemsRepository(NoteDatabase.getDatabase(context).itemDao())
    }

    // Lazy initialization of the UserRepository using the userDao from the database
    override val userRepository: UserRepository by lazy {
        UserRepository(NoteDatabase.getDatabase(context).userDao())
    }

    // Lazy initialization of the PostDao using the postDao from the database
    override val postDao: PostDao by lazy {
        NoteDatabase.getDatabase(context).postDao()
    }

    // Lazy initialization of the CommentDao using the commentDao from the database
    override val commentDao: CommentDao by lazy {
        NoteDatabase.getDatabase(context).commentDao()
    }
}
