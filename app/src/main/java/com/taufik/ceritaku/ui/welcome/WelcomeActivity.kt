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
import androidx.core.app.ActivityOptionsCompat
import com.taufik.ceritaku.utils.CommonConstant.DURATION_ALT
import com.taufik.ceritaku.utils.CommonConstant.DURATION
import com.taufik.ceritaku.utils.CommonConstant.LEFT
import com.taufik.ceritaku.utils.CommonConstant.RIGHT
import com.taufik.ceritaku.utils.CommonConstant.VALUES
import com.taufik.ceritaku.databinding.ActivityWelcomeBinding
import com.taufik.ceritaku.ui.auth.login.LoginActivity
import com.taufik.ceritaku.ui.auth.signup.SignupActivity

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
        ObjectAnimator.ofFloat(imgWelcome, View.TRANSLATION_X, LEFT, RIGHT).apply {
            duration = DURATION
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val welcome = ObjectAnimator.ofFloat(tvWelcome, View.ALPHA, VALUES).setDuration(DURATION_ALT)
        val desc = ObjectAnimator.ofFloat(tvDescription, View.ALPHA, VALUES).setDuration(DURATION_ALT)
        val signup = ObjectAnimator.ofFloat(btnSignup, View.ALPHA, VALUES).setDuration(DURATION_ALT)
        val login = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, VALUES).setDuration(DURATION_ALT)

        AnimatorSet().apply {
            playSequentially(welcome, desc, signup, login)
            start()
        }
    }

    private fun setAction() = with(binding) {
        btnSignup.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, SignupActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this@WelcomeActivity).toBundle())
            finish()
        }

        btnLogin.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this@WelcomeActivity).toBundle())
            finish()
        }
    }
}