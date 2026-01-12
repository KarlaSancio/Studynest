package com.example.studynest.ui

import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.studynest.R


class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            findNavController()
                .navigate(R.id.action_splash_to_login)
        }, 2000)
    }
}
