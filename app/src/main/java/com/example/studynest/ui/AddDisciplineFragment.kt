package com.example.studynest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.studynest.R
import com.example.studynest.databinding.FragmentAddDisciplineBinding
import com.example.studynest.persistence.firebase.FirebaseDisciplinaDAOImpl
import com.example.studynest.service.DisciplinaServiceImpl
import com.example.studynest.viewmodel.AddDisciplineViewModel
import com.example.studynest.viewmodel.AddDisciplineViewModelFactory
import com.example.studynest.viewmodel.AddState

class AddDisciplineFragment : Fragment() {

    private lateinit var binding: FragmentAddDisciplineBinding

    // Injeção de dependência manual (criando Service e DAO aqui)
    private val viewModel: AddDisciplineViewModel by viewModels {
        val dao = FirebaseDisciplinaDAOImpl()
        val service = DisciplinaServiceImpl(dao)
        AddDisciplineViewModelFactory(service)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDisciplineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnEnviar.setOnClickListener {
            val nome = binding.edtNome.text.toString()
            val codigo = binding.edtCodigo.text.toString()
            val area = binding.edtArea.text.toString()
            val descricao = binding.edtDescricao.text.toString()

            viewModel.salvar(nome, codigo, area, descricao)
        }
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AddState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnEnviar.isEnabled = false
                }
                is AddState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Disciplina criada com sucesso!", Toast.LENGTH_SHORT).show()

                    // Volta para a Home e reseta o estado
                    viewModel.resetState()
                    findNavController().navigate(R.id.homeFragment)
                    // Obs: Se quiser voltar para a lista de "Meus Favoritos" ou "Pesquisar", mude o ID aqui.
                }
                is AddState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnEnviar.isEnabled = true
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnEnviar.isEnabled = true
                }
            }
        }
    }
}