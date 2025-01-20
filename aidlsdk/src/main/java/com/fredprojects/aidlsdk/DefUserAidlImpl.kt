package com.fredprojects.aidlsdk

import android.content.Context
import com.fredprojects.aidlsdk.models.UserInfo
import com.fredprojects.aidlsdk.utils.ResultCallback
import kotlinx.coroutines.*
import kotlin.coroutines.resume

class DefUserAidlImpl(
    private val context: Context
) {
    private val serviceConnector by lazy { AidlServiceConnector(context) }
    private suspend fun awaitResponse(
        action: (ResultCallback) -> Unit
    ): UserInfo? = suspendCancellableCoroutine { continuation ->
        val resultCallback = object : ResultCallback.Stub() {
            override fun onSuccess(info: UserInfo?) {
                continuation.resume(info)
            }
            override fun onError(errorMessage: String?) {
                continuation.resume(null)
            }
        }
        action(resultCallback)
        continuation.invokeOnCancellation {
            resultCallback.onError(it?.message)
        }
    }
    suspend fun getUserInfoOrNull(): UserInfo? = executeUserStorageAction { userStorage ->
        awaitResponse { userStorage.getUserInfo(it) }
    }
    /**
     * @return true if success otherwise false
     */
    suspend fun setUserInfo(userInfo: UserInfo): Boolean = executeUserStorageAction { userStorage ->
        awaitResponse { userStorage.setUserInfo(userInfo, it) }
    } != null
    private suspend fun<T> executeUserStorageAction(action: suspend (UserAIDLWithCallback) -> T): T? {
        return try {
            val (serviceConnection, userStorage) = serviceConnector.connect()
            if(userStorage == null) return null
            action(userStorage).also {
                serviceConnector.disconnect(serviceConnection)
            }
        } catch (e: Exception) {
            null
        }
    }
}