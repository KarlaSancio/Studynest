package com.example.studynest.persistence.dao

import com.example.studynest.domain.Usuario

interface UsuarioDAO {

    suspend fun login(email: String, password: String): Result<String>

    suspend fun register(
        name: String,
        email: String,
        phone: String,
        gender: String,
        password: String
    ): Result<String>

    fun logout()

    suspend fun getUserProfile(uid: String): Result<Usuario>

    suspend fun saveUserProfile(usuario: Usuario): Result<Unit>
}