package com.example.untitled_capstone.data.util

import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

//class ApiResponseAdapter<T>(private val gson: Gson, private val type: Type) :
//    JsonDeserializer<ApiResponse<T>> {
//
//    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): ApiResponse<T> {
//        val jsonObject = json.asJsonObject
//
//        val isSuccess = jsonObject.get("isSuccess").asBoolean
//        val code = jsonObject.get("code").asString
//        val message = jsonObject.get("message").asString
//        val result: T = gson.fromJson(jsonObject.get("result"), type)
//
//        return ApiResponse(isSuccess, code, message, result)
//    }
//}