package com.stone.fridge.core.test.data

import com.stone.fridge.core.model.Post
import com.stone.fridge.core.model.PostRaw

val postRawsData: List<PostRaw> = listOf(
    PostRaw(
        id = 1L,
        title = "Sample Post",
        price = 10000,
        neighborhood = "남구",
        timeAgo = "2시간 전",
        currentParticipants = 2,
        memberCount = 5,
        liked = false,
        likeCount = 3,
        roomActive = true,
        district = "무거동",
        imageUrls = emptyList()
    )
)