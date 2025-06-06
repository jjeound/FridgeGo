package com.stone.fridge.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostImage(
    val imageUrl: String,
    val id: Long
): Parcelable
