package com.fredprojects.aidlsdk

import android.content.*
import android.os.IBinder
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal class AidlServiceConnector(
    private val context: Context
) {
    suspend fun connect(): Pair<ServiceConnection, UserAIDLWithCallback?> = suspendCancellableCoroutine {
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                it.resume(this to UserAIDLWithCallback.Stub.asInterface(service))
            }
            override fun onServiceDisconnected(name: ComponentName?) {}
        }
        val isConnected = context.bindService(
            Intent(AIDL_SERVICE_ACTION).setPackage(AIDL_SERVICE_PACKAGE),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
        if(!isConnected) it.resume(serviceConnection to null)
    }
    fun disconnect(serviceConnection: ServiceConnection) {
        context.unbindService(serviceConnection)
    }
    companion object {
        private const val AIDL_SERVICE_PACKAGE = "com.fredprojects.aidlservice"
        private const val AIDL_SERVICE_ACTION = "$AIDL_SERVICE_PACKAGE.UserServiceWithCallbacks"
    }
}