package com.app.thefruitsspirit.constants

object ApiConstants {
    //Live Url
    const val BASE_URL = "http://65.2.68.95:8520/user/"
    const val BASE_URL_IMAGE = "http://65.2.68.95:8520/Image/userImage/"

    const val PRODUCT_IMAGE = "http://65.2.68.95:8520/Image/productImage/"

    //Constants for utils
    const val DEVICE_TYPE = 1
    const val Content_Length = "application/json"


    const val CONNECT_TIMEOUT: Long = 500000
    const val READ_TIMEOUT: Long = 500000
    const val WRITE_TIMEOUT: Long = 500000

    const val SIGNUP = "signup"
    const val LOGIN = "login"
    const val OTP_VERIFY = "verifyotp"
    const val RESEND_OTP = "resendOtp"
    const val HOME_GET = "homeapi"
    const val USER_PROFILE = "userprofile"
    const val EDIT_PROFILE = "userprofileedit"
    const val TERMS_PRIVACY = "cmsdetails"
    const val DE_ACTIVE_ACCOUNT = "deActiveAccount"
    const val FAQS = "getallfaq"
    const val SUPPORT_CONTACT = "contactsupportcreate"
    const val LOG_OUT = "userlogout"
    const val BENEFITS_GET = "benefits"
    const val ONE_PRODUCT_BENEFITS = "oneproductbenefits"
//    const val RECIPES_GET = "recipes"
    const val RECIPES_GET = "BonusAndRess"
    const val RECIPES_PRODUCT = "productrecipe"
    const val RESOURCE_GET = "growprocess"
    const val PRODUCT_CONSUME = "productconsume"
    const val SUMMARY = "summary"
    const val PRODUCT_GROW = "productgrowprocess"
    const val GET_NOTIFICATION = "usernotifications"

}