package com.example.studynest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studynest.databinding.ItemDisciplineBinding
import com.example.studynest.domain.Disciplina

class DisciplineAdapter(
    private val onClick: (Disciplina) -> Unit
) : RecyclerView.Adapter<DisciplineAdapter.DisciplineViewHolder>() {

    private var listaOriginal = listOf<Disciplina>()

    fun updateList(novaLista: List<Disciplina>) {
        listaOriginal = novaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisciplineViewHolder {
        val binding = ItemDisciplineBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DisciplineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DisciplineViewHolder, position: Int) {
        holder.bind(listaOriginal[position])
    }

    override fun getItemCount(): Int = listaOriginal.size

    inner class DisciplineViewHolder(val binding: ItemDisciplineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(disciplina: Disciplina) {
            binding.txtNomeDisciplina.text = disciplina.nome
            binding.txtCodigoDisciplina.text = "(${disciplina.codigo})"

            binding.root.setOnClickListener {
                onClick(disciplina)
            }
        }
    }
}