package com.example.studynest.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.studynest.R
import com.example.studynest.databinding.FragmentHomeBinding
import com.example.studynest.viewmodel.AuthResultState
import com.example.studynest.viewmodel.AuthViewModel
import com.example.studynest.viewmodel.AuthViewModelFactory

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupUserInfo()
        setupListeners()
    }

    private fun setupUserInfo() {
        val state = viewModel.userState.value
        if (state is AuthResultState.Success) {
            binding.txtUserName.text = state.user.name
        }
    }

    private fun setupListeners() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            findNavController()
                .navigate(R.id.loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
