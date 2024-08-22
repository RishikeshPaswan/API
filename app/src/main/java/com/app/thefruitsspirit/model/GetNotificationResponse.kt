package com.app.thefruitsspirit.model

data class GetNotificationResponse(
    val body: List<Body>,
    val code: Int?,
    val message: String?,
    val success: Boolean?
) {
    data class Body(
        val __v: Int?,
        val _id: String?,
        val createdAt: String?,
        val is_read: String?,
        val message: String?,
        val productId: ProductId?,
        val receiver_id: String?,
        val sender_id: String?,
        val type: String?,
        val updatedAt: String?
    ) {
        data class ProductId(
            val __v: Int?,
            val _id: String?,
            val createdAt: String?,
            val image: String?,
            val name: String?,
            val updatedAt: String?
        )
    }
}