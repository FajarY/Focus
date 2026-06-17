package com.fajary.focus.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.fajary.focus.data.model.ToDoItem

@Dao
interface ToDoItemDao {
    @Query("INSERT INTO todo_items(title, description, deadline, completed, userId) VALUES (:title, :description, :deadline, 0, :userId)")
    suspend fun insert(title: String, description: String, deadline: String?, userId: Long): Long

    @Query("SELECT * FROM todo_items WHERE userId = :userId")
    suspend fun selectUserToDoListItems(userId: Long): List<ToDoItem>

    @Query(value = """
        UPDATE todo_items
        SET
            title = :title,
            description = :description,
            deadline = :deadline
        WHERE id = :id
    """)
    suspend fun updateToDoListItem(id: Long, title: String, description: String, deadline: String)

    @Query(value = """
        UPDATE todo_items
        SET
            completed = :completed
        WHERE id = :id
    """)
    suspend fun updateToDoListItemCompleted(id: Long, completed: Boolean)

    @Query(value = "SELECT * FROM todo_items WHERE id = :id")
    suspend fun selectOne(id: Long): ToDoItem?

    @Query("DELETE FROM todo_items WHERE id = :id")
    suspend fun deleteOne(id: Long)
}