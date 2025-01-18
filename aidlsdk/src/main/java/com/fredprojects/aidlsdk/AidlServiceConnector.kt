package com.fredprojects.aidlsdk

import android.content.*
import android.os.IBinder
import android.util.Log

internal class AidlServiceConnector(
    private val context: Context
) {
    var userStorage: UserAIDLWithCallback? = null
        private set
    private val serviceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Log.e("fred", "onServiceConnected ${service == null}")
                userStorage = UserAIDLWithCallback.Stub.asInterface(service)
            }
            override fun onServiceDisconnected(name: ComponentName?) {
                Log.e("fred", "onServiceDisconnected")
                userStorage = null
            }
        }
    }
    fun connect() {
        Log.e("fred", "connect")
        context.bindService(Intent(AIDL_SERVICE_ACTION).setPackage(AIDL_SERVICE_PACKAGE), serviceConnection, Context.BIND_AUTO_CREATE)
    }
    fun disconnect() {
        Log.e("fred", "disconnect")
        context.unbindService(serviceConnection)
    }
    companion object {
        private const val AIDL_SERVICE_PACKAGE = "com.fredprojects.aidlservice"
        private const val AIDL_SERVICE_ACTION = "$AIDL_SERVICE_PACKAGE.UserServiceWithCallbacks"
    }
}