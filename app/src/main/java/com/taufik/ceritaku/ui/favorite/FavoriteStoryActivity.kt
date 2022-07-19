package com.taufik.ceritaku.ui.favorite

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.taufik.ceritaku.R
import com.taufik.ceritaku.databinding.ActivityFavoriteStoryBinding
import com.taufik.ceritaku.ui.ViewModelFactory
import com.taufik.ceritaku.ui.main.MainAdapter

class FavoriteStoryActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFavoriteStoryBinding.inflate(layoutInflater)
    }

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initAdapter()
        initData()
    }

    private fun initToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.action_favorite)
            elevation = 0F
        }
    }

    private fun initAdapter() {
        mainAdapter = MainAdapter()
        binding.apply {
            with(rvFavoriteStory) {
                layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                setHasFixedSize(true)
                adapter = mainAdapter
            }
        }
    }

    private fun initData() = with(binding) {
        val factory = ViewModelFactory.getInstance(this@FavoriteStoryActivity)
        val viewModel: FavoriteViewModel by viewModels {
            factory
        }

        viewModel.getFavoriteStory().observe(this@FavoriteStoryActivity) {
            if (it != null) {
                mainAdapter.setData(it)
                viewError.root.visibility = if (it.isNotEmpty()) View.GONE else View.VISIBLE
                viewError.imgNoFavorite.resources.openRawResource(R.raw.no_data)
                viewError.tvError.text = getString(R.string.text_no_favorite)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}