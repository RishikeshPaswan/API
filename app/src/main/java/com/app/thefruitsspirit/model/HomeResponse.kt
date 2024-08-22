package com.app.thefruitsspirit.model

import java.io.Serializable

data class HomeResponse(
    val body: ArrayList<Body>,
    val code: Int?,
    val message: String?,
    val success: Boolean?
)
    :Serializable{
    data class Body(
        val _id: String?,
        val image: String?,
        val name: String?
    )
        :Serializable
}