package com.app.thefruitsspirit.base

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationListener
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.datatransport.BuildConfig.APPLICATION_ID
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*


abstract class LocationUpdateUtility<DB : ViewBinding> : ImagePickerActivityUtility<DB>(), LocationListener {

    private val TAG = "LocationUpdateUtility"
    private lateinit var activity: Activity

/*
    private var cancellationTokenSource = CancellationTokenSource()
    private val MY_PERMISSION_FINE_LOCATION = 101

    var lat = ""
    var lng = ""
    var curentlat = ""
    var currentlng = ""
*/

    private var locationRequest: LocationRequest? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var latitude=0.0
    var longitude=0.0

    override fun onLocationChanged(location: Location) {

        try {
            updatedLatLng(location!!.latitude, location.longitude)
        } catch (e: Exception) {
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.isNotEmpty()) {
                permissions.entries.forEach {
                    Log.d(TAG, "${it.key} = ${it.value}")
                }

                val fineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION]
                val coarseLocation = permissions[Manifest.permission.ACCESS_COARSE_LOCATION]

                if (fineLocation == true && coarseLocation == true) {
                    Log.e(TAG, "Permission Granted Successfully")
                    checkGpsOn()
                } else {
                    Log.e(TAG, "Permission not granted")
                    checkPermissionDenied(permissions.keys.first())
                }
            }
        }

    private val gpsOnLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                Log.e(TAG, "GPS Turned on successfully")
                startLocationUpdates()
            } else if (result.resultCode == RESULT_CANCELED) {
                Log.e(TAG, "GPS Turned on failed")
                locAlertDialogMethod()

            }
        }

    private fun locAlertDialogMethod() {
        val locationDialog = Dialog(this)
        locationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        locationDialog.setContentView(com.app.thefruitsspirit.R.layout.location_alert)

        locationDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        locationDialog.setCancelable(true)
        locationDialog.setCanceledOnTouchOutside(true)
        locationDialog.window!!.setGravity(Gravity.CENTER)

        var btnTryAgain = locationDialog.findViewById<Button>(com.app.thefruitsspirit.R.id.btnTryAgain)

        locationDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnTryAgain.setOnClickListener {
            locationDialog.dismiss()
            checkGpsOn()
        }
        locationDialog.show()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    open fun requestCurrentLocation(activity: Activity) {
        this.activity = activity

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        checkLocationPermissions()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    // ...
                    Log.e(TAG, "==========" + location.latitude.toString() + ", " + location.longitude + "=========")

                    //  if (latitude==0.0){

                    //  latitude=location.latitude
                    //   longitude=location.longitude
                    updatedLatLng(location.latitude, location.longitude)
                    // }else{

                    stopLocationUpdates()
                    //liveLatLng(location.latitude, location.longitude)
                    // }
                }
            }
        }
    }

    /* override fun onRequestPermissionsResult(
         requestCode: Int,
         permissions: Array<String>,
         grantResults: IntArray
     ) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         when (requestCode) {
             MY_PERMISSION_FINE_LOCATION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//permission to access location grant
                 if (ContextCompat.checkSelfPermission(
                         mActivity,
                         Manifest.permission.ACCESS_FINE_LOCATION
                     ) == PackageManager.PERMISSION_GRANTED
                 ) {
                     requestCurrentLocation(mActivity)

                 }
             }
             //permission to access location denied
             else {
                 Toast.makeText(
                     mActivity,
                     "This app requires location permissions to be granted",
                     Toast.LENGTH_LONG
                 ).show()
                 //  finish()
             }
         }
     }

     private fun displayLocationSettingsRequest() {
         *//* val googleApiClient = GoogleApiClient.Builder(this)
                 .addApi(LocationServices.API).build();
         googleApiClient.connect();*//*
        val locationRequest = LocationRequest.create();
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
        locationRequest.interval = 10000;
        locationRequest.fastestInterval = 10000 / 2;

        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        //.addLocationRequest(mLocationRequestBalancedPowerAccuracy)
        // val  builder =  LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        // builder.setAlwaysShow(true);
        builder.setNeedBle(true);
        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(mActivity)
                .checkLocationSettings(builder.build())
        // val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.addOnCompleteListener { task ->
            try {
                var response = task.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.

            } catch (exception: ApiException) {
                var status = exception.statusCode
                when (status) {

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            //startIntentSenderForResult(status.getResolution().getIntentSender(), REQUEST_ID_GPS_PERMISSIONS, null, 0, 0, 0, null);

                            resolvable.startResolutionForResult(mActivity, 1111)
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        Log.i(
                            "TAG",
                            "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                        );
                    }
                }
            }
        }
    }

    open fun requestCurrentLocation(activity: Activity) {
        mActivity = activity
        // Check Fine permission
         val fusedLocationClient: FusedLocationProviderClient by lazy {
            LocationServices.getFusedLocationProviderClient(activity)
        }

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val manager =
                activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (!statusOfGPS) {
                displayLocationSettingsRequest()
            } else {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {

                            curentlat = location.latitude.toString()
                            currentlng = location.longitude.toString()
                            // getBarberListApi()
                            updatedLatLng(location.latitude, location.longitude)
                        } else
                            requestCurrentLocation(mActivity)
                    }
            }

            val currentLocationTask: Task<Location> = fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )
            currentLocationTask.addOnCompleteListener { task: Task<Location> ->
                val result = if (task.isSuccessful) {
                    if (curentlat.isEmpty() && currentlng.isEmpty()) {
                        val result = task.result
                        curentlat = result.latitude.toString()
                        currentlng = result.longitude.toString()
                        "Location (success): ${result.latitude}, ${result.longitude}"
                        Log.d(
                            "Location",
                            "Location (success): ${result.latitude}, ${result.longitude}"
                        )

                        updatedLatLng(result.latitude, result.longitude)
                    } else {
                        val result = task.result
                        Log.d(
                            "Location",
                            "Location (success): ${result.latitude}, ${result.longitude}"
                        )
                    }

                } else {
                    val exception = task.exception
                    "Location (failure): ${exception.toString()}"
                    Log.d("Location", "getCurrentLocation() result: ${exception.toString()}")
                }
                Log.d("Location", "getCurrentLocation() result: ${result}")

            }
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                toast("please add permisson")
            // show UI part if you want here to show some rationale !!!
            else
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_FINE_LOCATION
                )
            // Request fine location permission (full code below).
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            val status = data!!.getStringExtra("status")
            if (status!!.equals("true", ignoreCase = true)) {
                val msg = data.getStringExtra("MESSAGE")
                val lat = data.getDoubleExtra("lat", 0.0)
                val aLong = data.getDoubleExtra("long", 0.0)
                this.lat = lat.toString()
                lng = aLong.toString()

                *//*       AppConstants.LATITUDE = lat
                       AppConstants.LONGITUDE = aLong*//*


                *//* savePreference(AppConstants.LATITUDE,lat.toString())
                 savePreference(AppConstants.LONGITUDE,aLong.toString())*//*

                updatedLatLng(lat, aLong)
            } else {

                val msg = data.getStringExtra("MESSAGE")
                toast(msg!!)

            }

        }
        if (requestCode == 1111)
            requestCurrentLocation(mActivity)
    }
*/

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkLocationPermissions() {

        if (hasPermissions(permissions)) {
            Log.e(TAG, "Permissions Granted")
            // getLiveLocation(requireActivity())
            checkGpsOn()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            checkPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            checkPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            Log.e(TAG, "Request for Permissions")
            requestPermission()
        }
    }

    // util method
    private fun hasPermissions(permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        requestMultiplePermissions.launch(permissions)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissionDenied(permission: String) {
        if (shouldShowRequestPermissionRationale(permission)) {
            Log.e(TAG, "Permissions Denied")
            val mBuilder = AlertDialog.Builder(activity)
            val dialog: AlertDialog =
                mBuilder.setTitle(com.app.thefruitsspirit.R.string.alert)
                    .setMessage(com.app.thefruitsspirit.R.string.permissionRequired)
                    .setPositiveButton(com.app.thefruitsspirit.R.string.ok) { dialog, which ->
                        // Request permission
                        requestPermission()
                    }.create()
            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(
                        activity, com.app.thefruitsspirit.R.color.app_color
                    )
                )
            }
            dialog.show()


        } else {
            val builder = AlertDialog.Builder(activity)
            val dialog: AlertDialog =
                builder.setTitle(com.app.thefruitsspirit.R.string.alert)
                    .setMessage(com.app.thefruitsspirit.R.string.permission_denied_explanation)
                    .setCancelable(
                        false
                    )
                    .setPositiveButton("Settings") { dialog, which ->
                        // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            APPLICATION_ID,
                            null)
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }.create()
            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(
                        activity, com.app.thefruitsspirit.R.color.app_color
                    )
                )
            }
            dialog.show()
//            locAlertDialogMethod()
        }
    }

    private fun locationPermission(permissions: Array<String>): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity,
            permissions[0]
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            activity,
            permissions[1]
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkGpsOn() {
        locationRequest = LocationRequest.create()
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest?.interval = 500000000
        locationRequest?.fastestInterval = 2000000000

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        builder.setAlwaysShow(true)

        val result =
            LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build())
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                Log.e(TAG, "==========GPS is ON=============")

                startLocationUpdates()
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                      val resolvable = ResolvableApiException(e.status)

                       // val resolvableApiException = e as ResolvableApiException
                        gpsOnLauncher.launch(IntentSenderRequest.Builder(resolvable.resolution).build())
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    //call startLocationUpdates() method for start live location update
    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            hasPermissions(permissions)
            return
        }

        locationRequest?.let {
            fusedLocationClient.requestLocationUpdates(
                it,
                locationCallback,
                Looper.getMainLooper()
            )
        }
        Log.e(TAG, "Get Live Location Start")
    }

    //call stopLocationUpdates() method for stop live location update
    fun stopLocationUpdates() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            Log.e(TAG, "Get Live Location Stop")
        } catch (e: Exception) {
        }
    }

    abstract fun updatedLatLng(lat: Double, lng: Double)
    abstract fun liveLatLng(lat: Double, lng: Double)
}