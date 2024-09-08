package com.chronelab.roomdatabase.roomdatabase.repository

import com.chronelab.roomdatabase.roomdatabase.dao.ItemDao
import com.chronelab.roomdatabase.roomdatabase.entity.Item
import kotlinx.coroutines.flow.Flow

// Repository class for managing Item data
class ItemsRepository(private val itemDao: ItemDao) : ItemsRepositoryInterface {

    // Provides a stream of all items, observed as a Flow
    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    // Provides a stream of a specific item by its ID, observed as a Flow
    override fun getItemStream(id: Int): Flow<Item?> = itemDao.getItem(id)

    // Inserts a new item into the database
    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    // Deletes an existing item from the database
    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    // Updates an existing item in the database
    override suspend fun updateItem(item: Item) = itemDao.update(item)
}
