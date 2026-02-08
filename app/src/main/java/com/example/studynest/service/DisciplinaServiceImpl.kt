package com.example.studynest.service

import com.example.studynest.domain.Disciplina
import com.example.studynest.persistence.dao.DisciplinaDAO

class DisciplinaServiceImpl(private val dao: DisciplinaDAO) : DisciplinaService {

    override suspend fun cadastrarDisciplina(
        nome: String,
        codigo: String,
        area: String,
        descricao: String
    ): Result<Unit> {
        if (nome.isBlank() || codigo.isBlank()) {
            return Result.failure(IllegalArgumentException("Nome e Código são obrigatórios"))
        }

        val novaDisciplina = Disciplina(
            nome = nome,
            codigo = codigo,
            area = area,
            descricao = descricao
        )
        return dao.salvarDisciplina(novaDisciplina)
    }
    override suspend fun listarTodas(): Result<List<Disciplina>> {
        return dao.buscarTodas()
    }
}