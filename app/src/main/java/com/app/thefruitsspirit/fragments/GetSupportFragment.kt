package com.app.thefruitsspirit.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.eazy.base.BaseFragment
import com.app.thefruitsspirit.databinding.FragmentGetSupportBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.genricdatacontainer.ValidateData
import com.app.thefruitsspirit.model.GetSupportResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.createRequestBody
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.utils.showSuccessAlert
import com.app.thefruitsspirit.view_model.AuthVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetSupportFragment : BaseFragment<FragmentGetSupportBinding>(),
    Observer<Resource<GetSupportResponse>> {
    private val getSupportVM by viewModels<AuthVM>()
    override val bindingInflater: (LayoutInflater) -> FragmentGetSupportBinding
        get() {
            return FragmentGetSupportBinding::inflate
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()

    }
    private fun onClicks() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSubmit.setOnClickListener {
            if (ValidateData.contactUsValidation(
                    requireActivity(),
                    binding.edtName.text.toString().trim(),
                    binding.edtEmail.text.toString().trim(),
                    binding.ccplogin.toString().trim(),
                    binding.edtPhoneNumber.text.toString().trim(),
                    binding.edtMessage.text.toString().trim()
                )
            ) {
                supportContactApi()
            }
        }
    }
    private fun supportContactApi() {
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["name"] = binding.edtName.text.toString().trim()
        hashMap["email"] = binding.edtEmail.text.toString().trim()
        hashMap["CountryCode"] = binding.ccplogin.selectedCountryCode
        hashMap["phoneNumber"] = binding.edtPhoneNumber.text.toString().trim()
        hashMap["message"] = binding.edtMessage.text.toString().trim()
        getSupportVM.getSupportApi(hashMap,requireActivity()).observe(viewLifecycleOwner, this)
    }

    override fun onChanged(value: Resource<GetSupportResponse>) {
        when (value?.status) {
            Status.SUCCESS -> {
                showSuccessAlert(requireActivity(), value.data?.message!!)
                findNavController().popBackStack()
            }
            Status.ERROR -> {
                showErrorAlert(requireActivity(), value.message!!)
            }
            Status.LOADING -> {
            }
            null -> {
                showErrorAlert(requireActivity(), value?.message!!)
            }


        }
    }

}