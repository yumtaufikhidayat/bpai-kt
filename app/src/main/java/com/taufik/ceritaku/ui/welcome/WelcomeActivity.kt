package com.taufik.ceritaku.ui.welcome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.taufik.ceritaku.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityWelcomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}