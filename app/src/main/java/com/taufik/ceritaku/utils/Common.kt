package com.taufik.ceritaku.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.taufik.ceritaku.R
import java.util.regex.Pattern

object Common {
    fun isValidEmailId(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .placeholder(R.color.purple_500)
            .into(this)
    }
}