package com.stone.fridge.data.util

sealed class FridgeFetchType {
    object OrderByCreated : FridgeFetchType() // 내가 쓴 글 조회
    object OrderByDate : FridgeFetchType() // 내가 좋아요한 글 조회
}