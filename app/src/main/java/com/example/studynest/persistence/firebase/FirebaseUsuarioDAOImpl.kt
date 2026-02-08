package com.example.studynest.persistence.firebase

import com.example.studynest.domain.Disciplina
import com.example.studynest.domain.Usuario
import com.example.studynest.persistence.dao.UsuarioDAO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseUsuarioDAOImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : UsuarioDAO {

    override suspend fun login(email: String, password: String): Result<String> =
        try {
            val result = auth
                .signInWithEmailAndPassword(email, password)
                .await()
            Result.success(result.user!!.uid)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun register(
        name: String,
        email: String,
        phone: String,
        gender: String,
        password: String
    ): Result<String> =
        try {
            val authResult = auth
                .createUserWithEmailAndPassword(email, password)
                .await()
            Result.success(authResult.user!!.uid)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun logout() {
        auth.signOut()
    }

    override suspend fun getUserProfile(uid: String): Result<Usuario> {
        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            if (!snapshot.exists()) {
                return Result.failure(IllegalStateException("Perfil não existe"))
            }
            val user = snapshot.toObject(Usuario::class.java)
            if (user == null) Result.failure(IllegalStateException("Falha ao mapear")) else Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveUserProfile(usuario: Usuario): Result<Unit> =
        try {
            firestore.collection("users").document(usuario.id).set(usuario).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    // --- FAVORITOS ---

    override suspend fun adicionarFavorito(userId: String, disciplina: Disciplina): Result<Unit> =
        try {
            firestore.collection("users")
                .document(userId)
                .collection("favorites")
                .document(disciplina.id)
                .set(disciplina)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun removerFavorito(userId: String, disciplinaId: String): Result<Unit> =
        try {
            firestore.collection("users")
                .document(userId)
                .collection("favorites")
                .document(disciplinaId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun listarFavoritos(userId: String): Result<List<Disciplina>> =
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("favorites")
                .get()
                .await()
            val lista = snapshot.toObjects(Disciplina::class.java)
            Result.success(lista)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun isFavorito(userId: String, disciplinaId: String): Result<Boolean> =
        try {
            val doc = firestore.collection("users")
                .document(userId)
                .collection("favorites")
                .document(disciplinaId)
                .get()
                .await()
            Result.success(doc.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
}