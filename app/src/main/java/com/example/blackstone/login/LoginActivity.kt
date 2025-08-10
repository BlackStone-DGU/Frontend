package com.example.blackstone.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.blackstone.MainActivity
import com.example.blackstone.R
import com.example.blackstone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.WinnerFit_black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.WinnerFit_black)

        // ViewBinding 설정
        binding = ActivityLoginBinding.inflate(layoutInflater)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(binding.root)

        // 로그인 버튼 클릭 → 메인화면 이동
        binding.btnLoginContainer.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // "회원가입" 텍스트 클릭 → 회원가입 화면으로 이동
        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}