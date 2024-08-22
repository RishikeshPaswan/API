package com.app.thefruitsspirit.fragments


import android.content.Intent
import android.graphics.Color
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
import androidx.viewpager2.widget.ViewPager2
import com.app.eazy.base.BaseFragment
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.adapter.HomeAdapter
import com.app.thefruitsspirit.adapter.HomeViewPagerAdapter
import com.app.thefruitsspirit.auth.LoginActivity
import com.app.thefruitsspirit.cache.getLoginPreference
import com.app.thefruitsspirit.constants.ApiConstants
import com.app.thefruitsspirit.databinding.FragmentHomeBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.model.HomeResponse
import com.app.thefruitsspirit.model.OneProductBenefitsResponse
import com.app.thefruitsspirit.model.ProductConsumeResponse
import com.app.thefruitsspirit.model.WelcomeModel
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.isGone
import com.app.thefruitsspirit.utils.isVisible
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.view_model.AuthVM
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import dagger.hilt.android.AndroidEntryPoint
import java.text.FieldPosition

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(),
    Observer<Resource<HomeResponse>>, HomeAdapter.OnClick {
    private lateinit var adapter: HomeAdapter
    private val homeList = ArrayList<HomeResponse.Body>()
    private val homeVM by viewModels<AuthVM>()

    var model: ArrayList<HomeResponse.Body>? = null
    var model1: OneProductBenefitsResponse.Body? = null
    var data = ""
    var data1 = ""

    private val homeViewHolder by lazy { HomeViewPagerAdapter(requireContext(), bannerImage) }
    private var bannerImage = ArrayList<WelcomeModel>()
    override val bindingInflater: (LayoutInflater) -> FragmentHomeBinding
        get() {
            return FragmentHomeBinding::inflate
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("sjdankjsdajkad", "" + data)
        setAdapters()
        onClicks()
        homeApi()

        binding.tvLocation.text = getLoginPreference("user_location", "")

//        binding.viewPaser.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//                binding.indicatorView.onPageScrolled(position, positionOffset, positionOffsetPixels)
//            }
//        })
    }

    private fun homeApi() {
        homeVM.getHomeApi(requireActivity()).observe(requireActivity(), this)
    }

    private fun onClicks() {
        binding.ivUser.setOnClickListener {
            findNavController().navigate(R.id.myAccountFragment)
        }
        binding.ivBell.setOnClickListener {
            findNavController().navigate(R.id.notificationFragments)
        }
        binding.tvSelfCont.setOnClickListener {
          data =  homeList[homeList.size-1]._id.toString()

           showDialog()
        }

//        binding.tvSeeAll.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_seeAllFragments)
//        }
    }

    private fun setAdapters() {
        bannerImage.clear()
        bannerImage.add(
            WelcomeModel(
                R.drawable.app_imag,
                "I don't want to be at the mercy of my emotions. I want to use them, to enjoy them, and to dominate them.",
                "Welcome"
            )
        )
//        bannerImage.add(
//            WelcomeModel(
//                R.drawable.app_imag,
//                "I don't want to be at the mercy of my emotions. I want to use them, to enjoy them, and to dominate them.",
//                "Welcome"
//            )
//        )
//        bannerImage.add(
//            WelcomeModel(
//                R.drawable.app_imag,
//                "I don't want to be at the mercy of my emotions. I want to use them, to enjoy them, and to dominate them.",
//                "Welcome"
//            )
//        )

//        binding.viewPaser.adapter = homeViewHolder
//        binding.indicatorView.setupWithViewPager(binding.viewPaser)
//        binding.indicatorView.apply {
//            setSliderColor(Color.parseColor("#989696"), Color.parseColor("#1277C5"))
//            setSliderWidth(resources.getDimension(R.dimen.dimen_10))
//            setSliderHeight(resources.getDimension(R.dimen.dimen_6))
//            setSlideMode(IndicatorSlideMode.NORMAL)
//            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
//            setPageSize(binding.viewPaser.adapter!!.itemCount)
//            notifyDataChanged()
//        }
    }

    private fun setAdapter() {
        adapter = HomeAdapter(requireContext(), homeList, this)
        binding.rvHome.adapter = adapter
    }

    override fun onChanged(value: Resource<HomeResponse>) {
        homeList.clear()
        model = value.data?.body
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

                if (value.message == "Please Login First") {
                    startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
                        putExtra("session","0")
                    })
                }else{
                    showErrorAlert(requireActivity(), value.message.toString())
                }

            }

            Status.LOADING -> {
                Log.e("error", value.message.toString())
            }
        }
    }

    override fun click(position:Int,id: String) {
        data = id
        showDialog()
    }

    private fun showDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.consume_bottom_sheet, null)
        val btnDone = view.findViewById<Button>(R.id.btnDone)
//        val btnOpen = view.findViewById<ImageView>(R.id.ivBottom)
        val btnOpen = view.findViewById<Button>(R.id.btnCharacteristies)
        val tvDone = view.findViewById<TextView>(R.id.tvDone)

        homeVM.getOneProductBenefitsApi(data, requireActivity())
            .observe(requireActivity(), getBenefitApi)
        tvDone.text = "Congratulations " + getLoginPreference("user_name", "")

        btnDone.setOnClickListener {
            dialog.dismiss()
            homeVM.consumeProductApi(data, requireActivity())
                .observe(requireActivity(), consumeProductApi)
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