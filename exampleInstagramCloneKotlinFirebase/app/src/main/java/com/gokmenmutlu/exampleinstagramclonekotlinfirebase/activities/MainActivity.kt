package com.gokmenmutlu.exampleinstagramclonekotlinfirebase.activities

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.R
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.databinding.ActivityMainBinding
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.viewModels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.setSelectedImage(it) // ViewModel'e resmi kaydet
            navController.navigate(R.id.uploadFragment) // UploadFragment'e geçiş yap
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        bottomNavigationBar()
    }

    private fun bottomNavigationBar() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.homepageFragment)
                    true
                }

                R.id.nav_profile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.nav_upload -> {
                    getContent.launch("image/*")
                    true
                }
                else -> false
            }
        }
    }

}