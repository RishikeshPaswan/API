package com.app.thefruitsspirit.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.eazy.base.BaseFragment
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.constants.ApiConstants
import com.app.thefruitsspirit.databinding.FragmentMyProfileBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.model.ProfileResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.view_model.AuthVM
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(),
    Observer<Resource<ProfileResponse>> {
    private val profileVM by viewModels<AuthVM>()
    var model: ProfileResponse.Body? = null
    override val bindingInflater: (LayoutInflater) -> FragmentMyProfileBinding
        get() {
            return FragmentMyProfileBinding::inflate
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
        profileApi()
    }
    private fun profileApi() {
        profileVM.getProfile(requireActivity()).observe(requireActivity(), this)
    }
    private fun onClicks() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnEditProfile.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("data", model)
            findNavController().navigate(R.id.action_myProfile_to_editProfile, bundle)
        }
    }

    override fun onChanged(value: Resource<ProfileResponse>) {
        when (value.status) {
            Status.SUCCESS -> {
                model = value.data?.body
                binding.apply {
                    if (!value.data?.body?.image.isNullOrEmpty()) {
                        Glide.with(requireActivity())
                            .load(ApiConstants.BASE_URL_IMAGE + value.data?.body?.image)
                            .placeholder(R.drawable.placeholder_img)
                            .into(binding.ivProfile)
                    }
                    tvName.text = value.data?.body?.name
                    tvFirstName.text = value.data?.body?.name
                    tvEmailHint.text = value.data?.body?.email
                    tvPhoneNumber.text = value.data?.body?.phoneNumber.toString()
                }
            }

            Status.ERROR -> {
                Log.e("error", value.message.toString())
                showErrorAlert(requireActivity(), value.message.toString())
            }

            Status.LOADING -> {
                Log.e("error", value.message.toString())
            }

        }

    }

}