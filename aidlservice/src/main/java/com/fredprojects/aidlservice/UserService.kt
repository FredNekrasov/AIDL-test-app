package com.fredprojects.aidlservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.fredprojects.aidlsdk.UserAIDL
import com.fredprojects.aidlsdk.models.UserInfo
import kotlinx.coroutines.*

class UserService : Service() {
    private val scope by lazy { CoroutineScope(Dispatchers.IO) }
    private var userData: UserInfo? = null
    override fun onBind(intent: Intent): IBinder {
        return object : UserAIDL.Stub() {
            override fun getUserInfo(): UserInfo? {
                scope.launch {
                    delay(3000)
                }
                return userData
            }
            override fun setUserInfo(userInfo: UserInfo?) {
                scope.launch {
                    delay(3000)
                    userData = userInfo
                }
            }
        }
    }
}