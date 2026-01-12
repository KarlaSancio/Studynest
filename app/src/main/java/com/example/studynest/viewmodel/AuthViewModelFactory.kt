package com.example.studynest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studynest.persistence.firebase.FirebaseUsuarioDAOImpl
import com.example.studynest.service.AuthServiceImpl

class AuthViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {

            val usuarioDAO = FirebaseUsuarioDAOImpl()
            val authService = AuthServiceImpl(usuarioDAO)

            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authService) as T
        }

        throw IllegalArgumentException("ViewModel desconhecido")
    }
}
