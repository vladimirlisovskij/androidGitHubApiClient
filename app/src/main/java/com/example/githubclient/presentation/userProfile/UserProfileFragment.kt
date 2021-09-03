package com.example.githubclient.presentation.userProfile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.githubclient.R
import com.example.githubclient.databinding.FragmentProfileBinding
import com.example.githubclient.presentation.utils.base.BaseViewFragment
import com.example.githubclient.presentation.utils.sharedViewModels.MainNavigatorViewModel
import com.example.githubclient.presentation.utils.sharedViewModels.UserInfoNavigationViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfileFragment : BaseViewFragment<UserProfileViewModel>(R.layout.fragment_profile) {
    companion object Fabric {
        fun create() = UserProfileFragment()
    }

    override val selfViewModel: UserProfileViewModel by viewModel()

    private val mainNavigatorViewModel: MainNavigatorViewModel by sharedViewModel()
    private val binding: FragmentProfileBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObservers()
        initListeners()
        super.onViewCreated(view, savedInstanceState)
        selfViewModel.onScreenCreate()
    }

    private fun initListeners() {
        with(binding) {
            btnLogout.setOnClickListener {
                mainNavigatorViewModel.openLoginScreen()
                selfViewModel.onLogOutClick()
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launchWhenResumed {
            launch {
                selfViewModel.profileInfo.collect {
                    with(binding) {
                        tvName.text = it.login
                        tvFollowers.text = getString(R.string.followers, it.followers)
                        tvFollowing.text = getString(R.string.following, it.following)
                        Glide.with(this@UserProfileFragment)
                            .load(it.avatarUrl)
                            .centerCrop()
                            .circleCrop()
                            .into(ivAvatar)
                    }
                }
            }
        }
    }
}

