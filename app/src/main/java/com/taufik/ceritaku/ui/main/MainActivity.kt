package com.taufik.ceritaku.ui.main

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.taufik.ceritaku.databinding.ActivityMainBinding
import com.taufik.ceritaku.model.UserPreference
import com.taufik.ceritaku.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: MainViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupViewModel()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() = with(binding) {
        viewModel = ViewModelProvider(this@MainActivity, ViewModelFactory(UserPreference.getInstance(dataStore)))[MainViewModel::class.java]
        viewModel.getUser().observe(this@MainActivity) { user ->
            if (user.isLogin) {
                Toast.makeText(this@MainActivity, "Halo, ${user.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}