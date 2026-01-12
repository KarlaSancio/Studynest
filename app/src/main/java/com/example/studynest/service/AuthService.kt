package com.example.studynest.service

import com.example.studynest.domain.Usuario

interface AuthService {
    suspend fun login(email: String, password: String): Result<Usuario>

    suspend fun register(
        name: String,
        email: String,
        phone: String,
        gender: String,
        password: String
    ): Result<Usuario>

    fun logout()
}