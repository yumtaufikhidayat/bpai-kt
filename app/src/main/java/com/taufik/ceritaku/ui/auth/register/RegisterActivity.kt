package com.taufik.ceritaku.ui.auth.register

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
import com.taufik.ceritaku.databinding.ActivityRegisterBinding
import com.taufik.ceritaku.model.User
import com.taufik.ceritaku.model.UserPreference
import com.taufik.ceritaku.ui.auth.login.LoginActivity
import com.taufik.ceritaku.utils.CommonConstant
import com.taufik.ceritaku.utils.CommonConstant.DURATION
import com.taufik.ceritaku.utils.CommonConstant.VALUES
import com.taufik.ceritaku.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var registerLocalViewModel: RegisterLocalViewModel
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        playAnimation()
        setupAction()
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

    private fun setupViewModel() {
        registerLocalViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[RegisterLocalViewModel::class.java]
        registerLocalViewModel.getUser().observe(this) {
            user = it
        }

        viewModel.apply {

            isLoading.observe(this@RegisterActivity) {
                showLoading(it)
            }
        }
    }

    private fun playAnimation() = with(binding) {
        ObjectAnimator.ofFloat(imgSignup, View.TRANSLATION_X,
            CommonConstant.LEFT,
            CommonConstant.RIGHT
        ).apply {
            duration = DURATION
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val message = ObjectAnimator.ofFloat(tvMessage, View.ALPHA, VALUES).setDuration(CommonConstant.DURATION_ALT)

        val nameTitle = ObjectAnimator.ofFloat(tvName, View.ALPHA, VALUES).setDuration(CommonConstant.DURATION_ALT)
        val nameInput = ObjectAnimator.ofFloat(etName, View.ALPHA, VALUES).setDuration(CommonConstant.DURATION_ALT)

        val emailTitle = ObjectAnimator.ofFloat(tvEmail, View.ALPHA, VALUES).setDuration(CommonConstant.DURATION_ALT)
        val emailInput = ObjectAnimator.ofFloat(etEmail, View.ALPHA, VALUES).setDuration(CommonConstant.DURATION_ALT)

        val passwordTitle = ObjectAnimator.ofFloat(tvPassword, View.ALPHA, VALUES).setDuration(CommonConstant.DURATION_ALT)
        val passwordInput = ObjectAnimator.ofFloat(etPassword, View.ALPHA, VALUES).setDuration(CommonConstant.DURATION_ALT)

        val signupButton = ObjectAnimator.ofFloat(btnSignup, View.ALPHA, VALUES).setDuration(CommonConstant.DURATION_ALT)
        val showPassword = ObjectAnimator.ofFloat(cbShowPassword, View.ALPHA, VALUES).setDuration(CommonConstant.DURATION_ALT)

        val haveAccount = ObjectAnimator.ofFloat(tvHaveAccount, View.ALPHA, VALUES).setDuration(CommonConstant.DURATION_ALT)

        AnimatorSet().apply {
            playSequentially(
                message,
                nameTitle, nameInput,
                emailTitle, emailInput,
                passwordTitle, passwordInput,
                showPassword, signupButton,
                haveAccount
            )
            start()
        }
    }

    private fun setupAction() = with(binding) {
        btnSignup.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            viewModel.apply {
                registerUser(name, email, password)
                registerResponse.observe(this@RegisterActivity) {
                    if (!it.error) {
                        registerLocalViewModel.saveUser(User(name, email, password, false))
                        startActivity(
                            Intent(this@RegisterActivity, LoginActivity::class.java),
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity)
                                .toBundle()
                        )
                        finish()
                    } else {
                        showSnackBar(it.message)
                    }
                }
            }
        }

        etName.addTextChangedListener(textWatcher())
        etEmail.addTextChangedListener(textWatcher())
        etPassword.addTextChangedListener(textWatcher())

        cbShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                etPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            }
        }

        tvHaveAccount.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity).toBundle())
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
        val name = etName.text
        val email = etEmail.text
        val password = etPassword.text
        btnSignup.isEnabled =  (name != null && name.toString().isNotEmpty())
                && (email != null && email.toString().isNotEmpty())
                && (password != null && password.toString().isNotEmpty()
                && password.toString().length >= 6)
    }

    private fun showLoading(isShow: Boolean) = with(binding) {
        progressRegister.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showSnackBar(text: String) {
        CustomSnackbar(this).show {
            textColor(ContextCompat.getColor(this@RegisterActivity, R.color.white))
            textTypeface(Typeface.DEFAULT_BOLD)
            backgroundColor(ContextCompat.getColor(this@RegisterActivity, R.color.purple_500))
            cornerRadius(18F)
            duration(Snackbar.LENGTH_LONG)
            message(text)
        }
    }
}