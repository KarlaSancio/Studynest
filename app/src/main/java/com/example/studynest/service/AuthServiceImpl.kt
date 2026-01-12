package com.example.studynest.service

import com.example.studynest.domain.Usuario
import com.example.studynest.persistence.dao.UsuarioDAO

class AuthServiceImpl(
    private val usuarioDAO: UsuarioDAO
) : AuthService {

    override suspend fun login(email: String, password: String): Result<Usuario> {
        val loginResult = usuarioDAO.login(email, password)
        if (loginResult.isFailure) return Result.failure(loginResult.exceptionOrNull()!!)

        return usuarioDAO.getUserProfile(loginResult.getOrThrow())
    }

    override suspend fun register(
        name: String,
        email: String,
        phone: String,
        gender: String,
        password: String
    ): Result<Usuario> {

        val authResult =
            usuarioDAO.register(name, email, phone, gender, password)

        if (authResult.isFailure)
            return Result.failure(authResult.exceptionOrNull()!!)

        val usuario = Usuario(
            id = authResult.getOrThrow(),
            name = name,
            email = email,
            phone = phone,
            gender = gender
        )

        val profileResult = usuarioDAO.saveUserProfile(usuario)

        if (profileResult.isFailure)
            return Result.failure(profileResult.exceptionOrNull()!!)

        return Result.success(usuario)
    }


    override fun logout() {
        usuarioDAO.logout()
    }
}
