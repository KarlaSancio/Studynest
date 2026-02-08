package com.example.studynest.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Disciplina(
    val id: String = "",
    val nome: String = "",
    val codigo: String = "",
    val area: String = "",
    val descricao: String = ""
) : Parcelable