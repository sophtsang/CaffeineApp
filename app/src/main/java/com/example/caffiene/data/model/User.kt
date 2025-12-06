package com.example.caffiene.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val email: String,
    @SerialName("password_hash") val passwordHash: String,
    @SerialName("daily_caffeine_limit") val dailyCaffeineLimit: Int,
    @SerialName("weight_lbs") val weightLbs: Double
)

@Serializable
data class Beverage(
    val id: Int,
    val name: String,
    @SerialName("caffeine_content_mg") val caffeineContentMg: Int,
    @SerialName("image_url") val imageUrl: String? = null,
    val category: String
)

@Serializable
data class Consumption(
    val id: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("beverage_id") val beverageId: Int,
    @SerialName("serving_count") val servingCount: Int,
    val timestamp: String? = null
)

@Serializable
data class CaffeineBreakdown(
    val beverage: String,
    val servings: Int,
    @SerialName("caffeine_mg") val caffeineMg: Int
)

@Serializable
data class DailySummary(
    val date: String,
    @SerialName("total_caffeine_mg") val totalCaffeineMg: Int,
    val breakdown: List<CaffeineBreakdown>
)

// Weekly summary: Map<String, Int>
typealias WeeklySummary = Map<String, Int>

@Serializable
data class UserStats(
    @SerialName("user_id") val userId: Int,
    @SerialName("daily_total_caffeine_mg") val dailyTotalCaffeineMg: Int,
    @SerialName("daily_limit_mg") val dailyLimitMg: Int,
    @SerialName("percentage_of_limit") val percentageOfLimit: Double,
    @SerialName("remaining_mg") val remainingMg: Int
)

@Serializable
data class IdResponse(
    val id: Int
)

@Serializable
data class MessageResponse(
    val message: String
)