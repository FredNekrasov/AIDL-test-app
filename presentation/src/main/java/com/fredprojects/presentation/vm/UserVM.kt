package com.fredprojects.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fredprojects.domain.model.User
import com.fredprojects.domain.useCase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserVM @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val userStateMSF = MutableStateFlow(UserState())
    val userStateSF = userStateMSF.asStateFlow()
    fun getUser() {
        userStateMSF.value = userStateSF.value.copy(isLoading = true)
        viewModelScope.launch {
            val newState = userStateSF.value.copy(isLoading = false, user = userUseCase.getUser())
            userStateMSF.emit(newState)
        }
    }
    fun setUser(user: User) {
        userStateMSF.value = userStateSF.value.copy(isLoading = true)
        viewModelScope.launch {
            val newState = userStateSF.value.copy(isLoading = false, userDataStatus = userUseCase.saveUser(user))
            userStateMSF.emit(newState)
        }
    }
}