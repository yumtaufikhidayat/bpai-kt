package com.taufik.ceritaku.ui.splashscreen

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.postDelayed
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.taufik.ceritaku.databinding.ActivitySplashScreenBinding
import com.taufik.ceritaku.model.UserPreference
import com.taufik.ceritaku.ui.main.MainActivity
import com.taufik.ceritaku.ui.main.MainViewModel
import com.taufik.ceritaku.ui.welcome.WelcomeActivity
import com.taufik.ceritaku.utils.ViewModelFactory

class SplashScreenActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: MainViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        playAnimation()
        setAppVersion()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() = with(binding) {
        val translationY = -1600f
        val translationYAlt = 1400f
        val duration = 1000L
        val delay = 4000L

        imgBackground.animate().translationY(translationY).setDuration(duration).startDelay = delay
        imgLogo.animate().translationY(translationYAlt).setDuration(duration).startDelay = delay
        tvAppName.animate().translationY(translationYAlt).setDuration(duration).startDelay = delay
        tvAppVersion.animate().translationY(translationYAlt).setDuration(duration).startDelay = delay

        setSplashscreen()
    }

    private fun setSplashscreen() {
        viewModel = ViewModelProvider(this@SplashScreenActivity, ViewModelFactory(UserPreference.getInstance(dataStore)))[MainViewModel::class.java]

        val delayMillis = 6000L
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(delayMillis) {
            viewModel.getUser().observe(this@SplashScreenActivity) { user ->
                if (user.isLogin) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
                    finish()
                } else {
                    startActivity(Intent(this@SplashScreenActivity, WelcomeActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this@SplashScreenActivity).toBundle())
                    finish()
                }
            }
        }
    }

    private fun setAppVersion() {
        binding.apply {
            try {
                val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
                val appVersion = pInfo.versionName
                tvAppVersion.text = appVersion
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
    }
}