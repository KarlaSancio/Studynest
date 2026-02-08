package com.example.studynest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studynest.domain.Disciplina
import com.example.studynest.persistence.dao.UsuarioDAO
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class FavoritesViewModel(private val usuarioDAO: UsuarioDAO) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private var fullList = listOf<Disciplina>() // Cache local

    private val _favorites = MutableLiveData<List<Disciplina>>()
    val favorites: LiveData<List<Disciplina>> = _favorites

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun loadFavorites() {
        val userId = auth.currentUser?.uid ?: return

        _loading.value = true
        viewModelScope.launch {
            val result = usuarioDAO.listarFavoritos(userId)
            _loading.value = false

            if (result.isSuccess) {
                fullList = result.getOrThrow()
                _favorites.value = fullList
            } else {
                _favorites.value = emptyList()
            }
        }
    }

    fun filter(query: String) {
        if (query.isBlank()) {
            _favorites.value = fullList
        } else {
            _favorites.value = fullList.filter {
                it.nome.contains(query, ignoreCase = true) ||
                        it.codigo.contains(query, ignoreCase = true)
            }
        }
    }
}

class FavoritesViewModelFactory(private val dao: UsuarioDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}