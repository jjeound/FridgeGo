package com.example.untitled_capstone.data.util

sealed class PostFetchType {
    object MyPosts : PostFetchType() // 내가 쓴 글 조회
    object LikedPosts : PostFetchType() // 내가 좋아요한 글 조회
    data class Search(val keyword: String?) : PostFetchType() // 검색한 글 조회
}