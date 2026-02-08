package com.example.studynest.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studynest.databinding.FragmentFavoritesBinding 
import com.example.studynest.persistence.firebase.FirebaseUsuarioDAOImpl
import com.example.studynest.ui.adapter.DisciplineAdapter
import com.example.studynest.viewmodel.FavoritesViewModel
import com.example.studynest.viewmodel.FavoritesViewModelFactory

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var adapter: DisciplineAdapter

    private val viewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(FirebaseUsuarioDAOImpl())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavorites()
    }

    private fun setupRecyclerView() {
        adapter = DisciplineAdapter { disciplina ->
            val action = FavoritesFragmentDirections.actionFavoritesToDetail(disciplina)
            findNavController().navigate(action)
        }

        binding.recyclerFavorites.layoutManager = LinearLayoutManager(context)
        binding.recyclerFavorites.adapter = adapter
    }

    private fun setupSearch() {
        binding.edtSearchFav.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = viewModel.filter(s.toString())
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupObservers() {
        viewModel.favorites.observe(viewLifecycleOwner) { lista ->
            adapter.updateList(lista)
            binding.txtEmpty.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressFav.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}
