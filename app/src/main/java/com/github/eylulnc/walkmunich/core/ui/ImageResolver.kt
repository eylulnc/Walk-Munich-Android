package com.github.eylulnc.walkmunich.core.ui

import androidx.annotation.DrawableRes
import com.github.eylulnc.walkmunich.R

object ImageResolver {
    @DrawableRes
    fun resolveDrawable(imageUrl: String): Int {
        return when (imageUrl) {
            "hero_munich" -> R.drawable.hero_munich
            else -> R.drawable.hero_munich //TODO :: add fallback placeholder image
        }
    }
}