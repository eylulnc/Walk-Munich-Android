package com.github.eylulnc.walkmunich.core.ui.util

import androidx.annotation.DrawableRes
import com.github.eylulnc.walkmunich.R

object ImageResolver {
    /**
     * Returns the drawable resource for a known imageUrl, or null when no
     * matching asset exists yet. Callers should show [PlaceholderImage] when null.
     *
     * hero_munich is kept here for when real place images are added;
     * simply add a new `"image_name" -> R.drawable.image_name` entry.
     */
    @DrawableRes
    fun resolveDrawable(imageUrl: String?): Int? = when (imageUrl) {
        "hero_munich" -> R.drawable.hero_munich
        else -> R.drawable.hero_munich
        //else -> null // enable after PlaceCardLarge reimplemented

    }
}
