package com.app.thefruitsspirit.model

data class EditProfileResponse(
    val body: Body?,
    val code: Int?,
    val message: String?,
    val success: Boolean?
) {
    data class Body(
        val countryCode: String?,
        val email: String?,
        val image: String?,
        val name: String?,
        val phoneNumber: Long?
    )
}