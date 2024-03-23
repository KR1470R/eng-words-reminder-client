package com.example.eng_words_reminder_client.network

import com.example.eng_words_reminder_client.network.requests.RequestGetTenWords
import com.example.eng_words_reminder_client.network.requests.RequestLogin
import com.example.eng_words_reminder_client.network.requests.RequestRegister
import com.example.eng_words_reminder_client.network.responses.ResponseGetTenWords
import com.example.eng_words_reminder_client.network.responses.ResponseLogin
import com.example.eng_words_reminder_client.network.responses.ResponseRegister
import com.example.eng_words_reminder_client.network.responses.ResponseStatistic
import retrofit2.http.*

interface RequestAPI {

    @POST("./auth/register")
    suspend fun registerUser(
        @Body request: RequestRegister,
        @Header("Authorization") password: String?
    ): ResponseRegister

    @POST("./auth/login")
    suspend fun loginUser(
        @Body request: RequestLogin,
        @Header("Authorization") password: String?
    ): ResponseLogin

    @PUT("./dataset-user/book-words")
    suspend fun getTenWords(
        @Body request: RequestGetTenWords,
        @Header("Authorization") password: String?
    ): List<ResponseGetTenWords>

    @GET("./dataset-user/statistic")
    suspend fun getStatistic(
        @Header("Authorization") password: String?
    ): ResponseStatistic

    @DELETE("./dataset-user/statistic")
    suspend fun resetStatistic(
        @Header("Authorization") password: String?
    )
}