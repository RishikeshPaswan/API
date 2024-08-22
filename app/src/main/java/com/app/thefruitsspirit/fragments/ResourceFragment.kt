package com.app.thefruitsspirit.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.eazy.base.BaseFragment
import com.app.thefruitsspirit.adapter.ResourceAdapter
import com.app.thefruitsspirit.auth.LoginActivity
import com.app.thefruitsspirit.databinding.FragmentResourceBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.model.GetBonusResponse
import com.app.thefruitsspirit.model.ResourceResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.isGone
import com.app.thefruitsspirit.utils.isVisible
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.view_model.AuthVM
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class ResourceFragment : BaseFragment<FragmentResourceBinding>(),
    Observer<Resource<GetBonusResponse>> {
    private lateinit var adapter: ResourceAdapter
    private val resourceList = ArrayList<ResourceResponse.Body>()
    private val resourceVM by viewModels<AuthVM>()
    override val bindingInflater: (LayoutInflater) -> FragmentResourceBinding
        get() {
            return FragmentResourceBinding::inflate
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        resourceVM.getResourceApi(requireActivity()).observe(requireActivity(), this)
        resourceVM.getRecipesApi("1",requireActivity()).observe(requireActivity(), this)

    }

//    private fun setAdapter() {
//        adapter = ResourceAdapter(requireContext(), resourceList)
//        binding.rvResource.adapter = adapter
//    }

    override fun onChanged(value: Resource<GetBonusResponse>) {
        resourceList.clear()
        when (value.status) {
            Status.SUCCESS -> {
                binding.tvConant.text = value.data!!.body?.content?.let {
                    HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)
                }
//                if (!value.data?.body.isNullOrEmpty()) {
//                    resourceList.addAll(value.data?.body!!)
//                    binding.tvNoResultFound.isGone()
//                    binding.rvResource.isVisible()
//                    setAdapter()
//                } else {
//                    binding.rvResource.isGone()
//                    binding.tvNoResultFound.isVisible()
//                }
            }

            Status.ERROR -> {
//                Log.e("error", value.message.toString())
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

}