package com.fredprojects.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.fredprojects.aidlsdk.DefUserAidlImpl
import com.fredprojects.aidlsdk.models.UserInfo
import com.fredprojects.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val userAidl by lazy { DefUserAidlImpl(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.button.setOnClickListener {
            lifecycleScope.launch {
                userAidl.getUserInfoOrNull().let {
                    binding.textView.text = it.toString()
                }
            }
        }
        binding.button2.setOnClickListener {
            val user = UserInfo(
                binding.editTextText.text.toString(),
                binding.editTextText.id
            )
            lifecycleScope.launch {
                userAidl.setUserInfo(user)
            }
        }
    }
}