package com.taufik.ceritaku.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.taufik.ceritaku.R
import com.taufik.ceritaku.databinding.ActivityMainBinding
import com.taufik.ceritaku.model.UserPreference
import com.taufik.ceritaku.ui.auth.login.data.LoginResult
import com.taufik.ceritaku.ui.upload.UploadStoryActivity
import com.taufik.ceritaku.ui.welcome.WelcomeActivity
import com.taufik.ceritaku.utils.ViewModelFactory


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var mainAdapter: MainAdapter

    private lateinit var mainLocalViewModel: MainLocalViewModel
    private lateinit var result: LoginResult
    private val viewModel: MainViewModel by viewModels()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupActionBar()
        setupView()
        setupViewModel()
        showData()
        logout()
        searchStory()
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    private fun setupView() {
        mainAdapter = MainAdapter()
        binding.apply {
            with(rvStories) {
                layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                setHasFixedSize(true)
                addOnScrollListener(object : RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0 || dy < 0 && fabAddStory.isShown) fabAddStory.hide()
                    }

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) fabAddStory.show()
                        super.onScrollStateChanged(recyclerView, newState)
                    }
                })
                adapter = mainAdapter
            }
        }
    }

    private fun setupViewModel() {
        mainLocalViewModel = ViewModelProvider(this@MainActivity, ViewModelFactory(UserPreference.getInstance(dataStore)))[MainLocalViewModel::class.java]
        mainLocalViewModel.getUser().observe(this@MainActivity) {
            result = it
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showData() = with(binding) {
        mainLocalViewModel.getUser().observe(this@MainActivity) { user ->
            if (user.token.isNotEmpty()) {
                val name = result.name
                val token = result.token
                tvName.text = name

                viewModel.apply {
                    listStories(token)
                    lisOfStories.observe(this@MainActivity) {
                        if (it.isNotEmpty()) {
                            mainAdapter.setData(it)
                        }
                    }
                }

                addStory()
            }
        }
    }

    private fun addStory() = with(binding) {
        fabAddStory.setOnClickListener {
            startActivity(Intent(
                this@MainActivity, UploadStoryActivity::class.java
            ), ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle())
        }
    }

    private fun logout() = with(binding) {
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

    private fun searchStory() = with(binding) {
        etSearch.apply {
            setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    return@OnEditorActionListener true
                }
                false
            })

            addTextChangedListener(afterTextChanged = { p0 ->
                mainAdapter.filter.filter(p0.toString())
            })
        }
    }

    private fun hideKeyboard() = with(binding) {
        etSearch.clearFocus()
        val imm: InputMethodManager = this@MainActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
    }

    private fun showLoading(isShow: Boolean) = with(binding) {
        if (isShow) {
            shimmerMain.visibility = View.VISIBLE
            rvStories.visibility = View.GONE
        } else {
            shimmerMain.visibility = View.GONE
            rvStories.visibility = View.VISIBLE
        }
    }
}