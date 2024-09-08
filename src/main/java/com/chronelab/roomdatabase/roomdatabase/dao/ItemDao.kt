package com.chronelab.roomdatabase.roomdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chronelab.roomdatabase.roomdatabase.entity.Item
import kotlinx.coroutines.flow.Flow

// Data Access Object (DAO) for managing Item entities in the database
@Dao
interface ItemDao {

    // Inserts a new Item into the database. If a conflict occurs (e.g., a duplicate entry), the insert operation will be ignored.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    // Updates an existing Item in the database
    @Update
    suspend fun update(item: Item)

    // Deletes an Item from the database
    @Delete
    suspend fun delete(item: Item)

    // Retrieves an Item by its ID. Returns a Flow to observe changes to the Item in real-time.
    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    // Retrieves all Items from the database, ordered by their name in ascending order. Returns a Flow to observe changes to the list of Items.
    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>
}
