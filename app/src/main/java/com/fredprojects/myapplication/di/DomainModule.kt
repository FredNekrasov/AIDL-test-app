package com.fredprojects.myapplication.di

import com.fredprojects.domain.repository.IUserRepository
import com.fredprojects.domain.useCase.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    fun provideUserUseCase(userRepository: IUserRepository) = UserUseCase(userRepository)
}