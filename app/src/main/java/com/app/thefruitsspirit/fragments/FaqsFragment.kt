package com.app.thefruitsspirit.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.eazy.base.BaseFragment
import com.app.thefruitsspirit.adapter.FaqsAdapter
import com.app.thefruitsspirit.databinding.FragmentFaqsBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.model.FaqsResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.isGone
import com.app.thefruitsspirit.utils.isVisible
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.view_model.AuthVM
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class FaqsFragment : BaseFragment<FragmentFaqsBinding>(),
    Observer<Resource<FaqsResponse>> {
    private lateinit var adapter: FaqsAdapter
    private val faqsList = ArrayList<FaqsResponse.Body>()

    private val faqsVM by viewModels<AuthVM>()
    override val bindingInflater: (LayoutInflater) -> FragmentFaqsBinding
        get(){
            return FragmentFaqsBinding::inflate
        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        onClicks()
        faqsApi()
    }
    private fun faqsApi() {
        faqsVM.getFaqsApi(requireActivity()).observe(requireActivity(), this)
    }

    private fun onClicks() {
       binding.ivBack.setOnClickListener {
           findNavController().popBackStack()
       }
    }
    private fun setAdapter() {
        adapter = FaqsAdapter(requireContext(), faqsList)
        binding.rvFaqs.adapter = adapter
    }
    override fun onChanged(value: Resource<FaqsResponse>) {
        faqsList.clear()
        when (value.status) {
            Status.SUCCESS -> {
                if (!value.data?.body.isNullOrEmpty()) {
                    faqsList.addAll(value.data?.body!!)
                    faqsList.reversed()
                    binding.tvNoResultFound.isGone()
                    binding.rvFaqs.isVisible()
                    setAdapter()
                } else {
                    binding.rvFaqs.isGone()
                    binding.tvNoResultFound.isVisible()
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