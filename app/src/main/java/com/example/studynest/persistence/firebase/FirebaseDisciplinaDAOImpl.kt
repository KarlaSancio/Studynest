package com.example.studynest.persistence.firebase

import com.example.studynest.domain.Disciplina
import com.example.studynest.persistence.dao.DisciplinaDAO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirebaseDisciplinaDAOImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : DisciplinaDAO {

    override suspend fun salvarDisciplina(disciplina: Disciplina): Result<Unit> = try {
        // Geramos um ID único para a disciplina se ela não tiver (embora o UUID abaixo garanta)
        val id = if (disciplina.id.isEmpty()) UUID.randomUUID().toString() else disciplina.id

        val disciplinaComId = disciplina.copy(id = id)

        firestore.collection("disciplinas")
            .document(id)
            .set(disciplinaComId)
            .await()

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun buscarTodas(): Result<List<Disciplina>> = try {
        val snapshot = firestore.collection("disciplinas")
            .get()
            .await()

        val lista = snapshot.toObjects(Disciplina::class.java)
        Result.success(lista)
    } catch (e: Exception) {
        Result.failure(e)
    }
}