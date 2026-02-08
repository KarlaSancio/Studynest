package com.example.studynest.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.studynest.R
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            val firebaseAuth = FirebaseAuth.getInstance()
            
            if (firebaseAuth.currentUser != null) {
                // Caso o Usuário esteja logado, navega para a Home
                findNavController().navigate(R.id.action_splash_to_home)
            } else {
                // Caso o Usuário não esteja logado, navega para o Login
                findNavController().navigate(R.id.action_splash_to_login)
            }
        }, 2000) // 2 segundos de delay
    }
}
