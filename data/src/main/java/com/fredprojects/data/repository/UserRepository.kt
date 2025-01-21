package com.fredprojects.data.repository

import com.fredprojects.aidlsdk.DefUserAidlImpl
import com.fredprojects.data.mapper.toUserInfo
import com.fredprojects.data.mapper.toUserModel
import com.fredprojects.domain.model.User
import com.fredprojects.domain.repository.IUserRepository

class UserRepository(
    private val userAidlImpl: DefUserAidlImpl
) : IUserRepository {
    override suspend fun getUser(): User? = userAidlImpl.getUserInfoOrNull()?.toUserModel()
    override suspend fun saveUser(user: User): Boolean = userAidlImpl.setUserInfo(user.toUserInfo())
}