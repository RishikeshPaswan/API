package com.app.thefruitsspirit.cache

import android.content.Context
import com.app.thefruitsspirit.constants.CacheConstants
import com.app.thefruitsspirit.constants.CacheConstants.USER_DATA
import com.app.thefruitsspirit.utils.Prefs
import com.app.thefruitsspirit.base.AppController

import retrofit2.http.Body

fun getUser(context: Context): Body? {
    return Prefs.with(context)?.getObject(USER_DATA, Body::class.java)
}

fun saveLoginPreference(key: String, value: Any) {
    val preference = AppController.getInstance()!!.getSharedPreferences(
        CacheConstants.SHARED_PREFERENCES_REMEMBER_NAME,
        0
    )
    val editor = preference.edit()

    when (value) {
        is String -> editor.putString(key, value)
        is Boolean -> editor.putBoolean(key, value)
        is Int -> editor.putInt(key, value)
    }
    editor.apply()
}

inline fun <reified T> getLoginPreference(key: String, deafultValue: T): T {
    val preference = AppController.getInstance()!!.getSharedPreferences(
        CacheConstants.SHARED_PREFERENCES_REMEMBER_NAME,
        0
    )
    return when (T::class) {
        String::class -> preference.getString(key, deafultValue as String) as T
        Boolean::class -> preference.getBoolean(key, deafultValue as Boolean) as T
        Int::class -> preference.getInt(key, deafultValue as Int) as T
        else -> {
            " " as T
        }
    }
}

fun clearPreferencesRemember() {
    val preference = AppController.getInstance()!!.getSharedPreferences(
        CacheConstants.SHARED_PREFERENCES_REMEMBER_NAME,
        0
    )
    val editor = preference.edit()
    editor.clear()
    editor.apply()
}

fun getToken(context: Context): String? {
    return Prefs.with(context)?.getString(CacheConstants.USER_TOKEN, "")
}


fun saveToken(context: Context, token: String) {
    Prefs.with(context)?.save(CacheConstants.USER_TOKEN, token)
}

fun saveString(context: Context, value: String) {
    Prefs.with(context)?.save(USER_DATA, value)
}

fun saveEmail(context: Context, value: String) {
    Prefs.with(context)?.save(USER_DATA, value)
}
fun savePassword(context: Context, value: String) {
    Prefs.with(context)?.save(USER_DATA, value)
}

fun saveString(context: Context, key: String, value: String) {
    Prefs.with(context)?.save(key, value)
}

fun getMyString(context: Context, user_data: String): String? {
    return Prefs.with(context)?.getString(USER_DATA, "")
}



fun clearData(context: Context, key: String) {
    Prefs.with(context)?.remove(key)
}

fun clearAllData(context: Context) {
    Prefs.with(context)?.removeAll()
}
