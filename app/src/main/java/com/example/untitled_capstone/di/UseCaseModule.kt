package com.example.untitled_capstone.di

import com.example.untitled_capstone.domain.repository.ChatRepository
import com.example.untitled_capstone.domain.repository.FridgeRepository
import com.example.untitled_capstone.domain.repository.LocalUserRepository
import com.example.untitled_capstone.domain.repository.LoginRepository
import com.example.untitled_capstone.domain.repository.MyRepository
import com.example.untitled_capstone.domain.repository.PostRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.example.untitled_capstone.domain.repository.WebSocketRepository
import com.example.untitled_capstone.domain.use_case.app_entry.ReadAppEntry
import com.example.untitled_capstone.domain.use_case.app_entry.SaveAppEntry
import com.example.untitled_capstone.domain.use_case.chat.ChatCheckWhoIsIn
import com.example.untitled_capstone.domain.use_case.chat.ChatGetMessages
import com.example.untitled_capstone.domain.use_case.chat.ChatGetMyRooms
import com.example.untitled_capstone.domain.use_case.chat.ChatRead
import com.example.untitled_capstone.domain.use_case.chat.ChatRoomClose
import com.example.untitled_capstone.domain.use_case.chat.ChatRoomEnter
import com.example.untitled_capstone.domain.use_case.chat.ChatRoomExit
import com.example.untitled_capstone.domain.use_case.chat.ChatRoomJoin
import com.example.untitled_capstone.domain.use_case.chat.ChatUseCases
import com.example.untitled_capstone.domain.use_case.chat.ConnectChatSocket
import com.example.untitled_capstone.domain.use_case.chat.Disconnect
import com.example.untitled_capstone.domain.use_case.chat.GetChatMessagesUseCase
import com.example.untitled_capstone.domain.use_case.chat.SendMessage
import com.example.untitled_capstone.domain.use_case.chat.SendReadEvent
import com.example.untitled_capstone.domain.use_case.chat.SubscribeRoom
import com.example.untitled_capstone.domain.use_case.fridge.AddFridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.DeleteFridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.FridgeUseCases
import com.example.untitled_capstone.domain.use_case.fridge.GetFridgeItemById
import com.example.untitled_capstone.domain.use_case.fridge.GetFridgeItems
import com.example.untitled_capstone.domain.use_case.fridge.GetFridgeItemsByDate
import com.example.untitled_capstone.domain.use_case.fridge.ModifyFridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.ToggleNotification
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
import com.example.untitled_capstone.domain.use_case.my.GetMyNicknameUseCase
import com.example.untitled_capstone.domain.use_case.my.MyUseCases
import com.example.untitled_capstone.domain.use_case.my.ReportUserUseCase
import com.example.untitled_capstone.domain.use_case.my.UploadProfileImage
import com.example.untitled_capstone.domain.use_case.post.AddPost
import com.example.untitled_capstone.domain.use_case.post.DeleteAllSearchHistory
import com.example.untitled_capstone.domain.use_case.post.DeletePost
import com.example.untitled_capstone.domain.use_case.post.DeletePostImage
import com.example.untitled_capstone.domain.use_case.post.DeleteSearchHistory
import com.example.untitled_capstone.domain.use_case.post.GetLikedPosts
import com.example.untitled_capstone.domain.use_case.post.GetMyPosts
import com.example.untitled_capstone.domain.use_case.post.GetPostById
import com.example.untitled_capstone.domain.use_case.post.GetSearchHistory
import com.example.untitled_capstone.domain.use_case.post.ModifyPost
import com.example.untitled_capstone.domain.use_case.post.PostUseCases
import com.example.untitled_capstone.domain.use_case.post.ReportPostUseCase
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
            modifyFridgeItems = ModifyFridgeItem(repository),
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
    fun provideReadAppEntry(localUserManger: LocalUserRepository): ReadAppEntry {
        return ReadAppEntry(localUserManger)
    }

    @Provides
    fun provideSaveAppEntry(localUserManger: LocalUserRepository): SaveAppEntry {
        return SaveAppEntry(localUserManger)
    }

    @Provides
    fun provideMyUseCases(repository: MyRepository, tokenRepository: TokenRepository): MyUseCases {
        return MyUseCases(
            getMyProfile = GetMyProfile(repository),
            getOtherProfile = GetOtherProfile(repository),
            logout = Logout(repository),
            getAccessToken = GetAccessToken(tokenRepository),
            uploadProfileImage = UploadProfileImage(repository),
            reportUser = ReportUserUseCase(repository),
            getMyNicknameUseCase = GetMyNicknameUseCase(repository)
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
            uploadPostImages = UploadPostImages(repository),
            deletePostImage = DeletePostImage(repository),
            getSearchHistory = GetSearchHistory(repository),
            deleteSearchHistory = DeleteSearchHistory(repository),
            deleteAllSearchHistory = DeleteAllSearchHistory(repository),
            reportPost = ReportPostUseCase(repository)
        )
    }

    @Provides
    fun provideChatUseCases(repository: ChatRepository, socketRepository: WebSocketRepository): ChatUseCases{
        return ChatUseCases(
            readChats = ChatRead(repository),
            joinChatRoom = ChatRoomJoin(repository),
            closeChatRoom = ChatRoomClose(repository),
            enterChatRoom = ChatRoomEnter(repository),
            checkWhoIsIn = ChatCheckWhoIsIn(repository),
            getMessages = ChatGetMessages(repository),
            getMyRooms = ChatGetMyRooms(repository),
            exitChatRoom = ChatRoomExit(repository),
            sendMessage = SendMessage(socketRepository),
            subscribeRoom = SubscribeRoom(socketRepository),
            disconnect = Disconnect(socketRepository),
            sendReadEvent = SendReadEvent(socketRepository),
            connectChatSocket = ConnectChatSocket(socketRepository),
            getChatMessages = GetChatMessagesUseCase(repository)
        )
    }

    @Provides
    fun provideGetAccessToken(tokenRepository: TokenRepository): GetAccessToken{
        return GetAccessToken(tokenRepository)
    }
}