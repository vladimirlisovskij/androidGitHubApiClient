package com.example.githubclient.presentation.userInfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.githubclient.R
import com.example.githubclient.databinding.FragmentUserInfoBinding
import com.example.githubclient.presentation.enteties.UserInfoScreens
import com.example.githubclient.presentation.repositoryList.RepositoryListFragmentDirections
import com.example.githubclient.presentation.userProfile.UserProfileFragmentDirections
import com.example.githubclient.presentation.utils.base.BaseViewFragment
import com.example.githubclient.presentation.utils.sharedViewModels.UserInfoNavigationViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserInfoFragment : BaseViewFragment<UserInfoViewModel>(R.layout.fragment_user_info) {
    companion object Fabric {
        fun create() = UserInfoFragment()
    }

    override val selfViewModel: UserInfoViewModel by viewModel()

    private val selfNavigationViewModel: UserInfoNavigationViewModel by sharedViewModel()
    private val binding: FragmentUserInfoBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.bnvMainNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.actionReposList -> {
                    selfNavigationViewModel.navigateToRepositoryList()
                }

                R.id.actionProfile -> {
                    selfNavigationViewModel.navigateToProfileInfo()
                }
            }
            true
        }
    }

    private fun initObservers() {
        lifecycleScope.launchWhenResumed {
            launch {
                selfNavigationViewModel.currentScreen.collect {
                    when (it) {
                        UserInfoScreens.REPOSITORY_LIST -> {
                            binding.fcvUserInfoContainer.findNavController().navigate(
                                UserProfileFragmentDirections.actionUserProfileFragmentToRepositoryListFragment()
                            )
                            binding.bnvMainNavigation.selectedItemId = R.id.actionReposList
                        }

                        UserInfoScreens.USER_PROFILE -> {
                            binding.fcvUserInfoContainer.findNavController().navigate(
                                RepositoryListFragmentDirections.actionRepositoryListFragmentToUserProfileFragment()
                            )
                            binding.bnvMainNavigation.selectedItemId = R.id.actionProfile
                        }
                    }
                }
            }
        }
    }
}
