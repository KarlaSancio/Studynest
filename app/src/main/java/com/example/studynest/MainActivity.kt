package com.example.studynest

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.studynest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 1. Controle de Visibilidade da Navbar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isAuthScreen = destination.id == R.id.splashFragment ||
                    destination.id == R.id.loginFragment ||
                    destination.id == R.id.registerFragment

            binding.bottomNavContainer.visibility = if (isAuthScreen) View.GONE else View.VISIBLE

            // Atualiza as cores dos ícones conforme a tela atual
            updateNavColors(destination.id)
        }

        // 2. Configuração dos Cliques
        binding.btnNavHome.setOnClickListener { navigateTo(R.id.homeFragment) }
        binding.btnNavAdd.setOnClickListener { navigateTo(R.id.addDisciplineFragment) }
        binding.btnNavSearch.setOnClickListener { navigateTo(R.id.searchDisciplineFragment) }
        binding.btnNavFavorites.setOnClickListener { navigateTo(R.id.favoritesFragment) }
    }

    private fun navigateTo(destinationId: Int) {
        if (navController.currentDestination?.id != destinationId) {
            navController.navigate(destinationId)
        }
    }

    // Função auxiliar para pintar de roxo o ícone ativo e de preto os inativos
    private fun updateNavColors(currentId: Int) {
        val purple = ContextCompat.getColor(this, R.color.studynest_purple)
        val black = ContextCompat.getColor(this, android.R.color.black)

        // Resetar tudo para preto
        setBtnColor(binding.btnNavHome, black)
        setBtnColor(binding.btnNavAdd, black)
        setBtnColor(binding.btnNavSearch, black)
        setBtnColor(binding.btnNavFavorites, black)

        // Pintar o ativo de roxo
        when (currentId) {
            R.id.homeFragment -> setBtnColor(binding.btnNavHome, purple)
            R.id.addDisciplineFragment -> setBtnColor(binding.btnNavAdd, purple)
            R.id.searchDisciplineFragment -> setBtnColor(binding.btnNavSearch, purple)
            R.id.favoritesFragment -> setBtnColor(binding.btnNavFavorites, purple)
        }
    }

    private fun setBtnColor(view: View, color: Int) {
        val layout = view as android.widget.LinearLayout
        val img = layout.getChildAt(0) as ImageView
        val txt = layout.getChildAt(1) as TextView
        img.setColorFilter(color)
        txt.setTextColor(color)
    }
}