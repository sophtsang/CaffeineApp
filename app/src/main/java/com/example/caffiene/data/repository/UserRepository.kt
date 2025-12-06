package com.example.caffiene.data.repository

import com.example.caffiene.data.model.User
import com.example.caffiene.data.remote.UserApiService
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApiService: UserApiService
) {
//    suspend fun getAllUsers(): List<User> = runCatching {
//        userApiService.getAllUsers()
//    }

    // POST /users - create new user
    suspend fun createUser(user: User): Result<User> = runCatching {
        userApiService.createUser(user)
    }
}