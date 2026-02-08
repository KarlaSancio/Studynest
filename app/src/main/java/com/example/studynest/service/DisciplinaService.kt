package com.example.studynest.service

import com.example.studynest.domain.Disciplina

interface DisciplinaService {
    suspend fun cadastrarDisciplina(nome: String, codigo: String, area: String, descricao: String): Result<Unit>
    suspend fun listarTodas(): Result<List<Disciplina>>
}