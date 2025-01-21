package com.fredprojects.data.mapper

import com.fredprojects.aidlsdk.models.UserInfo
import com.fredprojects.domain.model.User

fun UserInfo.toUserModel() = User(
    id = id,
    login = login
)
fun User.toUserInfo() = UserInfo(
    id = id,
    login = login
)