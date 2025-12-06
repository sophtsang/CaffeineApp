package com.example.caffiene.data.remote

import com.example.caffiene.data.model.IdResponse
import com.example.caffiene.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApiService {
//    @GET("users")
//    suspend fun getAllUsers(): List<User>

    // POST /users - create new user
    @POST("users")
    suspend fun createUser(
        @Body user: User
    ): User
}