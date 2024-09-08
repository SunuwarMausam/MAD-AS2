package com.chronelab.roomdatabase.roomdatabase.dao

import androidx.room.*
import com.chronelab.roomdatabase.model.Comment
import kotlinx.coroutines.flow.Flow

// Data Access Object (DAO) for managing Comment entities in the database
@Dao
interface CommentDao {

    // Inserts a new Comment into the database. If a Comment with the same ID already exists, it will be replaced.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: Comment)

    // Updates an existing Comment in the database
    @Update
    suspend fun update(comment: Comment)

    // Deletes a Comment from the database
    @Delete
    suspend fun delete(comment: Comment)

    // Retrieves a list of Comments associated with a specific postId, ordered by timestamp in descending order.
    // Returns a Flow to observe changes to the list of Comments in real-time.
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp DESC")
    fun getCommentsForPost(postId: Int): Flow<List<Comment>>
}
