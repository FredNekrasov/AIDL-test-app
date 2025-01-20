package com.fredprojects.aidlsdk

import android.content.*
import android.os.IBinder
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal class AidlServiceConnector(
    private val context: Context
) {
    private fun createServiceConnection(
        continuation: CancellableContinuation<Pair<ServiceConnection, UserAIDLWithCallback?>>
    ): ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            continuation.resume(this to UserAIDLWithCallback.Stub.asInterface(service))
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            continuation.resume(this to null)
        }
    }
    private fun bindService(serviceConnection: ServiceConnection): Boolean = context.bindService(
        Intent(AIDL_SERVICE_ACTION).setPackage(AIDL_SERVICE_PACKAGE),
        serviceConnection,
        Context.BIND_AUTO_CREATE
    )
    suspend fun connect(): Pair<ServiceConnection, UserAIDLWithCallback?> = suspendCancellableCoroutine {
        val serviceConnection = createServiceConnection(it)
        val isConnected = bindService(serviceConnection)
        if(!isConnected) it.resume(serviceConnection to null)
        it.invokeOnCancellation { _ ->
            if(isConnected) disconnect(serviceConnection)
        }
    }
    fun disconnect(serviceConnection: ServiceConnection) {
        context.unbindService(serviceConnection)
    }
    companion object {
        private const val AIDL_SERVICE_PACKAGE = "com.fredprojects.aidlservice"
        private const val AIDL_SERVICE_ACTION = "$AIDL_SERVICE_PACKAGE.UserServiceWithCallbacks"
    }
}