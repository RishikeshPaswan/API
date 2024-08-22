package com.app.thefruitsspirit.fragments


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.eazy.base.BaseFragment
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.auth.LoginActivity
import com.app.thefruitsspirit.cache.clearAllData
import com.app.thefruitsspirit.cache.clearData
import com.app.thefruitsspirit.constants.ApiConstants
import com.app.thefruitsspirit.databinding.FragmentMyAccountBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.model.CommanResponse
import com.app.thefruitsspirit.model.LogOutResponse
import com.app.thefruitsspirit.model.ProfileResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.clearPreferences
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.view_model.AuthVM
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAccountFragment : BaseFragment<FragmentMyAccountBinding>(),
    Observer<Resource<ProfileResponse>> {
    private val profileVM by viewModels<AuthVM>()
    override val bindingInflater: (LayoutInflater) -> FragmentMyAccountBinding
        get() {
            return FragmentMyAccountBinding::inflate
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
        profileApi()
    }

    private fun onClick() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.rlProfile.setOnClickListener {
            findNavController().navigate(R.id.action_myAccountFragment_to_myProfile)
        }
        binding.rlSetting.setOnClickListener {
            findNavController().navigate(R.id.action_myAccountFragment_to_settingFragment)
        }
        binding.rlFaqs.setOnClickListener {
            findNavController().navigate(R.id.action_myAccountFragment_to_faqsFragment)
        }
        binding.rlGetSupport.setOnClickListener {
            findNavController().navigate(R.id.action_myAccountFragment_to_getSupportFragment)
        }
        binding.rlLogOut.setOnClickListener {
            logOutDialog()
        }

        binding.ivImage.setOnClickListener {
            findNavController().navigate(R.id.action_myAccountFragment_to_myProfile)
        }
    }

    private fun profileApi() {
        profileVM.getProfile(requireActivity()).observe(requireActivity(), this)
    }

    private fun logOutDialog() {
        var reviewDialog = Dialog(requireContext())
        reviewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        reviewDialog.setContentView(R.layout.dialog_logout)
        reviewDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        reviewDialog.setCancelable(true)
        reviewDialog.setCanceledOnTouchOutside(true)
        reviewDialog.window!!.setGravity(Gravity.CENTER)
        reviewDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDone = reviewDialog.findViewById<TextView>(R.id.btnYes)
        val dialogNo = reviewDialog.findViewById<TextView>(R.id.btnNo)
        dialogDone.setOnClickListener {
            reviewDialog.dismiss()
            logOutApi()
        }
        dialogNo.setOnClickListener {
            reviewDialog.dismiss()
        }
        reviewDialog.show()
    }

    private fun logOutApi() {
        profileVM.logOutApi(requireActivity()).observe(requireActivity(), logOutAPI)
    }

    override fun onChanged(value: Resource<ProfileResponse>) {
        when (value.status) {
            Status.SUCCESS -> {
                binding.apply {
                    if (!value.data?.body?.image.isNullOrEmpty()) {
                        Glide.with(requireActivity())
                            .load(ApiConstants.BASE_URL_IMAGE + value.data?.body?.image)
                            .placeholder(R.drawable.placeholder_img)
                            .into(binding.ivImage)
                    }
                    tvName.text = value.data?.body?.name
                    tvEmail.text = value.data?.body?.email
                    tvPhone.text = value.data?.body?.phoneNumber.toString()
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

    private var logOutAPI = Observer<Resource<LogOutResponse>> {
        when (it.status) {
            Status.SUCCESS -> {
                clearPreferences()
                clearAllData(requireContext())
                clearData(requireContext(), "token")
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                activity?.finishAffinity()
            }
            Status.ERROR -> {
                showErrorAlert(requireActivity(), it.message!!)
            }

            Status.LOADING -> {

            }
        }
    }
}