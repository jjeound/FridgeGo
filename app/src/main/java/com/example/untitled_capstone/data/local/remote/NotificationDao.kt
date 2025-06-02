package com.example.untitled_capstone.data.local.remote

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.untitled_capstone.data.local.entity.NotificationEntity

@Dao
interface NotificationDao {

    @Query("SELECT * FROM NotificationEntity ORDER BY id DESC")
    fun getAllNotifications(): List<NotificationEntity>

    @Query("UPDATE notificationentity SET isRead = :value")
    suspend fun updateAllIsRead(value: Boolean = true)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)
}






