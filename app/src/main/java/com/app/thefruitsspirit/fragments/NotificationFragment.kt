package com.app.thefruitsspirit.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.eazy.base.BaseFragment
import com.app.thefruitsspirit.adapter.NotificationAdapter
import com.app.thefruitsspirit.databinding.FragmentNotificationBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.model.GetNotificationResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.isGone
import com.app.thefruitsspirit.utils.isVisible
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.view_model.AuthVM
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList


@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding>(),
    Observer<Resource<GetNotificationResponse>> {
    private lateinit var adapter: NotificationAdapter
    private val listNotification = ArrayList<GetNotificationResponse.Body>()
    private val notificationVM by viewModels<AuthVM>()
    override val bindingInflater: (LayoutInflater) -> FragmentNotificationBinding
        get() {
            return FragmentNotificationBinding::inflate
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
        notificationVM.getNotification(requireActivity()).observe(requireActivity(),this)
    }
    private fun setAdapter() {
        adapter = NotificationAdapter(requireContext(), listNotification)
        binding.rvNotification.adapter = adapter
    }

    private fun onClicks() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    override fun onChanged(value: Resource<GetNotificationResponse>) {
        listNotification.clear()
        when (value.status) {
            Status.SUCCESS -> {
                if (!value.data?.body.isNullOrEmpty()) {
                    listNotification.addAll(value.data?.body!!)
                    binding.ivImage.isGone()
                    binding.tvName.isGone()
                    binding.btnRefresh.isGone()
                    binding.rvNotification.isVisible()
                    setAdapter()
                    listNotification.reverse()
                } else {
                    binding.rvNotification.isGone()
                    binding.ivImage.isVisible()
                    binding.tvName.isVisible()
                    binding.btnRefresh.isVisible()
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