package com.fredprojects.myapplication

import android.content.*
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.IBinder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fredprojects.aidlsdk.UserAIDLWithCallback
import com.fredprojects.aidlsdk.models.UserInfo
import com.fredprojects.aidlsdk.utils.ResultCallback
import com.fredprojects.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    // ctrl + f, ctrl + r, enter this (UserAIDLWithCallback or UserAIDL) to replace and check all variants
    private var userStorage: UserAIDLWithCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        binding.button.setOnClickListener {
//            userStorage?.getUserInfo()?.let { toast(it.toString()) } ?: toast("info is null")
//        }
//        binding.button2.setOnClickListener {
//            val user = UserInfo(
//                binding.editTextText.text.toString(),
//                binding.editTextText.id
//            )
//            userStorage?.setUserInfo(user)
//        }
        //for aidl with callbacks
        binding.button.setOnClickListener {
            userStorage?.getUserInfo(defResultCallback)
        }
        binding.button2.setOnClickListener {
            val user = UserInfo(
                binding.editTextText.text.toString(),
                binding.editTextText.id
            )
            userStorage?.setUserInfo(user, defResultCallback)
        }
    }
    //for aidl with callbacks
    private val defResultCallback by lazy {
        object : ResultCallback.Stub() {
            override fun onSuccess(info: UserInfo?) {
                binding.textView.text = info.toString()
            }
            override fun onError(errorMessage: String?) {
                binding.textView.text = errorMessage.toString()
            }
        }
    }
    private val appUpdateReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.data?.schemeSpecificPart == AIDL_SERVICE_PACKAGE) {
                    unregisterReceiver(this)
                    connect()
                }
            }
        }
    }
    private val serviceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                userStorage = UserAIDLWithCallback.Stub.asInterface(service)
            }
            override fun onServiceDisconnected(name: ComponentName?) {
                userStorage = null
                toast("service disconnected")
            }
        }
    }
    override fun onStart() {
        super.onStart()
        connect()
    }
    override fun onStop() {
        super.onStop()
        disconnect()
    }
    // action = choose AIDL_SERVICE_ACTION1 or AIDL_SERVICE_ACTION2
    // open manifest
    private fun connect(action: String = AIDL_SERVICE_ACTION2) {
        bindService(createExplicitIntent(action), serviceConnection, Context.BIND_AUTO_CREATE)
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addDataScheme("package")
        }
        registerReceiver(appUpdateReceiver, intentFilter)
    }

    private fun disconnect() {
        unregisterReceiver(appUpdateReceiver)
        unbindService(serviceConnection)
    }

    private fun createExplicitIntent(action: String): Intent {
        val intent = Intent(action)
        val services: List<ResolveInfo> = packageManager.queryIntentServices(intent, 0)
        if (services.isEmpty()) {
            toast("service not found")
        }
        return Intent(intent).apply {
            val resolveInfo = services[0]
            val packageName = resolveInfo.serviceInfo.packageName
            val className = resolveInfo.serviceInfo.name
            component = ComponentName(packageName, className)
        }
    }
    companion object {
        private const val AIDL_SERVICE_PACKAGE = "com.fredprojects.aidlservice"
        private const val AIDL_SERVICE_ACTION1 = "com.fredprojects.aidlservice.UserService"
        private const val AIDL_SERVICE_ACTION2 = "com.fredprojects.aidlservice.UserServiceWithCallbacks"
    }
}