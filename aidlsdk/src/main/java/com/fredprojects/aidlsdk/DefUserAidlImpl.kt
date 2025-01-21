package com.fredprojects.aidlsdk

import android.content.Context
import android.util.Log
import com.fredprojects.aidlsdk.models.UserInfo
import com.fredprojects.aidlsdk.utils.ResultCallback
import kotlinx.coroutines.*
import kotlin.coroutines.resume

class DefUserAidlImpl(
    private val context: Context
) {
    private val serviceConnector by lazy { AidlServiceConnector(context) }
    private var i = 0
    private suspend fun awaitResponse(
        action: (ResultCallback) -> Unit
    ): UserInfo? = suspendCancellableCoroutine { continuation ->
        val resultCallback = object : ResultCallback.Stub() {
            override fun onSuccess(info: UserInfo?) {
                continuation.resume(info)
            }
            override fun onError(errorMessage: String?) {
                Log.e("fred", "onError: $errorMessage")
                continuation.resume(null)
            }
        }
        action(resultCallback)
        if(i % 3 == 2) {
            i++
            continuation.cancel(Exception("error awaitResponse"))
        }
        continuation.invokeOnCancellation {
            resultCallback.onError(it?.message)
        }
    }
    suspend fun getUserInfoOrNull(): UserInfo? = executeUserStorageAction { userStorage ->
        awaitResponse {
            if(i % 3 == 1) {
                i++
                throw Exception("error getUserInfoOrNull")
            }
            userStorage.getUserInfo(it)
        }
    }
    /**
     * @return true if success otherwise false
     */
    suspend fun setUserInfo(userInfo: UserInfo): Boolean = executeUserStorageAction { userStorage ->
        awaitResponse {
            if(i % 3 == 0) {
                i++
                throw Exception("error setUserInfo")
            }
            userStorage.setUserInfo(userInfo, it)
        }
    } != null
    private suspend fun<T> executeUserStorageAction(action: suspend (UserAIDLWithCallback) -> T): T? {
        val (serviceConnection, userStorage) = serviceConnector.connect()
        if(userStorage == null) return null
        return runCatching {
            action(userStorage)
        }.onSuccess {
            serviceConnector.disconnect(serviceConnection)
        }.onFailure { e ->
            Log.w("fred", "$i executeUserStorageAction: ${e.message}")
            serviceConnector.disconnect(serviceConnection)
        }.getOrNull()
    }
}