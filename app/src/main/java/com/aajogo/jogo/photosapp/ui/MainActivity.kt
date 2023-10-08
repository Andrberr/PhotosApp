package com.aajogo.jogo.photosapp.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aajogo.jogo.photosapp.R
import com.aajogo.jogo.photosapp.databinding.ActivityMainBinding
import com.aajogo.jogo.photosapp.ui.sign.SignViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private val viewModel by viewModels<SignViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.navHostFragment)
        setMenu()
        setMenuVisibility(false)
    }

    private fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun setupActionBar(navController: NavController, appBarConfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupNavigationMenu(navController: NavController) {
        binding.navMenu.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    private fun setupClickListener() {
        binding.navMenu.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = !menuItem.isChecked
            when (menuItem.itemId) {
                R.id.nav_photos -> {
                    if (navController.currentDestination?.id == R.id.mapFragment) {
                        navController.navigate(R.id.action_mapFragment_to_photosFragment)
                    }
                }
                R.id.nav_map -> {
                    if (navController.currentDestination?.id == R.id.photosFragment) {
                        navController.navigate(R.id.action_photosFragment_to_mapFragment)
                    }
                }
            }
            closeDrawer()
            true
        }
    }

    private fun setMenu() {
        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.photosFragment, R.id.mapFragment),
            binding.drawerLayout
        )

        setupActionBar(navController, appBarConfiguration)
        setupNavigationMenu(navController)

        setupClickListener()
        viewModel.setMenuInitialized(true)
    }

    fun setMenuVisibility(isVisible: Boolean) {
        with(binding) {
            toolbar.isVisible = isVisible
            navMenu.isVisible = isVisible
        }
    }

    fun setHeader(header: String) {
        val headerView = binding.navMenu.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.usernameView).text = header
    }

    fun setBarTitle(title: String) {
        binding.toolbar.title = title
    }

    override fun onDestroy() {
        viewModel.setMenuInitialized(false)
        super.onDestroy()
    }
}