package com.example.githubclient.presentation.loginFragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.githubclient.R
import com.example.githubclient.databinding.FragmentLoginBinding
import com.example.githubclient.presentation.utils.base.BaseViewFragment
import com.example.githubclient.presentation.utils.dialogs.NotificationDialog
import com.example.githubclient.presentation.utils.sharedViewModels.MainNavigatorViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseViewFragment<LoginViewModel>(R.layout.fragment_login) {
    companion object Fabric {
        fun create() = LoginFragment()
    }

    override val selfViewModel: LoginViewModel by viewModel()

    private val navigationViewModel: MainNavigatorViewModel by sharedViewModel()
    private val binding: FragmentLoginBinding by viewBinding()

    private val errorNotification = NotificationDialog.create(
        R.drawable.ic_simple_background,
        "header",
        "text"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launchWhenResumed {
            launch {
                selfViewModel.currentScreens.collect {
                    when (it) {
                        is LoginFragmentScreenStatus.Error -> {
                            errorNotification.show(childFragmentManager, "123")
                        }
                        LoginFragmentScreenStatus.OpenProfile -> navigationViewModel.openUserInfoScreen()
                    }
                }
            }
        }
    }

    private fun initListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
                selfViewModel.onLoginClick(etLogin.text.toString())
            }
        }
    }
}


