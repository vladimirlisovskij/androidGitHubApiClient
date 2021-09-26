package com.example.githubclient.presentation.mainActivity

import com.example.githubclient.R
import com.example.githubclient.presentation.utils.base.BaseViewActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseViewActivity<MainActivityViewModel>(R.layout.activity_main) {
    override val selfViewModel: MainActivityViewModel by viewModel()
}
