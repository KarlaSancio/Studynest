package com.example.studynest.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studynest.databinding.FragmentSearchDisciplineBinding
import com.example.studynest.persistence.firebase.FirebaseDisciplinaDAOImpl
import com.example.studynest.service.DisciplinaServiceImpl
import com.example.studynest.ui.adapter.DisciplineAdapter
import com.example.studynest.viewmodel.SearchDisciplineViewModel
import com.example.studynest.viewmodel.SearchDisciplineViewModelFactory

class SearchDisciplineFragment : Fragment() {

    private lateinit var binding: FragmentSearchDisciplineBinding
    private lateinit var adapter: DisciplineAdapter

    private val viewModel: SearchDisciplineViewModel by viewModels {
        val dao = FirebaseDisciplinaDAOImpl()
        val service = DisciplinaServiceImpl(dao)
        SearchDisciplineViewModelFactory(service)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchDisciplineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchInput()
        setupObservers()

        // Carrega os dados
        viewModel.loadDisciplines()
    }

    private fun setupRecyclerView() {
        adapter = DisciplineAdapter { disciplina ->
            // AQUI SERÁ O CLIQUE PARA ABRIR DETALHES (Futuro)
            Toast.makeText(context, "Selecionado: ${disciplina.nome}", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerSearch.layoutManager = LinearLayoutManager(context)
        binding.recyclerSearch.adapter = adapter
    }

    private fun setupSearchInput() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupObservers() {
        viewModel.disciplines.observe(viewLifecycleOwner) { lista ->
            adapter.updateList(lista)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressSearch.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}