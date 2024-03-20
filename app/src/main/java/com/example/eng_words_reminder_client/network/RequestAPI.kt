package com.example.eng_words_reminder_client.network

import com.example.eng_words_reminder_client.network.requests.RequestLogin
import com.example.eng_words_reminder_client.network.requests.RequestRegister
import com.example.eng_words_reminder_client.network.responses.ResponseLogin
import com.example.eng_words_reminder_client.network.responses.ResponseRegister
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

    /*
        @POST("./users")
        suspend fun registerUser(@Body request: RegisterUserRequest): AuthorizeResponse

        @POST("./login")
        suspend fun login(@Body request: LoginRequest): AuthorizeResponse

        @GET("users/{userId}")
        suspend fun getProfile(
            @Path("userId") userId: String,
            @Header("Authorization") accessToken: String?
        ): ProfileResponse

        @PUT("users/{userId}")
        suspend fun editProfile(
            @Path("userId") userId: String,
            @Header("Authorization") accessToken: String?,
            @Body request: EditProfileRequest
        ): ProfileResponse

        @GET("./users")
        suspend fun getUsers(
            @Header("Authorization") accessToken: String?,
        ): UsersResponse

        @PUT("users/{userId}/contacts")
        suspend fun addContact(
            @Path("userId") userId: String,
            @Header("Authorization") accessToken: String,
            @Body request: AddContactRequest
        ): ContactsResponse

        @DELETE("users/{userId}/contacts/{deletingUser}")
        suspend fun deleteContact(
            @Path("userId") userId: String,
            @Path("deletingUser") deletingUser: String,
            @Header("Authorization") accessToken: String?
        ): ContactsResponse

        @GET("users/{userId}/contacts")
        suspend fun getContacts(
            @Path("userId") userId: String,
            @Header("Authorization") accessToken: String?
        ): ContactsResponse*/
}