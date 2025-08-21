package com.stone.fridge.core.test.data

import com.stone.fridge.core.model.Post

val postData: Post = Post(
    id = 1L,
    title = "Sample Post",
    price = 10000,
    neighborhood = "남구",
    timeAgo = "2시간 전",
    currentParticipants = 2,
    memberCount = 5,
    liked = false,
    roomActive = true,
    chatRoomId = 1L,
    nickname = "User",
    category = "VEGETABLE",
    image = null,
    profileImageUrl = null,
    content = "This is a sample post content.",
    createdAt = null,
    district = "무거동",
    likeCount = 3
)