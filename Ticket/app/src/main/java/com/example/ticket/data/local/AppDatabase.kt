package com.example.ticket.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ticket.data.local.dao.BookmarkDao
import com.example.ticket.data.local.entity.BookmarkEntity

@Database(entities = [BookmarkEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}