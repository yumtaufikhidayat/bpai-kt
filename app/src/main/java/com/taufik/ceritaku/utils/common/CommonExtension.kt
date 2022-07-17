package com.taufik.ceritaku.utils.common

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.taufik.ceritaku.R

object CommonExtension {
    fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .placeholder(R.color.purple_500)
            .error(R.color.purple_500)
            .into(this)
    }
}