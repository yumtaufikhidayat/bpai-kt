package com.taufik.ceritaku.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.taufik.ceritaku.databinding.ActivityLoginBinding
import com.taufik.ceritaku.model.User
import com.taufik.ceritaku.model.UserPreference
import com.taufik.ceritaku.ui.signup.SignupActivity
import com.taufik.ceritaku.utils.CommonConstant.DURATION
import com.taufik.ceritaku.utils.CommonConstant.DURATION_ALT
import com.taufik.ceritaku.utils.CommonConstant.LEFT
import com.taufik.ceritaku.utils.CommonConstant.RIGHT
import com.taufik.ceritaku.utils.CommonConstant.VALUES
import com.taufik.ceritaku.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var user: User

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
        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[LoginViewModel::class.java]
        viewModel.getUser().observe(this) {
            this.user = it
        }
    }

    private fun setUpAction() = with(binding) {
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            when {
                email != user.email -> etEmail.error = "Email tidak sesuai"
                password != user.password -> etPassword.error = "Password tidak sesuai"
                else -> {
//                    viewModel.login()
                    Toast.makeText(this@LoginActivity, "Halo ${user.name}", Toast.LENGTH_SHORT).show()
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
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
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
}