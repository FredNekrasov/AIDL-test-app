package com.fredprojects.presentation.vm

import com.fredprojects.domain.model.User
import com.fredprojects.domain.utils.UserDataStatus

/**
 * Used for passing data from VM to UI.
 * @param isLoading used for displaying progress bar in UI or hiding it
 * @param user user data that we get from another app
 * @param userDataStatus used for displaying status of action in UI
 */
data class UserState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val userDataStatus: UserDataStatus? = null
)