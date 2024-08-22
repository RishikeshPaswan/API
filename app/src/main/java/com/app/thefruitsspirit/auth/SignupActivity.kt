package com.app.thefruitsspirit.auth


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.cache.saveLoginPreference
import com.app.thefruitsspirit.cache.saveToken
import com.app.thefruitsspirit.databinding.ActivitySignupBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.genricdatacontainer.ValidateData
import com.app.thefruitsspirit.model.SignUpResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.createRequestBody
import com.app.thefruitsspirit.utils.prepareMultiPart
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.view_model.AuthVM
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.app.thefruitsspirit.base.LocationUpdateUtility
import com.app.thefruitsspirit.utils.showSuccessAlert
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.Arrays
import java.util.Locale

@AndroidEntryPoint
class SignupActivity : LocationUpdateUtility<ActivitySignupBinding>(),
    Observer<Resource<SignUpResponse>> {
    private var isClick = true
    private var image = ""
    var latitudeG = ""
    var longitudeG = ""
    var latCurrent = ""
    var lngCurrent = ""
    private var device_token = ""

    private var locationS: String? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private var launcher: ActivityResultLauncher<Intent>? = null
    private val signUpVM by viewModels<AuthVM>()
    override val bindingInflater: (LayoutInflater) -> ActivitySignupBinding
        get() {
            return ActivitySignupBinding::inflate
        }

    override fun setup() {
        requestCurrentLocation(this)
        if (!Places.isInitialized()) {
            Places.initialize(this, resources.getString(R.string.map_key))
        }
        onClick()
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == AutocompleteActivity.RESULT_OK) {
                    val place: Place? =
                        result.data?.let { Autocomplete.getPlaceFromIntent(it) }
                    place?.let {
                        setPlaceData(it)
                    }
                }

            }
    }

    private fun onClick() {
        binding.ivUnselect.setOnClickListener {
            if (isClick) {
                isClick = false
                binding.ivUnselect.setImageResource(R.drawable.select_signup)
            } else {
                isClick = true
                binding.ivUnselect.setImageResource(R.drawable.unselect_signup)
            }

        }
        binding.tvLogin.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.ivImage.setOnClickListener {
            askStorageManagerPermission(this@SignupActivity, 0, false)
        }

        binding.tvTermsCondition.setOnClickListener {
            startActivity(Intent(this, TermsPrivacyActivity::class.java).apply {
                putExtra("value", "0")
            })
        }
        binding.edtLocation.setOnClickListener {
            openPlacePicker()
        }

        binding.btnSignUp.setOnClickListener {
            if (ValidateData.signUpValidation(
                    this,
                    image,
                    binding.edtName.text.toString().trim(),
                    binding.edtEmail.text.toString().trim(),
                    binding.ccplogin.toString().trim(),
                    binding.edtPhoneNumber.text.toString().trim(),
                    binding.edtLocation.text.toString().trim(),

                    )
            ) {
                if (!isClick) {
                    signUPApi()
                } else {
                    showErrorAlert(this, "Please select Terms & Conditions")
                }
            }
        }
    }

    private fun openPlacePicker() {
        //        MY_REQUEST = 1
        val fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS
        )
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this)
        launcher?.launch(intent)
    }

    private fun setPlaceData(it: Place) {
        binding.edtLocation.setText(it.address.toString())
        it.latLng?.let {
            latitudeG = it.latitude.toString()
        }
        it.latLng?.let {
            longitudeG = it.longitude.toString()
        }

        Log.d("latng", "lat $latitude long $longitude")
        getLocationDetails()
    }

    private fun getLocationDetails() {
        val addresses: List<Address>
        val geocoder = Geocoder(this, Locale.getDefault())

        if (latitudeG.isNotEmpty() && longitudeG.isNotEmpty()) {

            Log.d(ContentValues.TAG, "getLocationDetails: $latitudeG $longitudeG")

            addresses = geocoder.getFromLocation(
                latitudeG.toDouble(), longitudeG.toDouble(), 1
            ) as List<Address> // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            val address: String =
                addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            var postalCode = ""
            val city: String = addresses[0].locality
            val state: String = addresses[0].adminArea
            val country: String = addresses[0].countryName
//            if (addresses[0].postalCode != null) {
//                postalCode = addresses[0].postalCode
//            }
            val knownName: String = addresses[0].featureName // Only if available else return NULL

            binding.edtLocation.setText("$city " + "$state " + "$country")
//            binding.edtLocation.setText("$city ")
            locationS = address
            Log.d(ContentValues.TAG, "getLocationDetails111: $locationS")
        } else {
            showErrorAlert(this, "unable to find location")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun currentLocationDetails() {
        val addresses: List<Address>
        val geocoder = Geocoder(this, Locale.getDefault())

        if (latCurrent.isNotEmpty() && lngCurrent.isNotEmpty()) {

            Log.d(ContentValues.TAG, "getLocationDetails: $latCurrent $lngCurrent")

            addresses = geocoder.getFromLocation(
                latCurrent.toDouble(), lngCurrent.toDouble(), 1
            ) as List<Address> // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            val address: String =
                addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            var postalCode = ""
            val city: String = addresses[0].locality
            val state: String = addresses[0].adminArea
            val country: String = addresses[0].countryName
//            if (addresses[0].postalCode != null) {
//                postalCode = addresses[0].postalCode
//            }
            val knownName: String = addresses[0].featureName // Only if available else return NULL

            binding.edtLocation.setText("$city " + "$state " + "$country ")
//            binding.edtLocation.setText("$city ")
            locationS = address
            Log.d(ContentValues.TAG, "getLocationDetails111: $locationS")
        } else {
            showErrorAlert(this, "unable to find location")
        }
    }

    private fun signUPApi() {
        binding.also {
            val map: HashMap<String, RequestBody> = HashMap()
            map["name"] = createRequestBody(it.edtName.text.toString().trim())
            map["email"] = createRequestBody(it.edtEmail.text.toString().trim())
            map["countryCode"] = createRequestBody(it.ccplogin.selectedCountryCode)
            map["phoneNumber"] = createRequestBody(it.edtPhoneNumber.text.toString().trim())
            map["location"] = locationS?.let { it1 -> createRequestBody(it1) }!!
            map["deviceToken"] = createRequestBody(device_token)
            map["deviceType"] = createRequestBody("0")

            if (longitudeG.isEmpty() && latitudeG.isEmpty()) {
                map["logitude"] = createRequestBody(lngCurrent)
                map["latitude"] = createRequestBody(latCurrent)
            } else {
                map["logitude"] = createRequestBody(longitudeG)
                map["latitude"] = createRequestBody(latitudeG)
            }
            val images: MultipartBody.Part?
            images = prepareMultiPart("image", File(image))
            signUpVM.userSignUp(map, images, this).observe(this, this)
        }
    }

    override fun selectedImage(imagePath: String?, code: Int) {
        image = imagePath.toString()
        Glide.with(this).load(imagePath).into(binding.ivImage)
    }

    override fun onChanged(value: Resource<SignUpResponse>) {
        when (value?.status) {
            Status.SUCCESS -> {
                saveToken(this, value.data?.body?.token.toString())
                saveLoginPreference("otp", value.data?.body!!.otp.toString())
                saveLoginPreference("user_name", value.data?.body!!.name.toString())
                saveLoginPreference("is_verify", value.data?.body!!.otpVerified.toString())
                saveLoginPreference("user_location", value.data?.body!!.location?.locationName.toString())
                saveLoginPreference("user_location", binding.edtLocation.text.toString())
                showSuccessAlert(this,  value.data?.body!!.location?.locationName.toString())
                startActivity(Intent(this, OtpVerificationActivity::class.java).apply {
                    putExtra("phone", binding.edtPhoneNumber.text.toString().trim())
                    putExtra("ccp", binding.ccplogin.selectedCountryCode)
                    putExtra("email", value.data?.body?.email)
                })
            }

            Status.ERROR -> {
                showErrorAlert(this, value.message!!)
            }

            Status.LOADING -> {
//                binding.pb.isVisible()
            }

            null -> {
                showErrorAlert(this, value.data?.message!!)
            }
        }
    }

    override fun updatedLatLng(lat: Double, lng: Double) {
        latCurrent = lat.toString()
        lngCurrent = lng.toString()

        Log.d("jhsfjhksffd", "" + latCurrent)
        Log.d("jhsfjhksffd", "" + lngCurrent)

        currentLocationDetails()
    }

    override fun liveLatLng(lat: Double, lng: Double) {

    }

    private fun getDeviceToken() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("DEVICE_TOKEN", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result
                Log.e("DEVICE_TOKEN", token.toString())
                device_token = token.toString()
                Log.d("jksdjksdmlsfdmlsd", "gfnbghhg" + device_token)

            })
        } catch (e: Exception) {
            Log.d("DEVICE_TOKEN_ERROR", "getDeviceToken: " + e.message)
        }
    }
}