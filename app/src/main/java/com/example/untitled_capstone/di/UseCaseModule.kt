package com.example.untitled_capstone.di

import com.example.untitled_capstone.domain.repository.FridgeRepository
import com.example.untitled_capstone.domain.repository.HomeRepository
import com.example.untitled_capstone.domain.repository.LocalUserManger
import com.example.untitled_capstone.domain.repository.LoginRepository
import com.example.untitled_capstone.domain.repository.MyRepository
import com.example.untitled_capstone.domain.repository.ShoppingRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.example.untitled_capstone.domain.use_case.app_entry.ReadAppEntry
import com.example.untitled_capstone.domain.use_case.app_entry.SaveAppEntry
import com.example.untitled_capstone.domain.use_case.fridge.AddFridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.DeleteFridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.FridgeUseCases
import com.example.untitled_capstone.domain.use_case.fridge.GetFridgeItemById
import com.example.untitled_capstone.domain.use_case.fridge.GetFridgeItems
import com.example.untitled_capstone.domain.use_case.fridge.GetFridgeItemsByDate
import com.example.untitled_capstone.domain.use_case.fridge.ModifyFridgeItems
import com.example.untitled_capstone.domain.use_case.fridge.ToggleNotification
import com.example.untitled_capstone.domain.use_case.home.AddRecipe
import com.example.untitled_capstone.domain.use_case.home.GetAnotherRecommendation
import com.example.untitled_capstone.domain.use_case.home.GetFirstRecommendation
import com.example.untitled_capstone.domain.use_case.home.GetRecipeItems
import com.example.untitled_capstone.domain.use_case.home.GetTastePreference
import com.example.untitled_capstone.domain.use_case.home.HomeUseCases
import com.example.untitled_capstone.domain.use_case.home.SetTastePreference
import com.example.untitled_capstone.domain.use_case.my.GetAccessToken
import com.example.untitled_capstone.domain.use_case.my.GetMyProfile
import com.example.untitled_capstone.domain.use_case.my.GetOtherProfile
import com.example.untitled_capstone.domain.use_case.login.KakaoLogin
import com.example.untitled_capstone.domain.use_case.login.LoginUseCases
import com.example.untitled_capstone.domain.use_case.my.Logout
import com.example.untitled_capstone.domain.use_case.login.SetNickname
import com.example.untitled_capstone.domain.use_case.my.MyUseCases
import com.example.untitled_capstone.domain.use_case.shopping.AddPost
import com.example.untitled_capstone.domain.use_case.shopping.ShoppingUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
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
            setNickname = SetNickname(repository)
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
            getAccessToken = GetAccessToken(tokenRepository)
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
            setTastePreference = SetTastePreference(repository)
        )
    }

    @Provides
    fun provideShoppingUseCases(repository: ShoppingRepository): ShoppingUseCases{
        return ShoppingUseCases(
            addPost = AddPost(repository)
        )
    }

    @Provides
    fun provideGetAccessToken(tokenRepository: TokenRepository): GetAccessToken{
        return GetAccessToken(tokenRepository)
    }
}