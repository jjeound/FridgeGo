package com.example.untitled_capstone.di

import com.example.untitled_capstone.domain.repository.LocalUserRepository
import com.example.untitled_capstone.domain.repository.LoginRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.example.untitled_capstone.domain.use_case.app_entry.ReadAppEntry
import com.example.untitled_capstone.domain.use_case.app_entry.SaveAppEntry
import com.example.untitled_capstone.domain.use_case.login.GetAddressByCoord
import com.example.untitled_capstone.domain.use_case.my.GetAccessTokenUseCase
import com.example.untitled_capstone.domain.use_case.login.KakaoLogin
import com.example.untitled_capstone.domain.use_case.login.LoginUseCases
import com.example.untitled_capstone.domain.use_case.login.ModifyNickname
import com.example.untitled_capstone.domain.use_case.login.SetLocation
import com.example.untitled_capstone.domain.use_case.login.SetNickname
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

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
    fun provideGetAccessToken(tokenRepository: TokenRepository): GetAccessTokenUseCase{
        return GetAccessTokenUseCase(tokenRepository)
    }
}