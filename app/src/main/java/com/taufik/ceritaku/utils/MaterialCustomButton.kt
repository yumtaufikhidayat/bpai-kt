package com.taufik.ceritaku.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.taufik.ceritaku.R

class MaterialCustomButton: MaterialButton {

    private lateinit var enableBackground: Drawable
    private lateinit var disableBackground: Drawable

    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        background = if (isEnabled) enableBackground else disableBackground

        setTextColor(txtColor)
        gravity = Gravity.CENTER

        text = when (id) {
            R.id.btnLogin -> this.context.getString(R.string.action_login)
            R.id.btnSignup -> this.context.getString(R.string.action_signup)
            else -> ""
        }
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enableBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_enabled) as Drawable
        disableBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disabled) as Drawable
    }
}