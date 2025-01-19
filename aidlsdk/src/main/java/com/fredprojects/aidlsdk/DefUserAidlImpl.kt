package com.fredprojects.aidlsdk

import android.content.Context
import com.fredprojects.aidlsdk.models.UserInfo
import com.fredprojects.aidlsdk.utils.ResultCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class DefUserAidlImpl(
    private val context: Context
) {
    private val serviceConnector by lazy { AidlServiceConnector(context) }
    private suspend fun awaitResponse(
        action: (ResultCallback) -> Unit
    ): UserInfo? = suspendCancellableCoroutine {
        val resultCallback = object : ResultCallback.Stub() {
            override fun onSuccess(info: UserInfo?) {
                it.resume(info)
            }
            override fun onError(errorMessage: String?) {
                it.resume(null)
            }
        }
        action(resultCallback)
    }
    suspend fun getUserInfoOrNull(): UserInfo? {
        return try {
            val (serviceConnection, userStorage) = serviceConnector.connect()
            if(userStorage == null) return null
            val user = awaitResponse { userStorage.getUserInfo(it) }
            serviceConnector.disconnect(serviceConnection)
            user
        } catch(e: Exception) {
            null
        }
    }
    /**
     * @return true if success otherwise false
     */
    suspend fun setUserInfo(userInfo: UserInfo): Boolean {
        return try {
            val (serviceConnection, userStorage) = serviceConnector.connect()
            if(userStorage == null) return false
            val isSuccess = awaitResponse { userStorage.setUserInfo(userInfo, it) } != null
            serviceConnector.disconnect(serviceConnection)
            isSuccess
        } catch(e: Exception) {
            false
        }
    }
}