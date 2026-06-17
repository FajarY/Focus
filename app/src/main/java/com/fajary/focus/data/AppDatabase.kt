package com.fajary.focus.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fajary.focus.data.dao.ToDoItemDao
import com.fajary.focus.data.dao.UserDao
import com.fajary.focus.data.model.ToDoItem
import com.fajary.focus.data.model.User

@Database(
    entities = [User::class, ToDoItem::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun userDao() : UserDao
    abstract fun toDoItemDao() : ToDoItemDao
}