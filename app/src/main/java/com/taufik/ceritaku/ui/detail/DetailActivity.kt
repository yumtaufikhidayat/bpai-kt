package com.taufik.ceritaku.ui.detail

import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kishandonga.csbx.CustomSnackbar
import com.taufik.ceritaku.R
import com.taufik.ceritaku.data.local.entity.StoryEntity
import com.taufik.ceritaku.databinding.ActivityDetailBinding
import com.taufik.ceritaku.ui.ViewModelFactory
import com.taufik.ceritaku.utils.common.Common
import com.taufik.ceritaku.utils.common.CommonExtension.loadImage
import java.util.*

class DetailActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private lateinit var storyEntity: StoryEntity
    private val factory = ViewModelFactory.getInstance(this)
    private val detailViewModel: DetailViewModel by viewModels {
        factory
    }
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getParcelableData()
        setupToolbar()
        setData()
    }

    private fun getParcelableData() {
        storyEntity = intent.getParcelableExtra<StoryEntity>(EXTRA_DATA) as StoryEntity
    }

    private fun setupToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = 0F
        }
    }

    private fun setData() = with(binding) {
        storyEntity.apply {
            imgDetailStory.loadImage(photoUrl)

            val date = createdAt
            tvDetailDate.text = String.format(
                "%s %s",
                getString(R.string.text_uploaded_on),
                Common.formattedDate(date, TimeZone.getDefault().id)
            )

            tvDetailName.text = name
            tvDetailDescription.text = description
        }
        detailViewModel.setStoryData(storyEntity)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        this.menu = menu
        detailViewModel.favoriteStatus.observe(this) {
            setFavoriteState(it)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_favorite -> {
                detailViewModel.changeFavorite(storyEntity)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setFavoriteState(state: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.action_favorite)
        menuItem?.icon = if (state) {
            ContextCompat.getDrawable(this, R.drawable.ic_favorite_enable)
        } else {
            ContextCompat.getDrawable(this, R.drawable.ic_favorite_disable)
        }
    }

    private fun showSnackBar(text: String) {
        CustomSnackbar(this).show {
            textColor(ContextCompat.getColor(this@DetailActivity, R.color.white))
            textTypeface(Typeface.DEFAULT_BOLD)
            backgroundColor(ContextCompat.getColor(this@DetailActivity, R.color.purple_500))
            cornerRadius(18F)
            duration(Snackbar.LENGTH_LONG)
            message(text)
        }
    }

    companion object {
        const val EXTRA_DATA = "com.taufik.ceritaku.ui.detail.EXTRA_DATA"
    }
}