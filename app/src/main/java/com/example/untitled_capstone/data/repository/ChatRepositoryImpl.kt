package com.example.untitled_capstone.data.repository

import android.content.Context
import com.example.untitled_capstone.core.util.PrefKeys.NICKNAME
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.NewChatRoomBody
import com.example.untitled_capstone.data.remote.service.ChatApi
import com.example.untitled_capstone.domain.model.ChatMember
import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.domain.model.ChattingRoomRaw
import com.example.untitled_capstone.domain.model.Message
import com.example.untitled_capstone.domain.repository.ChatRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi,
): ChatRepository {
    override suspend fun createChatRoom(newChatRoomBody: NewChatRoomBody): Resource<ChattingRoom> {
        return try {
            Resource.Loading(data = null)
            val response = api.createChatRoom(newChatRoomBody)
            if(response.isSuccess){
                Resource.Success(response.result!!.toChattingRoom())
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun readChats(id: Long): Resource<Int> {
        return try {
            Resource.Loading(data = null)
            val response = api.readChats(id)
            if(response.isSuccess){
                Resource.Success(response.result)
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun joinChatRoom(id: Long): Resource<ChattingRoom> {
        return try {
            Resource.Loading(data = null)
            val response = api.joinChatRoom(id)
            if(response.isSuccess){
                Resource.Success(response.result!!.toChattingRoom())
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun closeChatRoom(id: Long): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.closeChatRoom(id)
            if(response.isSuccess){
                Resource.Success(response.result)
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun enterChatRoom(id: Long): Resource<ChattingRoom> {
        return try {
            Resource.Loading(data = null)
            val response = api.enterChatRoom(id)
            if(response.isSuccess){
                Resource.Success(response.result!!.toChattingRoom())
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun checkWhoIsIn(id: Long): Resource<List<ChatMember>> {
        return try {
            Resource.Loading(data = null)
            val response = api.checkWhoIsIn(id)
            if(response.isSuccess){
                Resource.Success(response.result?.map { it.toChatMember() })
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun getMessages(id: Long, lastMessageId: Long?): Resource<List<Message>> {
        return try {
            Resource.Loading(data = null)
            val response = api.getMessages(id, lastMessageId)
            if(response.isSuccess){
                Resource.Success(response.result?.map { it.toMessage() })
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun getMyRooms(): Resource<List<ChattingRoomRaw>> {
        return try {
            Resource.Loading(data = null)
            val response = api.getMyRooms()
            if(response.isSuccess){
                Resource.Success(response.result?.map { it.toChattingRoomRaw() })
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun exitChatRoom(id: Long): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.exitChatRoom(id)
            if(response.isSuccess){
                Resource.Success(response.result)
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }
}