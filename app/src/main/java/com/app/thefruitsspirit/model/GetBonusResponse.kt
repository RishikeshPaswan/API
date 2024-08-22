package com.app.thefruitsspirit.model

data class GetBonusResponse(
    val body: Body?,
    val code: Int?,
    val message: String?,
    val success: Boolean?
) {
    data class Body(
        val __v: Int?,
        val _id: String?,
        val content: String?,
        val createdAt: String?,
        val title: String?,
        val type: Int?,
        val updatedAt: String?
    )
}