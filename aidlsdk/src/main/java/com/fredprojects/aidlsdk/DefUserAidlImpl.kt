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
    suspend fun getUserInfoOrNull(): UserInfo? {
        Log.e("fred", "getUserInfoOrNull start")
        try {
            executeFunc { serviceConnector.userStorage?.getUserInfo(defResultCallback) }
        } catch(e: Exception) {
            return null
        }
        Log.e("fred", "getUserInfoOrNull end")
        return userData
    }
    /**
     * @return true if success otherwise false
     */
    suspend fun setUserInfo(userInfo: UserInfo): Boolean = with(serviceConnector) {
        Log.e("fred", "setUserInfo start")
        if(userStorage == null) return false
        try {
            executeFunc { userStorage?.setUserInfo(userInfo, defResultCallback) }
        } catch(e: Exception) {
            return false
        }
        Log.e("fred", "setUserInfo end")
        return true
    }
    private suspend fun executeFunc(action: () -> Unit) = with(serviceConnector) {
        connect()
        withContext(Dispatchers.IO) {
            action()
            Log.e("fred", "$userStorage userStorage, $userData userData")
        }
        disconnect()
    }
}