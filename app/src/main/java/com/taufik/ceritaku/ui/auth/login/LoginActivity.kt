package com.taufik.ceritaku.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.kishandonga.csbx.CustomSnackbar
import com.taufik.ceritaku.R
import com.taufik.ceritaku.data.UserPreference
import com.taufik.ceritaku.data.remote.Result
import com.taufik.ceritaku.databinding.ActivityLoginBinding
import com.taufik.ceritaku.ui.LocalViewModelFactory
import com.taufik.ceritaku.ui.ViewModelFactory
import com.taufik.ceritaku.ui.auth.register.RegisterActivity
import com.taufik.ceritaku.ui.main.MainActivity
import com.taufik.ceritaku.utils.common.CommonConstant.DURATION
import com.taufik.ceritaku.utils.common.CommonConstant.DURATION_ALT
import com.taufik.ceritaku.utils.common.CommonConstant.LEFT
import com.taufik.ceritaku.utils.common.CommonConstant.RIGHT
import com.taufik.ceritaku.utils.common.CommonConstant.VALUES

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val factory = ViewModelFactory.getInstance(this@LoginActivity)
    private val loginViewModel: LoginViewModel by viewModels {
        factory
    }

    private lateinit var loginLocalViewModel: LoginLocalViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        initViewModel()
        playAnimation()
        setUpAction()
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

    private fun initViewModel() {
        loginLocalViewModel = ViewModelProvider(this, LocalViewModelFactory(UserPreference.getInstance(dataStore)))[LoginLocalViewModel::class.java]
    }

    private fun setUpAction() = with(binding) {
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            loginViewModel.loginUser(email, password).observe(this@LoginActivity) {
                if (it != null) {
                    when (it) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            loginLocalViewModel.loginUser(it.data.loginResult)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
                            finish()

                            Toast.makeText(this@LoginActivity, "${getString(R.string.text_welcome)} ${it.data.loginResult.name}", Toast.LENGTH_SHORT).show()
                        }
                        is Result.Error -> {
                            showLoading(false)
                            showSnackBar(it.error)
                        }
                    }
                }
            }
        }

        etEmail.addTextChangedListener(textWatcher())
        etPassword.addTextChangedListener(textWatcher())

        cbShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                etPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            }
        }

        tvDoNotHaveAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
            finish()
        }
    }

    private fun textWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            setEnabledButton()
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    private fun setEnabledButton() = with(binding) {
        val email = etEmail.text
        val password = etPassword.text
        btnLogin.isEnabled = (email != null && email.toString().isNotEmpty())
                && (password != null && password.toString().isNotEmpty()
                && password.toString().length >= 6)
    }

    private fun playAnimation() = with(binding) {
        ObjectAnimator.ofFloat(imgLogin, View.TRANSLATION_X, LEFT, RIGHT).apply {
            duration = DURATION
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(tvLogin, View.ALPHA, VALUES).setDuration(DURATION_ALT)
        val message = ObjectAnimator.ofFloat(tvMessage, View.ALPHA, VALUES).setDuration(DURATION_ALT)

        val emailTitle = ObjectAnimator.ofFloat(tvEmail, View.ALPHA, VALUES).setDuration(DURATION_ALT)
        val emailInput = ObjectAnimator.ofFloat(etEmail, View.ALPHA, VALUES).setDuration(DURATION_ALT)

        val passwordTitle = ObjectAnimator.ofFloat(tvPassword, View.ALPHA, VALUES).setDuration(DURATION_ALT)
        val passwordInput = ObjectAnimator.ofFloat(etPassword, View.ALPHA, VALUES).setDuration(DURATION_ALT)

        val loginButton = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, VALUES).setDuration(DURATION_ALT)
        val showPassword = ObjectAnimator.ofFloat(cbShowPassword, View.ALPHA, VALUES).setDuration(DURATION_ALT)
        val doNotHaveAccount = ObjectAnimator.ofFloat(tvDoNotHaveAccount, View.ALPHA, VALUES).setDuration(DURATION_ALT)

        AnimatorSet().apply {
            playSequentially(
                login, message,
                emailTitle, emailInput,
                passwordTitle, passwordInput,
                showPassword, loginButton, doNotHaveAccount
            )
            start()
        }
    }

    private fun showLoading(isShow: Boolean) = with(binding) {
        progressLogin.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showSnackBar(text: String) {
        CustomSnackbar(this).show {
            textColor(ContextCompat.getColor(this@LoginActivity, R.color.white))
            textTypeface(Typeface.DEFAULT_BOLD)
            backgroundColor(ContextCompat.getColor(this@LoginActivity, R.color.purple_500))
            cornerRadius(18F)
            duration(Snackbar.LENGTH_LONG)
            message(text)
        }
    }
}