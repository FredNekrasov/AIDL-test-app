package com.fredprojects.presentation.core

import com.fredprojects.domain.utils.UserDataStatus
import com.fredprojects.presentation.R

fun UserDataStatus.getMessageId(): Int = when (this) {
    UserDataStatus.SUCCESS -> R.string.success
    UserDataStatus.INCORRECT_DATA -> R.string.invalid
    UserDataStatus.FAILURE -> R.string.failed
}