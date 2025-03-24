package com.example.untitled_capstone.data.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.PostDto
import com.example.untitled_capstone.data.remote.service.ShoppingApi
import com.example.untitled_capstone.domain.repository.ShoppingRepository
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val api: ShoppingApi
): ShoppingRepository {
    override suspend fun post(postDto: PostDto): Resource<ApiResponse> {
        return try {
            Resource.Loading(data = null)
            val response = api.post(postDto)
            if(response.isSuccess){
                Resource.Success(response)
            }else {
                Resource.Error(message = response.toString())
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }

    }
}