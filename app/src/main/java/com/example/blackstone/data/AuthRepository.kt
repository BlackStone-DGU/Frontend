// data/AuthRepository.kt
package com.example.blackstone.data

import android.content.Context
import retrofit2.Retrofit
import com.example.blackstone.model.*
import com.example.blackstone.network.AuthApi

class AuthRepository(
    private val retrofit: Retrofit,
    private val appContext: Context
) {
    private val api = retrofit.create(AuthApi::class.java)
    private val store = LocalAuthStore(appContext)

    suspend fun signup(req: SignupReq): Result<String> = runCatching {
        val res = api.signup(req)
        val body = if (res.isSuccessful) (res.body() ?: "") else (res.errorBody()?.string() ?: "")
        val msg = body.trim().replace("\"", "")

        if (res.isSuccessful && (msg == "회원가입 성공" || msg.isBlank())) {
            // ★ 가입 성공 시 로컬에 자격증명 저장
            store.saveUser(
                email = req.email,
                password = req.password,
                name = req.name,
                affiliation = req.affiliation.name
            )
            "회원가입 성공"
        } else {
            val code = res.code()
            when {
                code == 409 || body.contains("동일한 이메일") -> error("이미 사용 중인 이메일입니다.")
                code == 400 -> error("요청 형식이 올바르지 않습니다.")
                else -> error("회원가입 실패($code): ${body.ifBlank { "서버 오류" }}")
            }
        }
    }

    suspend fun login(req: LoginReq): Result<String> = runCatching {
        val res = api.login(req)
        val raw = if (res.isSuccessful) (res.body() ?: "") else (res.errorBody()?.string() ?: "")
        val body = raw.trim().replace("\"", "")

        if (res.isSuccessful && body == "로그인 성공") {
            // ★ 백엔드가 항상 성공을 줘도, 로컬에 저장된 가입정보와 비교해 필터링
            val savedEmail = store.getEmail()
            val savedPw = store.getPassword()
            if (savedEmail == null || savedPw == null) {
                // 가입 기록이 없으면 로그인 불가(데모 룰)
                throw IllegalStateException("먼저 회원가입을 진행해주세요.")
            }
            if (req.email == savedEmail && req.password == savedPw) {
                "로그인 성공"
            } else {
                throw IllegalStateException("이메일 또는 비밀번호가 올바르지 않습니다.")
            }
        } else if (!res.isSuccessful) {
            val code = res.code()
            throw IllegalStateException(
                when (code) {
                    401 -> "이메일 또는 비밀번호가 올바르지 않습니다."
                    else -> "로그인 실패($code): ${body.ifBlank { "서버 오류" }}"
                }
            )
        } else {
            // 200이지만 메시지가 다르면 실패
            throw IllegalStateException(body.ifBlank { "로그인 실패(응답 확인 필요)" })
        }
    }
}