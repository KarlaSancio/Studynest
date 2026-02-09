package com.example.studynest.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studynest.databinding.FragmentMaterialListBinding
import com.example.studynest.repository.Material
import com.example.studynest.repository.MaterialRepository
import kotlinx.coroutines.launch

class MaterialListFragment : Fragment() {

    private var _binding: FragmentMaterialListBinding? = null
    private val binding get() = _binding!!

    private val args: MaterialListFragmentArgs by navArgs()
    private val materialRepository = MaterialRepository()

    private var allMaterials = listOf<Material>()
    private lateinit var materialAdapter: MaterialListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaterialListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        loadMaterials()
    }

    private fun setupRecyclerView() {
        materialAdapter = MaterialListAdapter(listOf()) { material ->
            openMaterialUrl(material.urlArquivo)
        }
        binding.rvMaterials.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = materialAdapter
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterMaterials(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadMaterials() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                allMaterials = materialRepository.getMaterialsForDiscipline(args.idDisciplina)
                materialAdapter.updateMaterials(allMaterials)
                if (allMaterials.isEmpty()) {
                    Toast.makeText(context, "Nenhum material encontrado.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Falha ao carregar materiais: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun filterMaterials(query: String) {
        val filteredList = if (query.isEmpty()) {
            allMaterials
        } else {
            allMaterials.filter {
                it.nomeArquivo.contains(query, ignoreCase = true)
            }
        }
        materialAdapter.updateMaterials(filteredList)
    }

    private fun openMaterialUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Nenhum aplicativo encontrado para abrir este arquivo.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Não foi possível abrir o link do arquivo.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
