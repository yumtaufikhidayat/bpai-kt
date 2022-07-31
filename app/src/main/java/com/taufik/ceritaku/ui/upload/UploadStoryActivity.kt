package com.taufik.ceritaku.ui.upload

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.kishandonga.csbx.CustomSnackbar
import com.taufik.ceritaku.R
import com.taufik.ceritaku.data.CeritakuUserPreference
import com.taufik.ceritaku.data.remote.Result
import com.taufik.ceritaku.data.remote.response.auth.login.LoginResult
import com.taufik.ceritaku.databinding.ActivityUploadStoryBinding
import com.taufik.ceritaku.ui.LocalViewModelFactory
import com.taufik.ceritaku.ui.ViewModelFactory
import com.taufik.ceritaku.ui.main.MainActivity
import com.taufik.ceritaku.ui.main.MainLocalViewModel
import com.taufik.ceritaku.utils.createCustomTempFile
import com.taufik.ceritaku.utils.reduceFileImage
import com.taufik.ceritaku.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadStoryActivity : AppCompatActivity(){

    private val binding by lazy {
        ActivityUploadStoryBinding.inflate(layoutInflater)
    }

    private val factory = ViewModelFactory.getInstance(this)
    private val uploadViewModel: UploadStoryViewModel by viewModels {
        factory
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var mainLocalViewModel: MainLocalViewModel
    private lateinit var currentPhotoPath: String
    private lateinit var loginResult: LoginResult
    private var getFile: File? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        initObserver()
        getFusedLocation()
        getMyLastLocation()
        setAction()
    }

    private fun setupToolbar() {
        supportActionBar?.apply {
            title = getString(R.string.text_add_story)
            setDisplayHomeAsUpEnabled(true)
            elevation = 0F
        }
    }

    private fun initObserver() {
        mainLocalViewModel = ViewModelProvider(this, LocalViewModelFactory(CeritakuUserPreference.getInstance(dataStore)))[MainLocalViewModel::class.java]
        mainLocalViewModel.getUser().observe(this) {
            loginResult = it
        }
    }

    private fun getFusedLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setAction() = with(binding) {
        fabCamera.setOnClickListener { startTakePhoto() }
        fabGallery.setOnClickListener { openGallery() }
        btnUpload.setOnClickListener { uploadImageToServer() }
        etDescription.addTextChangedListener(textWatcher())
        clearDescription()
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

    private fun uploadImageToServer() = with(binding) {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val etDescription = etDescription.text.toString()
            val description = etDescription.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val token = loginResult.token
            uploadViewModel.getMyLocation().observe(this@UploadStoryActivity) { location ->
                uploadViewModel.uploadStory(
                    token,
                    imageMultipart,
                    description,
                    location.latitude.toFloat(),
                    location.longitude.toFloat()
                ).observe(this@UploadStoryActivity) {
                    if (token.isNotEmpty() && it != null) {
                        when (it) {
                            is Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                showLoading(false)
                                showSuccessDialog()
                                showSnackBar(it.data.message)
                            }
                            is Result.Error -> {
                                showLoading(false)
                                showSnackBar(it.error)
                            }
                            is Result.Unauthorized, is Result.ServerError -> showSnackBar(it.toString())
                        }
                    }
                }
            }
        }
    }

    private fun showSuccessDialog() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(resources.getString(R.string.action_upload))
            setMessage(resources.getString(R.string.text_upload_success))
            setCancelable(false)
            setPositiveButton(resources.getString(R.string.action_close)) { _, _ ->
                startActivity(Intent(this@UploadStoryActivity, MainActivity::class.java),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@UploadStoryActivity).toBundle())
                finish()
            }
            show()
        }
    }

    private fun textWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            checkUploadResource()
            showClear()
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    private fun showClear() = with(binding) {
        imgClear.visibility = if (tvDescription.text.toString().isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun clearDescription() = with(binding) {
        imgClear.setOnClickListener {
            if (imgClear.visibility == View.VISIBLE) {
                etDescription.text.clear()
                imgClear.visibility = View.GONE
                btnUpload.isEnabled = false
            }
        }
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
                showSnackBar(it.toString())
            }
        }
    }

    private fun checkUploadResource() = with(binding) {
        btnUpload.isEnabled = (getFile != null && tvDescription.text.toString().isNotEmpty())
        Log.i("Upload", "checkUploadResource: ")
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

    private fun showLoading(isShow: Boolean) = with(binding) {
        progressbarUpload.isVisible = isShow
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    uploadViewModel.setMyLocation(location)
                } else {
                    Log.i(TAG, "last location failed: $location")
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    showSnackBar(getString(R.string.text_permission_not_granted))
                }
            }
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    
    companion object {
        private val TAG = UploadStoryActivity::class.java.simpleName
    }
}