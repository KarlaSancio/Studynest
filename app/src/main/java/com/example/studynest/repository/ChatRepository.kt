package com.example.studynest.repository

import com.example.studynest.domain.Message
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepository {

    private val db = FirebaseFirestore.getInstance()

    fun listenMessages(
        disciplinaId: String,
        onUpdate: (List<Message>) -> Unit
    ) {
        db.collection("disciplines")
            .document(disciplinaId)
            .collection("chat")
            .document("messages")
            .collection("items")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                val list = snapshot?.toObjects(Message::class.java) ?: emptyList()
                onUpdate(list)
            }
    }

    fun sendMessage(disciplinaId: String, message: Message) {
        db.collection("disciplines")
            .document(disciplinaId)
            .collection("chat")
            .document("messages")
            .collection("items")
            .add(message)
    }
}