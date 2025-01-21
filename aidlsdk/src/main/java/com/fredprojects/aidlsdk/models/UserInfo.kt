package com.fredprojects.aidlsdk.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val login: String,
    val id: Int
) : Parcelable