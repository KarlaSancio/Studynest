package com.example.studynest.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.studynest.R
import com.example.studynest.databinding.FragmentRegisterBinding
import com.example.studynest.viewmodel.AuthResultState
import com.example.studynest.viewmodel.AuthViewModel
import com.example.studynest.viewmodel.AuthViewModelFactory

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterBinding.bind(view)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            viewModel.register(
                name = binding.edtName.text.toString(),
                email = binding.edtEmail.text.toString(),
                phone = binding.edtPhone.text.toString(),
                gender = binding.edtGender.text.toString(),
                pass1 = binding.edtPassword1.text.toString(),
                pass2 = binding.edtPassword2.text.toString()
            )
        }
    }

    private fun observeViewModel() {
        viewModel.userState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthResultState.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Cadastro realizado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()

                    viewModel.clearState()

                    findNavController().navigate(
                        R.id.action_register_to_login
                    )
                }

                is AuthResultState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                AuthResultState.Idle -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
