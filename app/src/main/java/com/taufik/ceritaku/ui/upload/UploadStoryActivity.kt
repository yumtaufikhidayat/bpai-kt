package com.taufik.ceritaku.ui.upload

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.taufik.ceritaku.R
import com.taufik.ceritaku.databinding.ActivityUploadStoryBinding

class UploadStoryActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityUploadStoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        checkUploadResource()
    }

    private fun setupToolbar() {
        supportActionBar?.apply {
            title = getString(R.string.text_add_story)
            setDisplayHomeAsUpEnabled(true)
            elevation = 0F
        }
    }

    private fun checkUploadResource() = with(binding) {
        btnUpload.isEnabled = imgUploadedImage.resources != null && tvDescription.text.isNotEmpty()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}