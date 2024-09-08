package com.chronelab.roomdatabase

import android.app.Application
import com.chronelab.roomdatabase.roomdatabase.DatabaseDataContainer

class RoomApp : Application() {
    lateinit var databaseContainer: DatabaseDataContainer

    override fun onCreate() {
        super.onCreate()
        // Initialize the DatabaseDataContainer with the application context
        databaseContainer = DatabaseDataContainer(this)
    }
}
