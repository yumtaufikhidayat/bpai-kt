package com.taufik.ceritaku.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.taufik.ceritaku.databinding.ActivityWelcomeBinding
import com.taufik.ceritaku.ui.login.LoginActivity
import com.taufik.ceritaku.ui.signup.SignupActivity

class WelcomeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityWelcomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        playAnimation()
        setAction()
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
        val left = -30f
        val right = 30f
        val durationWelcome = 6000L
        val durationAlt = 500L
        val values = 1f

        ObjectAnimator.ofFloat(imgWelcome, View.TRANSLATION_X, left, right).apply {
            this.duration = durationWelcome
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val welcome = ObjectAnimator.ofFloat(tvWelcome, View.ALPHA, values).setDuration(durationAlt)
        val desc = ObjectAnimator.ofFloat(tvDescription, View.ALPHA, values).setDuration(durationAlt)
        val signup = ObjectAnimator.ofFloat(btnSignup, View.ALPHA, values).setDuration(durationAlt)
        val login = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, values).setDuration(durationAlt)

        AnimatorSet().apply {
            playSequentially(welcome, desc, signup, login)
            start()
        }
    }

    private fun setAction() = with(binding) {
        btnSignup.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, SignupActivity::class.java))
        }

        btnLogin.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
        }
    }
}