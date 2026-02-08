package com.example.studynest.persistence.dao

import com.example.studynest.domain.Disciplina
import com.example.studynest.domain.Usuario

interface UsuarioDAO {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(name: String, email: String, phone: String, gender: String, password: String): Result<String>

    fun logout()

    suspend fun getUserProfile(uid: String): Result<Usuario>
    suspend fun saveUserProfile(usuario: Usuario): Result<Unit>

    // Favoritos
    suspend fun adicionarFavorito(userId: String, disciplina: Disciplina): Result<Unit>
    suspend fun removerFavorito(userId: String, disciplinaId: String): Result<Unit>
    suspend fun listarFavoritos(userId: String): Result<List<Disciplina>>
    suspend fun isFavorito(userId: String, disciplinaId: String): Result<Boolean>
}