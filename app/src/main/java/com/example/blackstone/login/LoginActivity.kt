package com.example.blackstone.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.blackstone.MainActivity
import com.example.blackstone.data.AuthRepository
import com.example.blackstone.databinding.ActivityLoginBinding
import com.example.blackstone.network.ApiClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // ViewModel + Factory (Signup과 동일 패턴)
    private lateinit var viewModelFactory: AuthViewModelFactory
    private val vm: AuthViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = AuthViewModelFactory(
            AuthRepository(ApiClient.retrofit, applicationContext)
        )

        // ★ 상태 관찰: 로딩/성공/실패 처리
        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                vm.state.collect { st ->
                    // 로딩 중 중복 클릭 방지
                    binding.btnLoginContainer.isEnabled = !st.loading
                    // (로딩 UI 없으면 이 줄은 생략 가능)
                    // binding.progress.isVisible = st.loading

                    st.message?.let { msg ->
                        if (msg == "로그인 성공") {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                    st.error?.let { err ->
                        Toast.makeText(this@LoginActivity, err, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 로그인 버튼 → API 호출
        binding.btnLoginContainer.setOnClickListener {
            currentFocus?.clearFocus()
            val email = binding.etEmail.text?.toString()?.trim().orEmpty()
            val pw = binding.etPassword.text?.toString().orEmpty()
            if (email.isBlank() || pw.isBlank()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            vm.login(email, pw)
        }

        // "회원가입" 이동
        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}