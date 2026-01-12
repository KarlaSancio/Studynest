package com.example.studynest.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studynest.service.AuthService
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authService: AuthService
) : ViewModel() {

    private val _userState = MutableLiveData<AuthResultState>(AuthResultState.Idle)
    val userState: LiveData<AuthResultState> = _userState

    fun login(email: String, password: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _userState.value = AuthResultState.Error("Email inválido")
            return
        }

        viewModelScope.launch {
            val result = authService.login(email, password)
            _userState.value =
                if (result.isSuccess)
                    AuthResultState.Success(result.getOrThrow())
                else
                    AuthResultState.Error("Falha no login")
        }
    }

    fun register(
        name: String,
        email: String,
        phone: String,
        gender: String,
        pass1: String,
        pass2: String
    ) {
        if (pass1 != pass2) {
            _userState.value = AuthResultState.Error("Senhas diferentes")
            return
        }

        viewModelScope.launch {
            val result =
                authService.register(name, email, phone, gender, pass1)

            _userState.value =
                if (result.isSuccess)
                    AuthResultState.Success(result.getOrThrow())
                else
                    AuthResultState.Error("Erro ao cadastrar")
        }
    }

    fun logout() {
        authService.logout()
    }

    fun clearState() {
        _userState.value = AuthResultState.Idle
    }

}