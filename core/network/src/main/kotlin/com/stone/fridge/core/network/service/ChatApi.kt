package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.ChatMember
import com.stone.fridge.core.model.ChatRoomRaw
import com.stone.fridge.core.model.ChatRoom
import com.stone.fridge.core.model.Message
import com.stone.fridge.core.network.model.ApiResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {

    @POST("/api/chat/room/{roomId}/read")
    suspend fun readChats(
        @Path ("roomId") roomId: Long
    ): ApiResponse<Int>

    @POST("/api/chat/room/{roomId}/join")
    suspend fun joinChatRoom(
        @Path("roomId") roomId: Long
    ): ApiResponse<ChatRoom>

    @POST("/api/chat/room/{roomId}/close")
    suspend fun closeChatRoom(
        @Path("roomId") roomId: Long
    ): ApiResponse<String>

    @GET("/api/chat/room/{roomId}")
    suspend fun enterChatRoom(
        @Path("roomId") roomId: Long
    ): ApiResponse<ChatRoom>

    @GET("/api/chat/room/{roomId}/participants")
    suspend fun checkWhoIsIn(
        @Path("roomId") roomId: Long
    ): ApiResponse<List<ChatMember>>

    @GET("/api/chat/room/{roomId}/messages/scroll")
    suspend fun getMessages(
        @Path("roomId") roomId: Long,
        @Query("lastMessageId") lastMessageId: Long? = null,
        @Query("size") size: Int = 20
    ): ApiResponse<List<Message>>

    @GET("/api/chat/my-rooms")
    suspend fun getMyRooms(): ApiResponse<List<ChatRoomRaw>>

    @DELETE("/api/chat/room/{roomId}/exit")
    suspend fun exitChatRoom(
        @Path("roomId") roomId: Long
    ): ApiResponse<String>
}