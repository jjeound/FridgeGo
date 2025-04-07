package com.example.untitled_capstone.domain.use_case.post

data class PostUseCases(
    val addPost: AddPost,
    val getMyPosts: GetMyPosts,
    val getPostById: GetPostById,
    val getLikedPosts: GetLikedPosts,
    val toggleLike: ToggleLikePost,
    val deletePost: DeletePost,
    val modifyPost: ModifyPost,
    val searchPosts: SearchPosts,
    val getNickname: GetNickname,
    val uploadPostImages: UploadPostImages,
    val deletePostImage: DeletePostImage
)
