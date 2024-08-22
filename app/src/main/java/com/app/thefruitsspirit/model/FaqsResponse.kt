package com.app.thefruitsspirit.model

data class FaqsResponse(
    val body: List<Body>?,
    val code: Int?,
    val message: String?,
    val success: Boolean?
) {
    data class Body(
        val __v: Int?,
        val _id: String?,
        val answer: String?,
        val createdAt: String?,
        val question: String?,
        val updatedAt: String?
    )
}