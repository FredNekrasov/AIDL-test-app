package com.fredprojects.domain.repository

import com.fredprojects.domain.model.User

interface IUserRepository {
    suspend fun getUser(): User?
    suspend fun saveUser(user: User): Boolean
}