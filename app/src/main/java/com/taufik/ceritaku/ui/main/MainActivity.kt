package com.taufik.ceritaku.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.viewModels
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
import com.taufik.ceritaku.ui.auth.login.data.LoginResult
import com.taufik.ceritaku.ui.welcome.WelcomeActivity
import com.taufik.ceritaku.utils.ViewModelFactory
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var mainLocalViewModel: MainLocalViewModel
    private lateinit var result: LoginResult
    private val viewModel: MainViewModel by viewModels()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupActionBar()
        setupViewModel()
        setupLoginLogout()
        searchStory()
        showListOfStories()
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        mainLocalViewModel = ViewModelProvider(this@MainActivity, ViewModelFactory(UserPreference.getInstance(dataStore)))[MainLocalViewModel::class.java]
        mainLocalViewModel.getToken().observe(this@MainActivity) {
            result = it
        }
    }

    private fun setupLoginLogout() = with(binding) {
        mainLocalViewModel.getUser().observe(this@MainActivity) { user ->
            if (user.isLogin) {
                val name = result.name
                val token = result.token
                tvName.text = name

                viewModel.apply {
                    listStories(token)
                    lisOfStories.observe(this@MainActivity) {
                        Log.i("mainActivity", "showListOfStories: $it")
                    }
                }

                Toast.makeText(this@MainActivity, "${getString(R.string.text_welcome)} ${user.name}", Toast.LENGTH_SHORT).show()
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
                    mainLocalViewModel.logout()
                    startActivity(Intent(this@MainActivity, WelcomeActivity::class.java),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                    )
                }
                show()
            }
        }
    }

    private fun showListOfStories() = with(binding) {
        if (this@MainActivity::result.isInitialized) {
            viewModel.apply {
                val loginResult = result.token
                listStories(loginResult)
                lisOfStories.observe(this@MainActivity) {
                    Log.i("mainActivity", "showListOfStories: $it")
                }
            }
        }
    }

    private fun searchStory() = with(binding) {
        etSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun hideKeyboard() = with(binding) {
        etSearch.clearFocus()
        val imm: InputMethodManager = this@MainActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
    }
}