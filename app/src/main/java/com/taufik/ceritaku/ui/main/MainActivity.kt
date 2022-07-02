package com.taufik.ceritaku.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.taufik.ceritaku.R
import com.taufik.ceritaku.databinding.ActivityMainBinding
import com.taufik.ceritaku.model.UserPreference
import com.taufik.ceritaku.ui.welcome.WelcomeActivity
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
        setupLoginLogout()
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

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this@MainActivity, ViewModelFactory(UserPreference.getInstance(dataStore)))[MainViewModel::class.java]
    }

    private fun setupLoginLogout() = with(binding) {
        viewModel.getUser().observe(this@MainActivity) { user ->
            if (user.isLogin) {
                tvName.text = user.name
                Toast.makeText(this@MainActivity, "Selamat datang, ${user.name}", Toast.LENGTH_SHORT).show()
            }
        }

        imgExitApp.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this@MainActivity)
            dialog.apply {
                setTitle(resources.getString(R.string.action_exit))
                setMessage(resources.getString(R.string.text_exit))
                setCancelable(false)
                setNegativeButton(resources.getString(R.string.action_no)) { _, _ ->
                    show().dismiss()
                }
                setPositiveButton(resources.getString(R.string.action_yes)) { _, _ ->
                    viewModel.logout()
                    startActivity(Intent(this@MainActivity, WelcomeActivity::class.java),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                    )
                }
                show()
            }
        }
    }
}