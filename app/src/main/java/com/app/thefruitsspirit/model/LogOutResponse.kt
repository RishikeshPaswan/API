package com.app.thefruitsspirit.model

data class LogOutResponse(
    val body: Body?,
    val code: Int?,
    val message: String?,
    val success: Boolean?
) {
    class Body
}