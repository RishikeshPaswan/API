package com.app.thefruitsspirit.model

import java.io.Serializable

data class ProfileResponse(
    val body: Body?,
    val code: Int?,
    val message: String?,
    val success: Boolean?
)  :Serializable
{
    data class Body(
        val countryCode: String?,
        val email: String?,
        val image: String?,
        val name: String?,
        val phoneNumber: Long?
    )
        :Serializable
}