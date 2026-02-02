package com.example.studynest.viewmodel

import androidx.lifecycle.*
import com.example.studynest.domain.Disciplina
import com.example.studynest.service.DisciplinaService
import kotlinx.coroutines.launch

class SearchDisciplineViewModel(private val service: DisciplinaService) : ViewModel() {

    // Lista completa (cache)
    private var fullList = listOf<Disciplina>()

    // Lista que aparece na tela (filtrada)
    private val _disciplines = MutableLiveData<List<Disciplina>>()
    val disciplines: LiveData<List<Disciplina>> = _disciplines

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadDisciplines() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = service.listarTodas()
            _isLoading.value = false

            if (result.isSuccess) {
                fullList = result.getOrThrow()
                _disciplines.value = fullList // Inicialmente mostra tudo
            }
        }
    }

    fun filter(query: String) {
        if (query.isBlank()) {
            _disciplines.value = fullList
            return
        }

        // Filtra por Nome OU Código (ignorando maiúsculas/minúsculas)
        val filtered = fullList.filter {
            it.nome.contains(query, ignoreCase = true) ||
                    it.codigo.contains(query, ignoreCase = true)
        }
        _disciplines.value = filtered
    }
}

// Factory
class SearchDisciplineViewModelFactory(private val service: DisciplinaService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchDisciplineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchDisciplineViewModel(service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}