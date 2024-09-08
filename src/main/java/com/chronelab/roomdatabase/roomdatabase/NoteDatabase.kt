package com.chronelab.roomdatabase.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chronelab.roomdatabase.model.Comment
import com.chronelab.roomdatabase.model.Post
import com.chronelab.roomdatabase.model.User
import com.chronelab.roomdatabase.roomdatabase.dao.CommentDao
import com.chronelab.roomdatabase.roomdatabase.dao.ItemDao
import com.chronelab.roomdatabase.roomdatabase.dao.PostDao
import com.chronelab.roomdatabase.roomdatabase.dao.UserDao
import com.chronelab.roomdatabase.roomdatabase.entity.Item

// Room database class defining the database configuration and entities
@Database(entities = [User::class, Post::class, Comment::class, Item::class], version = 2, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    // Abstract functions to get the DAOs for database access
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun itemDao(): ItemDao

    companion object {
        // Volatile annotation ensures that changes to Instance are visible to all threads immediately
        @Volatile
        private var Instance: NoteDatabase? = null

        // Singleton pattern to get the instance of the database
        fun getDatabase(context: Context): NoteDatabase {
            // Return the existing instance if available
            return Instance ?: synchronized(this) {
                // Create a new instance if not available
                Room.databaseBuilder(context, NoteDatabase::class.java, "note_database")
                    .fallbackToDestructiveMigration()  // Use this to handle migrations, but use with caution
                    .build()
                    .also { Instance = it }  // Set the new instance to the static variable
            }
        }
    }
}
