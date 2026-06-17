package com.fajary.focus.data.repository

import com.fajary.focus.data.dao.UserDao
import com.fajary.focus.data.model.User

class UserRepository {
    private val dao: UserDao;

    constructor(dao: UserDao)
    {
        this.dao = dao
    }

    suspend fun create(username: String, password: String): User?
    {
        val insert_id = dao.insert(username, password, "today")
        return dao.selectOne(insert_id)
    }

    suspend fun login(username: String, password: String): User?
    {
        return dao.login(username, password)
    }

    suspend fun updateQuotePreference(id: Long, quotePreference: String)
    {
        return dao.updateQuotePreference(id, quotePreference)
    }

    suspend fun selectOne(id: Long): User?
    {
        return dao.selectOne(id)
    }

    suspend fun delete(id: Long)
    {
        return dao.deleteOne(id)
    }
}