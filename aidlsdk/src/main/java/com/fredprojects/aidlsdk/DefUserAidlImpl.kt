package com.fredprojects.aidlsdk

import android.content.Context
import android.util.Log
import com.fredprojects.aidlsdk.models.UserInfo
import com.fredprojects.aidlsdk.utils.ResultCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefUserAidlImpl(
    private val context: Context
) {
    private var userData: UserInfo? = null
    private val serviceConnector by lazy { AidlServiceConnector(context) }
    private val defResultCallback by lazy {
        object : ResultCallback.Stub() {
            override fun onSuccess(info: UserInfo?) {
                Log.e("fred", "ResultCallback onSuccess $info")
                userData = info
            }
            override fun onError(errorMessage: String?) {
                Log.e("fred", errorMessage.toString())
            }
        }
    }
    suspend fun getUserInfoOrNull(): UserInfo? = with(serviceConnector) {
        Log.e("fred", "getUserInfoOrNull start")
        serviceConnector.connect()
        withContext(Dispatchers.IO) {
            userStorage?.getUserInfo(defResultCallback)
            Log.e("fred", "$userStorage userStorage, $userData userData")
        }
        serviceConnector.disconnect()
        Log.e("fred", "getUserInfoOrNull end")
        return userData
    }
    suspend fun setUserInfo(userInfo: UserInfo): Boolean = with(serviceConnector) {
        Log.e("fred", "setUserInfo start")
//        if(userStorage == null) return false
        serviceConnector.connect()
        withContext(Dispatchers.IO) {
            userStorage?.setUserInfo(userInfo, defResultCallback)
            Log.e("fred", "$userStorage userStorage, $userData userData")
        }
        serviceConnector.disconnect()
        Log.e("fred", "setUserInfo end")
        return true
    }
}