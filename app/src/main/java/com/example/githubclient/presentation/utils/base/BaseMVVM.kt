package com.example.githubclient.presentation.utils.base

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

}

abstract class BaseViewFragment<ViewModelT : BaseViewModel>(
    @LayoutRes layoutId: Int
) : Fragment(layoutId) {
    abstract val selfViewModel: ViewModelT
}

abstract class BaseViewActivity<ViewModelT : BaseViewModel>(
    @LayoutRes layoutId: Int
) : AppCompatActivity(layoutId) {
    abstract val selfViewModel: ViewModelT
}
