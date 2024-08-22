package com.app.thefruitsspirit.model

data class SummaryResponse(
    val body: List<Body>?,
    val code: Int?,
    val message: String?,
    val success: Boolean?
) {
    data class Body(
        val count: Int?,
        val image: String?,
        val monthName: String?,
        val name: String?,
        val productId: String?,
        val userId: String?
    )
}