package com.app.thefruitsspirit.model

data class ResourceResponse(

    val body: List<Body>,
    val code: Int?,
    val message: String?,
    val success: Boolean?
) {
    data class Body(
        val _id: String?,
        val description: String?,
        val image: String?,
        val productID: String?,
        val title: String?
    )
}