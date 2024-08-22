package com.app.thefruitsspirit.retrofit


import android.app.Activity
import com.app.thefruitsspirit.base.BaseError


import okhttp3.ResponseBody
import org.json.JSONObject

fun getErrorMessage(responseBody: ResponseBody): BaseError {
    return try {
        val jsonObject = JSONObject(responseBody.string())

        BaseError(jsonObject.getInt("statusCode"), jsonObject.getString("message"))

    } catch (e: Exception) {
        BaseError(101, e.message!!)
    }

}

fun getErrorMessage(jsonObject: JSONObject): BaseError {
    return try {
        BaseError(jsonObject.getInt("statusCode"), jsonObject.getString("message"))

    } catch (e: Exception) {
        BaseError(101, e.message!!)
    }
}

fun tackleError(activity: Activity, errorBody: BaseError) {
    when (errorBody.code) {

        401 -> {
            // showAlert(activity, errorBody.message,activity.getString(R.string.ok)) {
//            activity.startActivity(Intent(activity, LoginActivity::class.java))
//            activity.finishAffinity()
//            clearAllData(activity)
//            clearData(activity, "role")
//            clearData(activity, "token")
        }

    }

    // 400 -> {
    //   showAlert(activity, errorBody.message,activity.getString(R.string.ok),{})

}