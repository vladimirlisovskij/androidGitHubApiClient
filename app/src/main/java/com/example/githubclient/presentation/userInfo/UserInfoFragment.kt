package com.example.githubclient.presentation.userInfo

import android.os.Bundle
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.githubclient.R
import com.example.githubclient.presentation.enteties.UserInfoScreens
import com.example.githubclient.presentation.repositoryList.RepositoryListFragment
import com.example.githubclient.presentation.userProfile.UserProfileFragment
import com.example.githubclient.presentation.utils.base.BaseViewFragment
import com.example.githubclient.presentation.utils.sharedViewModels.UserInfoNavigationViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserInfoFragment: BaseViewFragment<UserInfoViewModel>(R.layout.fragment_user_info) {
    companion object Fabric {
        fun create() = UserInfoFragment()
    }

    override val selfViewModel: UserInfoViewModel by viewModel()
    private val selfNavigationViewModel: UserInfoNavigationViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launchWhenResumed {
            launch {
                selfNavigationViewModel.currentScreen.collect {
                    childFragmentManager.commit {
                        when(it) {
                            UserInfoScreens.REPOSITORY_LIST -> replace(
                                R.id.fcvUserInfoContainer,
                                RepositoryListFragment.create()
                            )

                            UserInfoScreens.USER_PROFILE -> replace(
                                R.id.fcvUserInfoContainer,
                                UserProfileFragment.create()
                            )
                        }
                    }
                }
            }
        }
    }
}
