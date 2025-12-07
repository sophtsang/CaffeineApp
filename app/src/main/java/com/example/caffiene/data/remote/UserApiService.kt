package com.example.caffiene.data.remote

import com.example.caffiene.data.model.Beverage
import com.example.caffiene.data.model.BeverageResponse
import com.example.caffiene.data.model.DeletedBeverageResponse
import com.example.caffiene.data.model.IdResponse
import com.example.caffiene.data.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface UserApiService {
    @GET("beverages")
    suspend fun getBeverages(): BeverageResponse

    @POST("beverages")
    suspend fun createBeverage(
        @Body beverage: Beverage
    ): Beverage

    @DELETE("beverages/{bev_id}")
    suspend fun deleteBeverage(
        @Path("bev_id") bevId: Int
    ): DeletedBeverageResponse

    @POST("users")
    suspend fun createUser(
        @Body user: User
    ): User
}