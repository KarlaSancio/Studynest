package com.example.studynest.persistence.dao

import com.example.studynest.domain.Disciplina

interface DisciplinaDAO {
    suspend fun salvarDisciplina(disciplina: Disciplina): Result<Unit>
    suspend fun buscarTodas(): Result<List<Disciplina>>
}