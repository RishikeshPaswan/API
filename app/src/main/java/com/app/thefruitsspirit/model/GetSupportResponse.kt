package com.app.thefruitsspirit.model

data class GetSupportResponse(
    val body: Body?,
    val code: Int?,
    val message: String?,
    val success: Boolean?
) {
    data class Body(
        val CountryCode: Int?,
        val __v: Int?,
        val _id: String?,
        val createdAt: String?,
        val email: String?,
        val message: String?,
        val name: String?,
        val phoneNumber: Long?,
        val updatedAt: String?,
        val userId: String?
    )
}