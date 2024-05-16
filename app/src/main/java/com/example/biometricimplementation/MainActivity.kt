package com.example.biometricimplementation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.biometricimplementation.databinding.ActivityMainBinding
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var executor: Executor? = null
    private lateinit var biometricPrompt: androidx.biometric.BiometricPrompt
    private lateinit var promptInfo: PromptInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = androidx.biometric.BiometricPrompt(this@MainActivity, executor!!,object:androidx.biometric.BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
//                WHEN ERROR COMES
                binding.AuthStatus.text = buildString {
        append("ERROR")
    }
            }

            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                binding.AuthStatus.text = buildString {
        append("SUCCESSFUL AUTHENTICATED")
    }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                binding.AuthStatus.text = buildString {
        append("AUTHENTICATION FAILED")
    }
            }
        })
        promptInfo=androidx.biometric.BiometricPrompt.PromptInfo.Builder().setTitle("BIOMETRIC AUTHENTICATION")
            .setSubtitle("login using fingerprint or face")
            .setNegativeButtonText("Cancel")
            .build()

        binding.button.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}