package com.fredprojects.domain.useCase

import com.fredprojects.domain.model.User
import com.fredprojects.domain.repository.IUserRepository
import com.fredprojects.domain.utils.UserDataStatus

class UserUseCase(
    private val userRepository: IUserRepository
) {
    suspend fun getUser(): User? = userRepository.getUser()
    suspend fun saveUser(user: User): UserDataStatus {
        // можно было бы создать отдельный класс для проверки данных пользователся, но пока сделаем так
        if(user.id < 0 || user.login.isBlank()) return UserDataStatus.INCORRECT_DATA
        val isSuccess = userRepository.saveUser(user)
        return if(isSuccess) {
            UserDataStatus.SUCCESS
        } else UserDataStatus.FAILURE
    }
}