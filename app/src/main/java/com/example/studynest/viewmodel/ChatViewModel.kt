package com.example.studynest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studynest.domain.Message
import com.example.studynest.repository.ChatRepository

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    fun startListening(disciplinaId: String) {
        repository.listenMessages(disciplinaId) {
            _messages.postValue(it)
        }
    }

    fun sendMessage(disciplinaId: String, message: Message) {
        repository.sendMessage(disciplinaId, message)
    }
}
