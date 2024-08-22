package com.app.thefruitsspirit.base

import android.app.Application
import android.content.Context
import android.os.StrictMode
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppController : Application(), AppLifecycleHandler.AppLifecycleDelegates {
    private var context: Context? = null
    companion object {
//        var instance: AppController? = null
        //        var instance: AppController? = null
        lateinit var application: AppController

//        @get:Synchronized
//        var mSocketManager: SocketManager? = null

        fun getInstance(): AppController {
            return application
        }
    }

    private var lifecycleHandler: AppLifecycleHandler? = null
    override fun onCreate() {
        super.onCreate()
        application = this
        application.context = applicationContext

        // dark mode
        //   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

//        mSocketManager = getSocketManager()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        lifecycleHandler = AppLifecycleHandler(this)
        registerLifecycleHandler(lifecycleHandler!!)

    }

//    fun getSocketManager(): SocketManager? {
//        mSocketManager = if (mSocketManager == null) {
//            SocketManager()
//        } else {
//            return mSocketManager
//        }
//        return mSocketManager
//    }

    private fun registerLifecycleHandler(lifecycleHandler: AppLifecycleHandler) {
        registerActivityLifecycleCallbacks(lifecycleHandler)
        registerComponentCallbacks(lifecycleHandler)
    }

    override fun onAppForegrounded() {
//        Log.e("Application", "Foreground")
//        if (!mSocketManager!!.isConnected() || mSocketManager!!.getmSocket() == null) {
//            mSocketManager!!.init()
//        }
    }

    override fun onAppBackgrounded() {
        //        SocketManager.onDisConnect()
        Log.e("DisconnectSocket", "Disconnect")
    }
}
