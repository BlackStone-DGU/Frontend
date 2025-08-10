package com.example.blackstone.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blackstone.data.AuthRepository
import com.example.blackstone.model.AffiliationReq
import com.example.blackstone.model.LoginReq
import com.example.blackstone.model.SignupReq
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UiState(
    val loading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)

data class SignupForm(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var affiliationName: String = ""
)

class AuthViewModel(
    private val repo: AuthRepository
) : ViewModel() {
    val form = SignupForm()

    fun setEmail(v: String) { form.email = v }
    fun setPassword(v: String) { form.password = v }
    fun setName(v: String) { form.name = v }
    fun setAffiliation(v: String) { form.affiliationName = v }

    fun signupWithForm() = signup(
        email = form.email,
        pw = form.password,
        name = form.name,
        affiliationName = form.affiliationName
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    fun signup(email: String, pw: String, name: String, affiliationName: String) {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            val req = SignupReq(
                email = email,
                password = pw,
                name = name,
                affiliation = AffiliationReq(name = affiliationName)
            )
            repo.signup(req)
                .onSuccess { msg -> _state.value = UiState(message = msg) }
                .onFailure { e -> _state.value = UiState(error = e.message ?: "회원가입 실패") }
        }
    }

    fun login(email: String, pw: String) {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            val req = LoginReq(email = email, password = pw)
            repo.login(req)
                .onSuccess { msg -> _state.value = UiState(message = msg) }
                .onFailure { e -> _state.value = UiState(error = e.message ?: "로그인 실패") }
        }
    }
}