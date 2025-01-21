package com.fredprojects.data.di

import android.content.Context
import com.fredprojects.aidlsdk.DefUserAidlImpl
import com.fredprojects.data.repository.UserRepository
import com.fredprojects.domain.repository.IUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideUserAIDL(@ApplicationContext context: Context) = DefUserAidlImpl(context)
    @Singleton
    @Provides
    fun provideUserRepository(userAIDL: DefUserAidlImpl): IUserRepository = UserRepository(userAIDL)
}