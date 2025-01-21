package com.fredprojects.aidlservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.fredprojects.aidlsdk.UserAIDLWithCallback
import com.fredprojects.aidlsdk.models.UserInfo
import com.fredprojects.aidlsdk.utils.ResultCallback
import kotlinx.coroutines.*

class UserServiceWithCallbacks : Service() {
    private val scope by lazy { CoroutineScope(Dispatchers.Default) }
    private var userData: UserInfo? = null
    override fun onBind(intent: Intent): IBinder {
        return object : UserAIDLWithCallback.Stub() {
            override fun getUserInfo(callback: ResultCallback?) {
                if(callback == null) return println("fred: callback is null")
                try {
                    if(userData == null) throw Exception("userData is null")
                    scope.launch {
                        delay(3000)
                        Log.e("fred", userData.toString())
                        callback.onSuccess(userData)
                    }
                } catch (e: Exception) {
                    Log.e("fred", "UserServiceWithCallbacks: ${e.message}")
                    callback.onError(e.message)
                }
            }
            override fun setUserInfo(userInfo: UserInfo?, callback: ResultCallback?) {
                if(callback == null) return println("fred: callback is null")
                try {
                    if(userInfo?.login == "null") throw Exception("login is null")
                    scope.launch {
                        delay(3000)
                        userData = userInfo
                        callback.onSuccess(userData)
                    }
                } catch(e: Exception) {
                    Log.e("fred", "UserServiceWithCallbacks: ${e.message}")
                    callback.onError(e.message)
                }
            }
        }
    }
}