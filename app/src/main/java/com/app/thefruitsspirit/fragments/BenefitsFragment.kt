package com.app.thefruitsspirit.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.eazy.base.BaseFragment
import com.app.thefruitsspirit.adapter.VegetableBenefitsAdapter
import com.app.thefruitsspirit.auth.LoginActivity
import com.app.thefruitsspirit.databinding.FragmentBenefitsBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.model.BenfitsResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.isGone
import com.app.thefruitsspirit.utils.isVisible
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.view_model.AuthVM
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class BenefitsFragment : BaseFragment<FragmentBenefitsBinding>(),
    Observer<Resource<BenfitsResponse>> {
    private lateinit var adapter: VegetableBenefitsAdapter
    private val benefitsList = ArrayList<BenfitsResponse.Body>()
    private val benefitsVM by viewModels<AuthVM>()
    override val bindingInflater: (LayoutInflater) -> FragmentBenefitsBinding
        get() {
            return FragmentBenefitsBinding::inflate
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBenefitsApi()

    }

    private fun getBenefitsApi() {
        benefitsVM.getBenefits(requireActivity()).observe(requireActivity(),this)
    }
    private fun setAdapter() {
        adapter = VegetableBenefitsAdapter(requireContext(), benefitsList)
        binding.rvBenefits.adapter = adapter
    }

    override fun onChanged(value: Resource<BenfitsResponse>) {
        benefitsList.clear()
        when (value.status) {
            Status.SUCCESS -> {
                if (!value.data?.body.isNullOrEmpty()) {
                    benefitsList.addAll(value.data?.body!!)
                    binding.tvNoResultFound.isGone()
                    binding.rvBenefits.isVisible()
                    setAdapter()
                } else {
                    binding.rvBenefits.isGone()
                    binding.tvNoResultFound.isVisible()
                }
            }
            Status.ERROR -> {
                Log.e("error", value.message.toString())
                showErrorAlert(requireActivity(), value.message.toString())
                if (value.message == "Please Login First"){
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }
            }
            Status.LOADING -> {
                Log.e("error", value.message.toString())
            }
        }
    }
}