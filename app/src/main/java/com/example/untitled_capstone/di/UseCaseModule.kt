package com.example.untitled_capstone.di

import com.example.untitled_capstone.domain.repository.ChatRepository
import com.example.untitled_capstone.domain.repository.FridgeRepository
import com.example.untitled_capstone.domain.repository.HomeRepository
import com.example.untitled_capstone.domain.repository.LocalUserManger
import com.example.untitled_capstone.domain.repository.LoginRepository
import com.example.untitled_capstone.domain.repository.MyRepository
import com.example.untitled_capstone.domain.repository.PostRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.example.untitled_capstone.domain.use_case.app_entry.ReadAppEntry
import com.example.untitled_capstone.domain.use_case.app_entry.SaveAppEntry
import com.example.untitled_capstone.domain.use_case.chat.ChatCheckWhoIsIn
import com.example.untitled_capstone.domain.use_case.chat.ChatCreateRoom
import com.example.untitled_capstone.domain.use_case.chat.ChatGetMessages
import com.example.untitled_capstone.domain.use_case.chat.ChatGetMyRooms
import com.example.untitled_capstone.domain.use_case.chat.ChatRead
import com.example.untitled_capstone.domain.use_case.chat.ChatRoomClose
import com.example.untitled_capstone.domain.use_case.chat.ChatRoomEnter
import com.example.untitled_capstone.domain.use_case.chat.ChatRoomExit
import com.example.untitled_capstone.domain.use_case.chat.ChatRoomJoin
import com.example.untitled_capstone.domain.use_case.chat.ChatUseCases
import com.example.untitled_capstone.domain.use_case.fridge.AddFridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.DeleteFridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.FridgeUseCases
import com.example.untitled_capstone.domain.use_case.fridge.GetFridgeItemById
import com.example.untitled_capstone.domain.use_case.fridge.GetFridgeItems
import com.example.untitled_capstone.domain.use_case.fridge.GetFridgeItemsByDate
import com.example.untitled_capstone.domain.use_case.fridge.ModifyFridgeItems
import com.example.untitled_capstone.domain.use_case.fridge.ToggleNotification
import com.example.untitled_capstone.domain.use_case.home.AddRecipe
import com.example.untitled_capstone.domain.use_case.home.DeleteRecipe
import com.example.untitled_capstone.domain.use_case.home.GetAnotherRecommendation
import com.example.untitled_capstone.domain.use_case.home.GetFirstRecommendation
import com.example.untitled_capstone.domain.use_case.home.GetIsFirstSelection
import com.example.untitled_capstone.domain.use_case.home.GetRecipeById
import com.example.untitled_capstone.domain.use_case.home.GetRecipeItems
import com.example.untitled_capstone.domain.use_case.home.GetTastePreference
import com.example.untitled_capstone.domain.use_case.home.HomeUseCases
import com.example.untitled_capstone.domain.use_case.home.ModifyRecipe
import com.example.untitled_capstone.domain.use_case.home.SetIsFirstSelection
import com.example.untitled_capstone.domain.use_case.home.SetTastePreference
import com.example.untitled_capstone.domain.use_case.home.ToggleLike
import com.example.untitled_capstone.domain.use_case.home.UploadRecipeImage
import com.example.untitled_capstone.domain.use_case.login.GetAddressByCoord
import com.example.untitled_capstone.domain.use_case.my.GetAccessToken
import com.example.untitled_capstone.domain.use_case.my.GetMyProfile
import com.example.untitled_capstone.domain.use_case.my.GetOtherProfile
import com.example.untitled_capstone.domain.use_case.login.KakaoLogin
import com.example.untitled_capstone.domain.use_case.login.LoginUseCases
import com.example.untitled_capstone.domain.use_case.login.ModifyNickname
import com.example.untitled_capstone.domain.use_case.login.SetLocation
import com.example.untitled_capstone.domain.use_case.my.Logout
import com.example.untitled_capstone.domain.use_case.login.SetNickname
import com.example.untitled_capstone.domain.use_case.my.MyUseCases
import com.example.untitled_capstone.domain.use_case.my.UploadProfileImage
import com.example.untitled_capstone.domain.use_case.post.AddPost
import com.example.untitled_capstone.domain.use_case.post.DeletePost
import com.example.untitled_capstone.domain.use_case.post.GetLikedPosts
import com.example.untitled_capstone.domain.use_case.post.GetMyPosts
import com.example.untitled_capstone.domain.use_case.post.GetNickname
import com.example.untitled_capstone.domain.use_case.post.GetPostById
import com.example.untitled_capstone.domain.use_case.post.ModifyPost
import com.example.untitled_capstone.domain.use_case.post.PostUseCases
import com.example.untitled_capstone.domain.use_case.post.SearchPosts
import com.example.untitled_capstone.domain.use_case.post.ToggleLikePost
import com.example.untitled_capstone.domain.use_case.post.UploadPostImages
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideFridgeUseCases(repository: FridgeRepository): FridgeUseCases {
        return FridgeUseCases(
            addFridgeItem = AddFridgeItem(repository),
            deleteFridgeItem = DeleteFridgeItem(repository),
            toggleNotification = ToggleNotification(repository),
            modifyFridgeItems = ModifyFridgeItems(repository),
            getFridgeItems = GetFridgeItems(repository),
            getFridgeItemById = GetFridgeItemById(repository),
            getFridgeItemsByDate = GetFridgeItemsByDate(repository),
        )
    }

    @Provides
    fun provideLoginUseCases(repository: LoginRepository): LoginUseCases {
        return LoginUseCases(
            kakaoLogin = KakaoLogin(repository),
            setNickname = SetNickname(repository),
            getAddressByCoord = GetAddressByCoord(repository),
            setLocation = SetLocation(repository),
            modifyNickname = ModifyNickname(repository)
        )
    }

    @Provides
    fun provideReadAppEntry(localUserManger: LocalUserManger): ReadAppEntry {
        return ReadAppEntry(localUserManger)
    }

    @Provides
    fun provideSaveAppEntry(localUserManger: LocalUserManger): SaveAppEntry {
        return SaveAppEntry(localUserManger)
    }

    @Provides
    fun provideMyUseCases(repository: MyRepository, tokenRepository: TokenRepository): MyUseCases {
        return MyUseCases(
            getMyProfile = GetMyProfile(repository),
            getOtherProfile = GetOtherProfile(repository),
            logout = Logout(repository),
            getAccessToken = GetAccessToken(tokenRepository),
            uploadProfileImage = UploadProfileImage(repository)
        )
    }

    @Provides
    fun provideHomeUseCases(repository: HomeRepository): HomeUseCases {
        return HomeUseCases(
            getRecipeItems = GetRecipeItems(repository),
            addRecipe = AddRecipe(repository),
            getFirstRecommendation = GetFirstRecommendation(repository),
            getAnotherRecommendation = GetAnotherRecommendation(repository),
            getTastePreference = GetTastePreference(repository),
            setTastePreference = SetTastePreference(repository),
            getIsFirstSelection = GetIsFirstSelection(repository),
            setIsFirstSelection = SetIsFirstSelection(repository),
            getRecipeById = GetRecipeById(repository),
            toggleLike = ToggleLike(repository),
            deleteRecipe = DeleteRecipe(repository),
            modifyRecipe = ModifyRecipe(repository),
            uploadImage = UploadRecipeImage(repository)
        )
    }

    @Provides
    fun providePostUseCases(repository: PostRepository): PostUseCases{
        return PostUseCases(
            addPost = AddPost(repository),
            getMyPosts = GetMyPosts(repository),
            getPostById = GetPostById(repository),
            getLikedPosts = GetLikedPosts(repository),
            toggleLike = ToggleLikePost(repository),
            deletePost = DeletePost(repository),
            modifyPost = ModifyPost(repository),
            searchPosts = SearchPosts(repository),
            getNickname = GetNickname(repository),
            uploadPostImages = UploadPostImages(repository)
        )
    }

    @Provides
    fun provideChatUseCases(repository: ChatRepository): ChatUseCases{
        return ChatUseCases(
            createRoom = ChatCreateRoom(repository),
            readChats = ChatRead(repository),
            joinChatRoom = ChatRoomJoin(repository),
            closeChatRoom = ChatRoomClose(repository),
            enterChatRoom = ChatRoomEnter(repository),
            checkWhoIsIn = ChatCheckWhoIsIn(repository),
            getMessages = ChatGetMessages(repository),
            getMyRooms = ChatGetMyRooms(repository),
            exitChatRoom = ChatRoomExit(repository)
        )
    }

    @Provides
    fun provideGetAccessToken(tokenRepository: TokenRepository): GetAccessToken{
        return GetAccessToken(tokenRepository)
    }
}