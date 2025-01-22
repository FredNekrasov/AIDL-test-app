package com.fredprojects.aidlservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
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
                    scope.launch {
                        delay(3000)
                        callback.onSuccess(userData)
                    }
                } catch (e: RemoteException) {
                    callback.onError(e.message)
                }
            }
            override fun setUserInfo(userInfo: UserInfo?, callback: ResultCallback?) {
                if(callback == null) return println("fred: callback is null")
                try {
                    scope.launch {
                        delay(3000)
                        userData = userInfo
                        callback.onSuccess(userData)
                    }
                } catch(e: RemoteException) {
                    callback.onError(e.message)
                }
            }
        }
    }
}