package com.example.blackstone.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.blackstone.data.AuthRepository
import com.example.blackstone.databinding.ActivitySignupBinding
import com.example.blackstone.network.ApiClient
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var step = 1
    lateinit var viewModelFactory: AuthViewModelFactory
    val vm: AuthViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = AuthViewModelFactory(
            AuthRepository(ApiClient.retrofit, applicationContext) // ← appContext 전달
        )

        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                vm.state.collect { st ->
                    // 로딩 중엔 중복 클릭 방지
                    binding.btnLoginContainer.isEnabled = !st.loading
                    // ProgressBar가 없다면 생략해도 OK (있다면 보여주기/숨기기)
                    // binding.progress.isVisible = st.loading

                    st.message?.let { msg ->
                        if (msg.contains("성공")) {
                            Toast.makeText(this@SignupActivity, msg, Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignupActivity, CongratsActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@SignupActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                    st.error?.let { err ->
                        Toast.makeText(this@SignupActivity, err, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Step1 시작
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, SignupStep1Fragment())
            .commit()

        // "다음/가입하기" 버튼 컨테이너 클릭
        binding.btnLoginContainer.setOnClickListener {
            when (step) {
                1 -> {
                    if (!validateStep1()) return@setOnClickListener
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id, SignupStep2Fragment())
                        .addToBackStack(null)
                        .commit()
                    step = 2
                    binding.btnNext.text = "다음"
                }
                2 -> {
                    if (!validateStep2()) return@setOnClickListener
                    supportFragmentManager.beginTransaction()
                        .replace(binding.fragmentContainer.id, SignupStep3Fragment())
                        .addToBackStack(null)
                        .commit()
                    step = 3
                    binding.btnNext.text = "가입하기"
                }
                3 -> {
                    if (!validateStep3()) return@setOnClickListener
                    vm.signupWithForm()
                }
            }
        }

        // 하단 "로그인" 이동
        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateStep1(): Boolean {
        val frag = supportFragmentManager.fragments
            .firstOrNull { it is SignupStep1Fragment && it.isVisible } as? SignupStep1Fragment
            ?: supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as? SignupStep1Fragment
        val (name, email, pw) = frag?.collectStep1() ?: Triple("", "", "")

        android.util.Log.d("SIGNUP", "step1 collected name='$name', email='$email', pw_len=${pw.length}")
        vm.setName(name); vm.setEmail(email); vm.setPassword(pw)
        val ok = name.isNotBlank() && email.isNotBlank() && pw.length >= 8
        if (!ok) toast("닉네임/이메일/8자 이상 비밀번호를 입력하세요")
        return ok
    }
    private fun validateStep2(): Boolean {
        val frag = supportFragmentManager.fragments
            .firstOrNull { it is SignupStep2Fragment && it.isVisible } as? SignupStep2Fragment
            ?: supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as? SignupStep2Fragment

        val affiliation = frag?.collectStep2().orEmpty()
        android.util.Log.d("SIGNUP", "step2 collected affiliation='$affiliation'")
        vm.setAffiliation(affiliation)

        val ok = affiliation.isNotBlank()
        if (!ok) toast("학교/학과를 입력하세요")
        return ok
    }
    private fun validateStep3(): Boolean {
        return true
    }
    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}