package com.stone.fridge.core.database.di

import android.content.Context
import androidx.room.Room
import com.stone.fridge.core.database.GoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideGoDatabase(@ApplicationContext context: Context): GoDatabase{
        return Room.databaseBuilder(
            context, GoDatabase::class.java, "go-database"
        ).build()
    }
}