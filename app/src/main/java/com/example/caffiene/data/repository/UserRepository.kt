package com.example.caffiene.data.repository

import com.example.caffiene.data.model.Beverage
import com.example.caffiene.data.model.BeverageResponse
import com.example.caffiene.data.model.DeletedBeverageResponse
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
    // GET /beverages - return all available beverages for selection
    suspend fun getBeverages(): Result<List<Beverage>> = runCatching {
        userApiService.getBeverages().beverages
    }

    suspend fun createBeverage(beverage: Beverage): Result<Beverage> = runCatching {
        userApiService.createBeverage(beverage)
    }

    suspend fun deleteBeverage(bevId: Int): Result<DeletedBeverageResponse> = runCatching {
        userApiService.deleteBeverage(bevId)
    }

    // POST /users - create new user
    suspend fun createUser(user: User): Result<User> = runCatching {
        userApiService.createUser(user)
    }
}