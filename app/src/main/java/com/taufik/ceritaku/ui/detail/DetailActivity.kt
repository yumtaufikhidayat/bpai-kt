package com.taufik.ceritaku.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.taufik.ceritaku.R
import com.taufik.ceritaku.databinding.ActivityDetailBinding
import com.taufik.ceritaku.ui.main.data.ListStoryItem
import com.taufik.ceritaku.utils.common.CommonConstant
import com.taufik.ceritaku.utils.common.CommonExtension.formatDate
import com.taufik.ceritaku.utils.common.CommonExtension.loadImage
import com.taufik.ceritaku.utils.common.CommonExtension.parseDate

class DetailActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private lateinit var listStoryItem: ListStoryItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getParcelableData()
        setupToolbar()
        setData()
    }

    private fun getParcelableData() {
        listStoryItem = intent.getParcelableExtra<ListStoryItem>(EXTRA_DATA) as ListStoryItem
    }

    private fun setupToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = 0F
        }
    }

    private fun setData() = with(binding) {
        imgDetailStory.loadImage(listStoryItem.photoUrl)

        val date = listStoryItem.createdAt
        val dateParse = date.parseDate(CommonConstant.DATE_YYYY_MM_DD_FORMAT)
        val dateFormat = dateParse.formatDate(CommonConstant.DATE_DD_MMMM_YYYY_FORMAT)
        tvDetailDate.text = String.format("%s %s", getString(R.string.text_uploaded_on), dateFormat)

        tvDetailName.text = listStoryItem.name
        tvDetailDescription.text = listStoryItem.description
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_DATA = "com.taufik.ceritaku.ui.detail.EXTRA_DATA"
    }
}