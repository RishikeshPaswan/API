package com.app.thefruitsspirit.model

data class SignUpResponse(
    val body: Body?,
    val code: Int?,
    val message: String?,
    val success: Boolean?
) {
    data class Body(
        val __v: Int?,
        val _id: String?,
        val amount: Int?,
        val countryCode: String?,
        val countryCodePhone: String?,
        val coupon: Any?,
        val createdAt: String?,
        val email: String?,
        val image: String?,
        val isAdmin: Boolean?,
        val isDeActivated: Boolean?,
        val location: Location?,
        val login_time: Int?,
        val name: String?,
        val organizationId: Any?,
        val otp: Int?,
        val otpVerified: Int?,
        val phoneNumber: Long?,
        val subscriptionPrice: Int?,
        val token: String?,
        val updatedAt: String?,
        val userFromCoupon: Boolean?,
        val userPaidAmount: Boolean?
    ) {
        data class Location(
            val coordinates: List<Double>?,
            val locationName: String?,
            val type: String?
        )
    }
}