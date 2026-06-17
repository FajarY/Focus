package com.fajary.focus.data.repository

import com.fajary.focus.data.dao.ToDoItemDao
import com.fajary.focus.data.model.ToDoItem

class ToDoItemRepository {
    private val dao: ToDoItemDao;

    constructor(dao: ToDoItemDao)
    {
        this.dao = dao
    }

    suspend fun create(title: String, description: String, deadline: String?, userId: Long): ToDoItem?
    {
        val insert_id = dao.insert(title, description, deadline, userId)
        return dao.selectOne(insert_id)
    }

    suspend fun selectUserToDoItems(userId: Long): List<ToDoItem>
    {
        return dao.selectUserToDoListItems(userId)
    }

    suspend fun selectOne(id: Long): ToDoItem?
    {
        return dao.selectOne(id)
    }

    suspend fun update(id: Long, title: String, description: String, deadline: String): ToDoItem?
    {
        dao.updateToDoListItem(id, title, description, deadline)
        return dao.selectOne(id)
    }

    suspend fun updateCompleted(id: Long, completed: Boolean): ToDoItem?
    {
        dao.updateToDoListItemCompleted(id, completed)
        return dao.selectOne(id)
    }

    suspend fun delete(id: Long)
    {
        dao.deleteOne(id)
    }
}