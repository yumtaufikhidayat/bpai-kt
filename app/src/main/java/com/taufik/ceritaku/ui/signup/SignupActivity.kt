package com.taufik.ceritaku.ui.signup

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.taufik.ceritaku.databinding.ActivitySignupBinding
import com.taufik.ceritaku.model.User
import com.taufik.ceritaku.model.UserPreference
import com.taufik.ceritaku.ui.login.LoginActivity
import com.taufik.ceritaku.utils.CommonConstant
import com.taufik.ceritaku.utils.CommonConstant.DURATION
import com.taufik.ceritaku.utils.CommonConstant.VALUES
import com.taufik.ceritaku.utils.ViewModelFactory

class SignupActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: SignupViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpView()
        setUpViewModel()
        playAnimation()
        setUpAction()
    }

    private fun setUpView() {
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

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[SignupViewModel::class.java]
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

    private fun setUpAction() = with(binding) {
        btnSignup.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            viewModel.saveUser(User(name, email, password, false))
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this@SignupActivity).toBundle())
            finish()
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
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this@SignupActivity).toBundle())
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
}