package com.fredprojects.aidlsdk.models

import android.os.Parcel
import android.os.Parcelable

data class UserInfo(
    val login: String,
    val id: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString() ?: "", parcel.readInt())
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeInt(id)
    }
    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<UserInfo> {
        override fun createFromParcel(parcel: Parcel): UserInfo {
            return UserInfo(parcel)
        }
        override fun newArray(size: Int): Array<UserInfo?> {
            return arrayOfNulls(size)
        }
    }
}