package com.app.thefruitsspirit.fragments


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.app.thefruitsspirit.auth.TermsPrivacyActivity
import com.app.thefruitsspirit.databinding.FragmentSettingsBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.model.DeActiveAccountResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.utils.showSuccessAlert
import com.app.thefruitsspirit.view_model.AuthVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(),
    Observer<Resource<DeActiveAccountResponse>> {
    private val settingVM by viewModels<AuthVM>()
    override val bindingInflater: (LayoutInflater) -> FragmentSettingsBinding
        get() {
            return FragmentSettingsBinding::inflate
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
    }

    private fun onClicks() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.rlNotification.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_notificationFragments)
        }

        binding.rlTerms.setOnClickListener {
            startActivity(Intent(requireContext(), TermsPrivacyActivity::class.java).apply {
                putExtra("value", "0")
            })
        }

        binding.rlPrivacyPolicy.setOnClickListener {
            startActivity(Intent(requireContext(), TermsPrivacyActivity::class.java).apply {
                putExtra("value", "1")
            })
        }

        binding.rlDeActivate.setOnClickListener {
            deActiveAccountDialog()
        }
    }
    private fun deActiveAccountDialog() {
        var reviewDialog = Dialog(requireContext())
        reviewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        reviewDialog.setContentView(R.layout.dialog_de_activated)
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
            deActiveApi()
        }
        dialogNo.setOnClickListener {
            reviewDialog.dismiss()
        }
        reviewDialog.show()
    }

    private fun deActiveApi() {
        settingVM.deActiveApi(requireActivity()).observe(requireActivity(), this)
    }
    override fun onChanged(value: Resource<DeActiveAccountResponse>) {
        when (value?.status) {
            Status.SUCCESS -> {
                showSuccessAlert(requireActivity(), value.data?.message!!)
                startActivity(Intent(requireContext(),LoginActivity::class.java))
            }

            Status.ERROR -> {
                showErrorAlert(requireActivity(), value.message!!)
            }

            Status.LOADING -> {

            }

            null -> {

            }
        }
    }
}