package com.taufik.ceritaku.ui.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.taufik.ceritaku.R
import com.taufik.ceritaku.data.CeritakuUserPreference
import com.taufik.ceritaku.data.remote.Result
import com.taufik.ceritaku.data.remote.response.auth.login.LoginResult
import com.taufik.ceritaku.databinding.ActivityMapsBinding
import com.taufik.ceritaku.ui.LocalViewModelFactory
import com.taufik.ceritaku.ui.ViewModelFactory
import com.taufik.ceritaku.ui.main.MainLocalViewModel
import com.taufik.ceritaku.ui.main.MainViewModel
import java.io.IOException
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy {
        ActivityMapsBinding.inflate(layoutInflater)
    }

    private lateinit var mMap: GoogleMap
    private lateinit var result: LoginResult
    private lateinit var mainLocalViewModel: MainLocalViewModel
    private lateinit var latLng: LatLng
    private val boundsBuilder = LatLngBounds.Builder()

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initToolbar()
        setupViewModel()
    }

    private fun initToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.text_maps)
            elevation = 0f
        }
    }

    private fun setupViewModel() {
        mainLocalViewModel = ViewModelProvider(this, LocalViewModelFactory(CeritakuUserPreference.getInstance(dataStore)))[MainLocalViewModel::class.java]
        mainLocalViewModel.getUser().observe(this) {
            result = it
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = false
            isMapToolbarEnabled = true
            isScrollGesturesEnabledDuringRotateOrZoom = false
            isRotateGesturesEnabled = false
        }

        getMyLocation()
        setLocationMarkers()
    }

    private fun setLocationMarkers() {
        mainLocalViewModel.getUser().observe(this) { user ->
            if (user.token.isNotEmpty()) {
                val token = result.token

                val factory = ViewModelFactory.getInstance(this)
                val mainViewModel: MainViewModel by viewModels {
                    factory
                }

                mainViewModel.getLocation(token).observe(this) {
                    if (it != null) {
                        when (it) {
                            is Result.Loading -> {}
                            is Result.Success -> {
                                it.data.forEach { story ->
                                    val lat = story.lat
                                    val lon = story.lon

                                    latLng = LatLng(lat, lon)
                                    val addressName = getAddressName(lat, lon)

                                    mMap.addMarker(MarkerOptions()
                                        .position(latLng)
                                        .title(story.name)
                                        .icon(vectorToBitmap(R.drawable.ic_outline_person_pin, Color.parseColor("#FF0000")))
                                    )?.snippet = addressName

                                    boundsBuilder.include(latLng)
                                }

                                showCustomInfoWindow()
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    latLng,
                                    ZOOM_LEVEL
                                ))
                            }
                            is Result.Error -> {}
                            is Result.Unauthorized -> {}
                            is Result.ServerError -> {}
                        }
                    }
                }
            }
        }
    }

    private fun getAddressName(latitude: Double, longitude: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(latitude, longitude, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
                Log.d(TAG, "getAddressName: $addressName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return addressName
    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun showCustomInfoWindow() {
        mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {

            private val view: View = layoutInflater.inflate(R.layout.item_list_stories, null)

            private fun windowInfo(marker: Marker, view: View) {
                val imgStory = view.findViewById<ImageView>(R.id.imgStory)
                val tvName = view.findViewById<TextView>(R.id.tvName)
                val tvCreatedAt = view.findViewById<TextView>(R.id.tvCreatedAt)
                val tvDescription = view.findViewById<TextView>(R.id.tvDescription)

                imgStory.visibility = View.GONE
                tvName.text = marker.title
                tvCreatedAt.visibility = View.GONE
                tvDescription.text = StringBuilder(getString(R.string.text_location)).append("\n").append(marker.snippet)
            }

            override fun getInfoContents(p0: Marker): View {
                windowInfo(p0, view)
                return view
            }

            override fun getInfoWindow(p0: Marker): View {
                windowInfo(p0, view)
                return view
            }
        })
    }

    private fun getMyLocation() {
        if(ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_maps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.action_normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }

            R.id.action_satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }

            R.id.action_terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }

            R.id.action_hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val TAG = "MapActivity"
        private const val ZOOM_LEVEL = 5f
    }
}