package com.app.thefruitsspirit.fragments


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.base.ImagePickerUtility
import com.app.thefruitsspirit.constants.ApiConstants
import com.app.thefruitsspirit.databinding.FragmentEditProfileBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.genricdatacontainer.ValidateData
import com.app.thefruitsspirit.model.EditProfileResponse
import com.app.thefruitsspirit.model.ProfileResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.createRequestBody
import com.app.thefruitsspirit.utils.prepareMultiPart
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.utils.showSuccessAlert
import com.app.thefruitsspirit.view_model.AuthVM
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.URL

@AndroidEntryPoint
class EditProfileFragment : ImagePickerUtility<FragmentEditProfileBinding>(),
    Observer<Resource<EditProfileResponse>> {
    var image = ""
    var imageFile:File?=null
    var images: MultipartBody.Part?=null
    var model : ProfileResponse.Body? =null
    private val editProfileVM by viewModels<AuthVM>()
    override val bindingInflater: (LayoutInflater) -> FragmentEditProfileBinding
        get() {
            return FragmentEditProfileBinding::inflate
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            model = arguments?.getSerializable("data") as ProfileResponse.Body
        }catch (e:Exception){}


        binding.apply {
            (edtName as TextView).text = model?.name.toString()
            (edtEmail as TextView).text = model?.email.toString()
            (edtPhoneNumber as TextView).text = model?.phoneNumber.toString()
            Glide.with(requireActivity())
                .load(ApiConstants.BASE_URL_IMAGE +model?.image)
                .placeholder(R.drawable.placeholder_img)
                .into(binding.ivProfile)
        }
        onClicks()
    }

    private fun onClicks() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSubmit.setOnClickListener {
            if (ValidateData.editValidation(
                    requireActivity(),
                    image,
                    binding.edtName.text.toString().trim(),
                    binding.ccplogin.toString().trim(),
                    binding.edtPhoneNumber.text.toString().trim(),
                )
            ) {
                editProfileApi()
            }
        }
        binding.ivProfile.setOnClickListener {
            askStorageManagerPermission(requireActivity(), 0, false)
        }
    }
    private fun editProfileApi() {
        binding.also {
            val map: HashMap<String, RequestBody> = HashMap()
            map["name"] = createRequestBody(it.edtName.text.toString().trim())
            map["countryCode"] = createRequestBody(it.ccplogin.selectedCountryCode)
            map["phoneNumber"] = createRequestBody(it.edtPhoneNumber.text.toString().trim())
//            val images: MultipartBody.Part?
//            images = prepareMultiPart("image", File(image))
            editProfileVM.editProfile(map, images, requireActivity()).observe(requireActivity(), this)
        }
    }

    private fun getBitmapFromUrl() {
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = getBitmap()
            withContext(Dispatchers.Main) {
                var imageBitMap=bitmap
                persistImage(imageBitMap!!,"pro_img")
            }
        }
    }

    private fun persistImage(bitmap: Bitmap, name: String) {
        val filesDir: File = requireContext().applicationContext.filesDir
        imageFile = File(filesDir, "$name.jpg")
        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
            images=prepareMultiPart("image", imageFile)
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error writing bitmap", e)
        }
    }

    private fun getBitmap(): Bitmap {
        val imageUrl = image
        val url = URL(imageUrl)
        return BitmapFactory.decodeStream(url.openConnection().getInputStream())
    }

    override fun selectedImage(imagePath: String?, code: Int) {
        image = imagePath.toString()
        images=prepareMultiPart("image", File(image))
        Glide.with(this).load(imagePath).into(binding.ivProfile)
    }
    override fun onChanged(value: Resource<EditProfileResponse>) {
        when (value?.status) {
            Status.SUCCESS -> {
                image=ApiConstants.BASE_URL_IMAGE + value.data?.body?.image
                getBitmapFromUrl()
                showSuccessAlert(requireActivity(), "User profile has been updated successfully")
                findNavController().popBackStack()
            }

            Status.ERROR -> {
                showErrorAlert(requireActivity(), value.message!!)
            }

            Status.LOADING -> {

            }
            null -> {
                showErrorAlert(requireActivity(), value.data?.message!!)
            }
        }
    }




}