package com.app.thefruitsspirit.model

data class ResourceOneProductResponse(
    val body: Body?,
    val code: Int?,
    val message: String?,
    val success: Boolean?
) {
    data class Body(
        val _id: String?,
        val description: String?,
        val title: String?
    )
}