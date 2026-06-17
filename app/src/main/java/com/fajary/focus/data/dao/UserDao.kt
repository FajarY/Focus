package com.fajary.focus.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.fajary.focus.data.model.User

@Dao
interface UserDao {
    @Query("INSERT INTO users(username, password, quotePreference) VALUES (:username, :password, :quotePreference)")
    suspend fun insert(username: String, password: String, quotePreference: String): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): User?

    @Query(value = "UPDATE users SET quotePreference = :quotePreference WHERE id = :id")
    suspend fun updateQuotePreference(id: Long, quotePreference: String)

    @Query(value = "SELECT * FROM users WHERE id = :id")
    suspend fun selectOne(id: Long): User?

    @Query(value = "DELETE FROM users WHERE id = :id")
    suspend fun deleteOne(id: Long);
}