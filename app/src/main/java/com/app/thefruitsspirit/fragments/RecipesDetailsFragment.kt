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
import com.app.thefruitsspirit.databinding.FragmentRacipesDetailsBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.model.OneProductBenefitsResponse
import com.app.thefruitsspirit.model.RecipiesProductResponse
import com.app.thefruitsspirit.model.ResourceOneProductResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.view_model.AuthVM
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesDetailsFragment : BaseFragment<FragmentRacipesDetailsBinding>(),
    Observer<Resource<OneProductBenefitsResponse>> {
    private val productDetailsVM by viewModels<AuthVM>()
    var id = ""
    var type = ""
    var image = ""
    override val bindingInflater: (LayoutInflater) -> FragmentRacipesDetailsBinding
        get() {
            return FragmentRacipesDetailsBinding::inflate
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getString("id").toString()

        type = arguments?.getString("type").toString()
        image = arguments?.getString("image").toString()
        onClicks()

        when (type) {
            "2" -> {
                recipesApi()
                binding.tvHint.text = "Recipes Details"
            }
            "4" -> {
                getResourceApi()

                Glide.with(requireContext()).load(ApiConstants.PRODUCT_IMAGE+image).apply(
                    RequestOptions.placeholderOf(
                        R.drawable.placeholder_img
                    )
                ).into(binding.ivImage)

                binding.tvHint.text = "Resource"
            }

            else -> {
                getProductApi()
                binding.tvHint.text = "Benefit"
            }
        }


    }

    private fun getResourceApi() {
        productDetailsVM.resourceProduct(id, requireActivity()).observe(requireActivity(), resorceProduct)
    }

    private fun recipesApi() {
        productDetailsVM.recipesProduct(id, requireActivity())
            .observe(requireActivity(), recipesProduct)
    }

    private fun getProductApi() {
        productDetailsVM.getOneProductBenefitsApi(id, requireActivity())
            .observe(requireActivity(), this)
    }

    private fun onClicks() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onChanged(value: Resource<OneProductBenefitsResponse>) {
        when (value.status) {
            Status.SUCCESS -> {
                binding.apply {
                    if (!value.data?.body?.image.isNullOrEmpty()) {
                        Glide.with(requireActivity())
                            .load(ApiConstants.PRODUCT_IMAGE + value.data?.body?.image)
                            .placeholder(R.drawable.placeholder_img)
                            .into(binding.ivImage)
                    }
                    tvVegName.text = value.data?.body?.title
                    tvDescription.text = value.data?.body?.description
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

    private var recipesProduct = Observer<Resource<RecipiesProductResponse>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.apply {
                    if (!it.data?.body?.image.isNullOrEmpty()) {
                        Glide.with(requireActivity())
                            .load(ApiConstants.PRODUCT_IMAGE + it.data?.body?.image)
                            .placeholder(R.drawable.placeholder_img)
                            .into(binding.ivImage)
                    }
                    tvVegName.text = it.data?.body?.title
                    tvDescription.text = it.data?.body?.description
                }
            }

            Status.ERROR -> {
                showErrorAlert(requireActivity(), it.message!!)
            }

            Status.LOADING -> {

            }
        }
    }


    private var resorceProduct = Observer<Resource<ResourceOneProductResponse>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding.apply {
                    tvVegName.text = it.data?.body?.title
                    tvDescription.text = it.data?.body?.description
                }
            }

            Status.ERROR -> {
                showErrorAlert(requireActivity(), it.message!!)
            }

            Status.LOADING -> {

            }
        }

    }
}
