package com.app.thefruitsspirit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.eazy.base.BaseFragment
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.adapter.HomeAdapter
import com.app.thefruitsspirit.cache.getLoginPreference
import com.app.thefruitsspirit.constants.ApiConstants
import com.app.thefruitsspirit.databinding.FragmentHomeSeeAllBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.model.HomeResponse
import com.app.thefruitsspirit.model.OneProductBenefitsResponse
import com.app.thefruitsspirit.model.ProductConsumeResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.isGone
import com.app.thefruitsspirit.utils.isVisible
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.view_model.AuthVM
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class HomeSeeAllFragment : BaseFragment<FragmentHomeSeeAllBinding>(),
    Observer<Resource<HomeResponse>>, HomeAdapter.OnClick {
    private lateinit var adapter: HomeAdapter
    private val homeVM by viewModels<AuthVM>()
    var model1: OneProductBenefitsResponse.Body? = null
    var data = ""
    private val homeList = ArrayList<HomeResponse.Body>()
    override val bindingInflater: (LayoutInflater) -> FragmentHomeSeeAllBinding
        get() {
            return FragmentHomeSeeAllBinding::inflate
        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
        homeApi()
    }
    private fun onClicks() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun homeApi() {
        homeVM.getHomeApi(requireActivity()).observe(requireActivity(), this)
    }
    private fun setAdapter() {
        adapter = HomeAdapter(requireContext(), homeList, this)
        binding.rvHome.adapter = adapter
    }
    override fun onChanged(value: Resource<HomeResponse>) {
        homeList.clear()
        when (value.status) {
            Status.SUCCESS -> {
                if (!value.data?.body.isNullOrEmpty()) {
                    homeList.addAll(value.data?.body!!)
                    binding.tvNoResultFound.isGone()
                    binding.rvHome.isVisible()
                    setAdapter()
                    homeList.reverse()
                } else {
                    binding.rvHome.isGone()
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

    override fun click(position: Int, id: String) {
        data = id
        val dialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.consume_bottom_sheet, null)
        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnOpen = view.findViewById<TextView>(R.id.btnCharacteristies)
        val tvDone= view.findViewById<TextView>(R.id.tvDone)

        tvDone.text = "Congratulations "+ getLoginPreference("user_name","")

        homeVM.getOneProductBenefitsApi(data, requireActivity())
            .observe(requireActivity(), getBenefitApi)

        btnDone.setOnClickListener {
            dialog.dismiss()
            homeVM.consumeProductApi(id,requireActivity()).observe(requireActivity(),consumeProductApi)
        }
        btnOpen.setOnClickListener {
            dialog.dismiss()
            showBottom()
        }
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }
    private fun showBottom() {
        val dialogNew = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.detail_botom_sheet, null)
        val btnClose = view.findViewById<ImageView>(R.id.ivDown)

        val btnImage = view.findViewById<ImageView>(R.id.ivImage)
        val tvName = view.findViewById<TextView>(R.id.tvVegName)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)

        Glide.with(requireActivity())
            .load(ApiConstants.PRODUCT_IMAGE + model1?.image)
            .placeholder(R.drawable.placeholder_img)
            .into(btnImage)

        tvName.text = model1?.title
        tvDescription.text = model1?.description
        btnClose.setOnClickListener {
            dialogNew.dismiss()
        }
        dialogNew.setCancelable(false)
        dialogNew.setContentView(view)
        dialogNew.show()
    }

    private var consumeProductApi = Observer<Resource<ProductConsumeResponse>> {
        when (it.status) {
            Status.SUCCESS -> {

            }
            Status.ERROR -> {
                showErrorAlert(requireActivity(), it.message!!)
            }
            Status.LOADING -> {

            }
        }
    }

    private var getBenefitApi = Observer<Resource<OneProductBenefitsResponse>> {
        when (it.status) {
            Status.SUCCESS -> {
                model1 = it.data?.body
            }
            Status.ERROR -> {
                showErrorAlert(requireActivity(), it.message!!)
            }
            Status.LOADING -> {

            }
        }
    }



}