package com.taufik.ceritaku.utils.common

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.taufik.ceritaku.R
import java.text.SimpleDateFormat
import java.util.*

object CommonExtension {
    fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .placeholder(R.color.purple_500)
            .error(R.color.purple_500)
            .into(this)
    }

    fun String.parseDate(format: String): Date {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.parse(this) ?: Date()
    }

    fun Date.formatDate(format: String): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(this)
    }
}