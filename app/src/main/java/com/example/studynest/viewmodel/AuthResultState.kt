package com.example.studynest.viewmodel

import com.example.studynest.domain.Usuario

sealed class AuthResultState {
    object Idle : AuthResultState()
    data class Success(val user: Usuario) : AuthResultState()
    data class Error(val message: String) : AuthResultState()
}
