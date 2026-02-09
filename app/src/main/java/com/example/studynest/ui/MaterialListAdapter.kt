package com.example.studynest.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studynest.databinding.ItemMaterialBinding
import com.example.studynest.repository.Material

class MaterialListAdapter(
    private var materials: List<Material>,
    private val onDownloadClick: (Material) -> Unit
) : RecyclerView.Adapter<MaterialListAdapter.MaterialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val binding = ItemMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MaterialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        holder.bind(materials[position])
    }

    override fun getItemCount() = materials.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateMaterials(newMaterials: List<Material>) {
        this.materials = newMaterials
        notifyDataSetChanged()
    }

    inner class MaterialViewHolder(private val binding: ItemMaterialBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(material: Material) {
            binding.tvFileName.text = material.nomeArquivo
            binding.btnDownload.setOnClickListener { onDownloadClick(material) }
        }
    }
}
