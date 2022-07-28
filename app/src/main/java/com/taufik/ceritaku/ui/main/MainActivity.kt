package com.taufik.ceritaku.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.kishandonga.csbx.CustomSnackbar
import com.taufik.ceritaku.R
import com.taufik.ceritaku.data.CeritakuUserPreference
import com.taufik.ceritaku.data.remote.Result
import com.taufik.ceritaku.data.remote.response.auth.login.LoginResult
import com.taufik.ceritaku.databinding.ActivityMainBinding
import com.taufik.ceritaku.ui.LocalViewModelFactory
import com.taufik.ceritaku.ui.ViewModelFactory
import com.taufik.ceritaku.ui.favorite.FavoriteStoryActivity
import com.taufik.ceritaku.ui.maps.MapsActivity
import com.taufik.ceritaku.ui.upload.UploadStoryActivity
import com.taufik.ceritaku.ui.welcome.WelcomeActivity


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var listAdapter: CeritakuListAdapter
    private lateinit var result: LoginResult
    private lateinit var mainLocalViewModel: MainLocalViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupActionBar()
        setupViewModel()
        setupView()
        favoriteStory()
        logout()
    }

    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        mainLocalViewModel = ViewModelProvider(this, LocalViewModelFactory(CeritakuUserPreference.getInstance(dataStore)))[MainLocalViewModel::class.java]
        mainLocalViewModel.getUser().observe(this) {
            result = it
        }
    }

    private fun setupView() {
        listAdapter = CeritakuListAdapter()
        showLoading(true)
        binding.apply {
            with(rvStories) {
                layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                setHasFixedSize(true)
                addOnScrollListener(object : RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0 || dy < 0 && fabAddStory.isShown) fabAddStory.hide()
                        if (dy > 0 || dy < 0 && fabMaps.isShown) fabMaps.hide()
                    }

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) fabAddStory.show()
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) fabMaps.show()
                        super.onScrollStateChanged(recyclerView, newState)
                    }
                })

                adapter = listAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        listAdapter.retry()
                    }
                )

                mainLocalViewModel.getUser().observe(this@MainActivity) { user ->
                    if (user.token.isNotEmpty()) {
                        val name = result.name
                        val token = result.token
                        tvName.text = name

                        val factory = ViewModelFactory.getInstance(this@MainActivity)
                        val mainViewModel: MainViewModel by viewModels {
                            factory
                        }

                        listAdapter.addLoadStateListener {
                            mainViewModel.dataResult(it).observe(this@MainActivity) { state ->
                                when (state) {
                                    is Result.Loading -> showLoading(true)
                                    is Result.Success -> showLoading(false)
                                    is Result.Error -> showLoading(false)
                                    is Result.Unauthorized -> showSnackBar(state.error)
                                    is Result.ServerError -> showSnackBar(state.error)
                                }
                            }
                        }
                        mainViewModel.getListOfStories(token).observe(this@MainActivity) {
                            if (it != null) {
                                showLoading(false)
                                listAdapter.submitData(lifecycle, it)
                            }
                        }

                        addStory()
                        showMaps()
                    }
                }
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

    private fun showMaps() = with(binding) {
        fabMaps.setOnClickListener {
            startActivity(Intent(
                this@MainActivity, MapsActivity::class.java
            ), ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle())
        }
    }

    private fun favoriteStory() = with(binding) {
        imgFavorite.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavoriteStoryActivity::class.java),
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle()
            )
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
                    mainLocalViewModel.logoutUser()
                    startActivity(Intent(this@MainActivity, WelcomeActivity::class.java),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                    )
                }
                show()
            }
        }
    }

    private fun showSnackBar(text: String) {
        CustomSnackbar(this).show {
            textColor(ContextCompat.getColor(this@MainActivity, R.color.white))
            textTypeface(Typeface.DEFAULT_BOLD)
            backgroundColor(ContextCompat.getColor(this@MainActivity, R.color.purple_500))
            cornerRadius(18F)
            duration(Snackbar.LENGTH_LONG)
            message(text)
        }
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