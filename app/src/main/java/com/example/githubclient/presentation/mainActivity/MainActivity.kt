package com.example.githubclient.presentation.mainActivity

import android.os.Bundle
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.githubclient.R
import com.example.githubclient.databinding.ActivityMainBinding
import com.example.githubclient.presentation.enteties.LoginStatusScreens
import com.example.githubclient.presentation.loginFragment.LoginFragment
import com.example.githubclient.presentation.repositoryList.RepositoryListFragment
import com.example.githubclient.presentation.userInfo.UserInfoFragment
import com.example.githubclient.presentation.utils.base.BaseViewActivity
import com.example.githubclient.presentation.utils.sharedViewModels.MainNavigatorViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseViewActivity<MainActivityViewModel>(R.layout.activity_main) {
    override val selfViewModel: MainActivityViewModel by viewModel()

    private val navigationViewModel: MainNavigatorViewModel by viewModel()
    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            navigationViewModel.onScreenCreate()
        }
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launchWhenResumed {
            launch {
                navigationViewModel.currentScreens.collect {
                    supportFragmentManager.commit {
                        when (it) {
                            LoginStatusScreens.LOGIN -> replace(
                                R.id.fcvMainContainer,
                                LoginFragment.create()
                            )

                            LoginStatusScreens.USER_INFO -> replace(
                                R.id.fcvMainContainer,
                                UserInfoFragment.create()
                            )
                        }
                    }
                }
            }
        }
    }
}
