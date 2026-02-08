package com.example.studynest.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Base para enviar as informacoes do Material para o Firestore Database
data class Material( 
    val nomeArquivo: String = "",
    val urlArquivo: String = "",
    val idDisciplina: String = "",
    val idUsuario: String = "",
    val dataUpload: Timestamp = Timestamp.now(),
)

class MaterialRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun addMaterial(material: Material) {
        val currentUser = auth.currentUser ?: throw Exception("Usuário não autenticado")
        val materialWithUser = material.copy(
            idUsuario = currentUser.uid,
        )
        db.collection("materiais").add(materialWithUser).await()
    }
}
