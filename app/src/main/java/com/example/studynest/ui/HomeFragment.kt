package com.example.studynest.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.studynest.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserName()
    }

    private fun loadUserName() {
        val user = auth.currentUser

        if (user != null) {
            val userId = user.uid
            Log.d("HomeFragment", "Tentando buscar dados para o ID: $userId")

            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name") ?: "Estudante"
                        Log.d("HomeFragment", "Nome encontrado no banco: $name")
                        val firstName = name.split(" ").firstOrNull() ?: name
                        binding.txtWelcomeTitle.text = "Bem-Vindo, $firstName!"
                    } else {
                        Log.e("HomeFragment", "Documento do usuário não existe no Firestore!")
                        binding.txtWelcomeTitle.text = "Bem-Vindo!"
                        // debug
                        Toast.makeText(context, "Perfil não encontrado no banco.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("HomeFragment", "Erro ao buscar no Firestore: ${exception.message}")
                    binding.txtWelcomeTitle.text = "Bem-Vindo!"
                }
        } else {
            Log.e("HomeFragment", "Usuário não está logado (Auth é null)")
            binding.txtWelcomeTitle.text = "Bem-Vindo!"
        }
    }
}