package com.example.studynest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studynest.service.DisciplinaService
import kotlinx.coroutines.launch

// Estado da tela (Carregando, Sucesso ou Erro)
sealed class AddState {
    object Idle : AddState()
    object Loading : AddState()
    object Success : AddState()
    data class Error(val message: String) : AddState()
}

class AddDisciplineViewModel(private val service: DisciplinaService) : ViewModel() {

    private val _state = MutableLiveData<AddState>(AddState.Idle)
    val state: LiveData<AddState> = _state

    fun salvar(nome: String, codigo: String, area: String, descricao: String) {
        _state.value = AddState.Loading

        viewModelScope.launch {
            val result = service.cadastrarDisciplina(nome, codigo, area, descricao)
            if (result.isSuccess) {
                _state.value = AddState.Success
            } else {
                _state.value = AddState.Error(result.exceptionOrNull()?.message ?: "Erro desconhecido")
            }
        }
    }

    fun resetState() { _state.value = AddState.Idle }
}

// Factory para criar o ViewModel (necessário pois passamos argumentos no construtor)
class AddDisciplineViewModelFactory(private val service: DisciplinaService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddDisciplineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddDisciplineViewModel(service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}