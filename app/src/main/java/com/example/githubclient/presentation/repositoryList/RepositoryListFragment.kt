package com.example.githubclient.presentation.repositoryList

import com.example.githubclient.R
import com.example.githubclient.presentation.utils.base.BaseViewFragment
import com.example.githubclient.presentation.utils.sharedViewModels.MainNavigatorViewModel
import com.example.githubclient.presentation.utils.sharedViewModels.UserInfoNavigationViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryListFragment
    : BaseViewFragment<RepositoryListViewModel>(R.layout.fragment_repository_list) {
    companion object Fabric {
        fun create() = RepositoryListFragment()
    }

    override val selfViewModel: RepositoryListViewModel by viewModel()
}