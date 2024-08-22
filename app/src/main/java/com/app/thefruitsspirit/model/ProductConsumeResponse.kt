package com.app.thefruitsspirit.model

data class ProductConsumeResponse(
    val body: Body?,
    val code: Int?,
    val message: String?,
    val success: Boolean?
) {
    data class Body(
        val __v: Int?,
        val _id: String?,
        val createdAt: String?,
        val productId: String?,
        val updatedAt: String?,
        val userId: String?
    )
}