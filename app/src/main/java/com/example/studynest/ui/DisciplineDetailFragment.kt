package com.example.studynest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studynest.R
import com.example.studynest.databinding.FragmentDisciplineDetailBinding
import com.example.studynest.domain.Disciplina
import com.example.studynest.persistence.firebase.FirebaseUsuarioDAOImpl
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.ContextCompat

class DisciplineDetailFragment : Fragment() {

    private lateinit var binding: FragmentDisciplineDetailBinding

    private val args: DisciplineDetailFragmentArgs by navArgs()

    // Instâncias do Firebase e DAO
    private val auth = FirebaseAuth.getInstance()
    private val usuarioDAO = FirebaseUsuarioDAOImpl()

    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisciplineDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val disciplina = args.disciplinaSelecionada

        setupUI(disciplina)
        checkFavoriteStatus(disciplina.id)
        setupListeners(disciplina)
    }

    private fun setupUI(disciplina: Disciplina) {
        binding.txtDetailNome.text = disciplina.nome
        binding.txtDetailCodigo.text = disciplina.codigo
        binding.txtDetailDescricao.text = disciplina.descricao
    }

    private fun checkFavoriteStatus(disciplinaId: String) {
        val uid = auth.currentUser?.uid ?: return

        // Desabilita o botão enquanto carrega para evitar cliques errados
        binding.btnFavoritar.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            val result = usuarioDAO.isFavorito(uid, disciplinaId)

            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    isFavorite = result.getOrThrow()
                    updateFavoriteButtonUI()
                }
                binding.btnFavoritar.isEnabled = true
            }
        }
    }

    // Ação de Favoritar/Desfavoritar
    private fun toggleFavorite(disciplina: Disciplina) {
        val uid = auth.currentUser?.uid ?: return
        isFavorite = !isFavorite
        updateFavoriteButtonUI()

        CoroutineScope(Dispatchers.IO).launch {
            val result = if (isFavorite) {
                usuarioDAO.adicionarFavorito(uid, disciplina)
            } else {
                usuarioDAO.removerFavorito(uid, disciplina.id)
            }
            if (result.isFailure) {
                withContext(Dispatchers.Main) {
                    isFavorite = !isFavorite // Reverte
                    updateFavoriteButtonUI()
                    Toast.makeText(context, "Erro ao atualizar favorito", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Atualiza Texto e Ícone do botão
    private fun updateFavoriteButtonUI() {
        val context = requireContext()
        val purple = ContextCompat.getColorStateList(context, R.color.studynest_purple)

        if (isFavorite) {
            binding.btnFavoritar.text = "Favoritado"
            binding.btnFavoritar.iconTint = purple
        } else {
            binding.btnFavoritar.text = "Favoritar"
            binding.btnFavoritar.iconTint = purple

        }
    }

    private fun setupListeners(disciplina: Disciplina) {
        // Botão Voltar
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Botão Favoritar
        binding.btnFavoritar.setOnClickListener {
            toggleFavorite(disciplina)
        }

        // Botão Materiais
        binding.btnMateriais.setOnClickListener {
            Toast.makeText(context, "Abrir Lista de Materiais", Toast.LENGTH_SHORT).show()
        }

        // Botão Adicionar Material
        binding.btnAddMaterial.setOnClickListener {
            Toast.makeText(context, "Ir para tela de Upload", Toast.LENGTH_SHORT).show()
        }

        // Botão Chat
        binding.btnChat.setOnClickListener {
            Toast.makeText(context, "Abrir Chat", Toast.LENGTH_SHORT).show()
        }
    }
}