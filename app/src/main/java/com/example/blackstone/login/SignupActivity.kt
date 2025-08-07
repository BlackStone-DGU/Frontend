package com.example.blackstone.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blackstone.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var step = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1단계 프래그먼트 로딩
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, SignupStep1Fragment())
            .commit()

        // 다음 버튼 클릭
        binding.btnLoginContainer.setOnClickListener {
            when (step) {
                1 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id, SignupStep2Fragment())
                        .addToBackStack(null)
                        .commit()
                    step = 2
                    binding.btnNext.text = "다음"
                }

                2 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id, SignupStep3Fragment())
                        .addToBackStack(null)
                        .commit()
                    step = 3
                    binding.btnNext.text = "가입하기"
                }

                3 -> {
                    val intent = Intent(this, CongratsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        // "로그인" 클릭 시
        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}