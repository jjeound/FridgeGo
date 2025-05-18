package com.example.untitled_capstone.data.local.remote

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.untitled_capstone.data.local.entity.ProfileEntity

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profileentity")
    suspend fun getProfile(): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Delete
    suspend fun deleteProfile(profile: ProfileEntity)

    @Query("UPDATE profileentity SET nickname = :nickname")
    suspend fun updateNickname(nickname: String)
}






