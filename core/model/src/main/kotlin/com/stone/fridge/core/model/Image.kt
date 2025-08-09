package com.stone.fridge.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Image(
    val imageUrl: String,
    val imageId: Long
) : Parcelable