package com.chronelab.roomdatabase.roomdatabase.dao

import androidx.room.*
import com.chronelab.roomdatabase.model.Post
import kotlinx.coroutines.flow.Flow

// Data Access Object (DAO) for managing Post entities in the database
@Dao
interface PostDao {

    // Inserts a new Post into the database. If a conflict occurs (e.g., a duplicate entry), the existing entry will be replaced.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    // Updates an existing Post in the database
    @Update
    suspend fun update(post: Post)

    // Deletes a Post from the database
    @Delete
    suspend fun delete(post: Post)

    // Retrieves all Posts from the database, ordered by their timestamp in descending order. Returns a Flow to observe changes.
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<Post>>

    // Increments the likes count for a specific Post by its ID
    @Query("UPDATE posts SET likes = likes + 1 WHERE id = :postId")
    suspend fun likePost(postId: Int)

    // Increments the dislikes count for a specific Post by its ID
    @Query("UPDATE posts SET dislikes = dislikes + 1 WHERE id = :postId")
    suspend fun dislikePost(postId: Int)
}
