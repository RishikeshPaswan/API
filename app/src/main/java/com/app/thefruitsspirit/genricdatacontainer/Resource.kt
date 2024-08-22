package com.app.thefruitsspirit.genricdatacontainer

import android.app.Activity
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.Progress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//// A generic class that contains data and status about loading this data.


data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            Progress.hide()
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            Progress.hide()
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?, showLoader : Boolean = true, activity : Activity): Resource<T> {

            CoroutineScope(Dispatchers.Main).launch{
                if (showLoader) {
                    Progress.show(activity)
                }
            }
            return Resource(Status.LOADING, data, null)


        }
    }


}
