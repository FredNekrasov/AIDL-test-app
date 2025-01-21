package com.fredprojects.domain.utils

/**
 * UserDataStatus is an enum class that represents the status of user data.
 * @property SUCCESS is used when the operation is successfully completed.
 * @property INCORRECT_DATA is used when the user data does not pass validation.
 * @property FAILURE is used when the operation completes with an error.
 */
enum class UserDataStatus {
    SUCCESS,
    INCORRECT_DATA,
    FAILURE
}