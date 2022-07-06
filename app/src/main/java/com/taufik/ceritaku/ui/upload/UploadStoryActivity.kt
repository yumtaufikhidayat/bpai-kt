package com.taufik.ceritaku.ui.upload

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.kishandonga.csbx.CustomSnackbar
import com.taufik.ceritaku.R
import com.taufik.ceritaku.databinding.ActivityUploadStoryBinding
import com.taufik.ceritaku.utils.createCustomTempFile
import com.taufik.ceritaku.utils.uriToFile
import java.io.File

class UploadStoryActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityUploadStoryBinding.inflate(layoutInflater)
    }

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setAction()
        checkUploadResource()
    }

    private fun setupToolbar() {
        supportActionBar?.apply {
            title = getString(R.string.text_add_story)
            setDisplayHomeAsUpEnabled(true)
            elevation = 0F
        }
    }

    private fun setAction() = with(binding) {
        fabCamera.setOnClickListener { startTakePhoto() }
        fabGallery.setOnClickListener { openGallery() }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.taufik.ceritaku.ui",
                it
            )

            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launchIntentCamera.launch(intent)
        }
    }

    private fun openGallery() {
        val intent = Intent().apply {
            action = ACTION_GET_CONTENT
            type = "image/*"
        }

        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launchIntentGallery.launch(chooser)
    }

    private val launchIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        with(binding) {
            if (it.resultCode == RESULT_OK) {
                val myFile = File(currentPhotoPath)
                getFile = myFile

                val result = BitmapFactory.decodeFile(getFile?.path)
                imgUploadedImage.setImageBitmap(result)
            } else {
                showSnackBar(it.toString())
            }
        }
    }

    private val launchIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        with(binding) {
            if (it.resultCode == RESULT_OK) {
                val selectedImage: Uri = it.data?.data as Uri
                val myFile = uriToFile(selectedImage, this@UploadStoryActivity)
                getFile = myFile
                imgUploadedImage.setImageURI(selectedImage)
            } else {
                it.toString()
            }
        }
    }

    private fun checkUploadResource() = with(binding) {
        btnUpload.isEnabled = (imgUploadedImage.resources != null
                || imgUploadedImage.drawable != null)
                && tvDescription.text.isNotEmpty()
    }

    private fun showSnackBar(text: String) {
        CustomSnackbar(this).show {
            textColor(ContextCompat.getColor(this@UploadStoryActivity, R.color.white))
            textTypeface(Typeface.DEFAULT_BOLD)
            backgroundColor(ContextCompat.getColor(this@UploadStoryActivity, R.color.purple_500))
            cornerRadius(18F)
            duration(Snackbar.LENGTH_LONG)
            message(text)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}