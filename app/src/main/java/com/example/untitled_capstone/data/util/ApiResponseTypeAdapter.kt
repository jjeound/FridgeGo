package com.example.untitled_capstone.data.util

import com.example.untitled_capstone.data.remote.dto.ApiResponseTest
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


class ApiResponseTypeAdapter<T>(
    private val resultAdapter: TypeAdapter<T>
) : TypeAdapter<ApiResponseTest<T>>() {
    override fun write(
        out: JsonWriter?,
        value: ApiResponseTest<T>?
    ) {
        throw UnsupportedOperationException("Serialization not supported")
    }

    override fun read(`in`: JsonReader?): ApiResponseTest<T>? {
        val jsonObject = JsonParser.parseReader(`in`).asJsonObject

        val isSuccess = jsonObject["isSuccess"]?.asBoolean == true
        val code = jsonObject["code"]?.asString ?: ""
        val message = jsonObject["message"]?.asString ?: ""

        val resultJson = jsonObject["result"]
        val result = if (resultJson != null && !resultJson.isJsonNull) {
            resultAdapter.fromJsonTree(resultJson)
        } else {
            null
        }

        return ApiResponseTest(isSuccess, code, message, result)
    }
}