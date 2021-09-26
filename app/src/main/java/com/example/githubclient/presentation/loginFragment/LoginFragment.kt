package com.example.githubclient.presentation.loginFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.githubclient.R
import com.example.githubclient.databinding.FragmentLoginBinding
import com.example.githubclient.presentation.enteties.LoginFragmentScreenStatus
import com.example.githubclient.presentation.utils.base.BaseViewFragment
import com.example.githubclient.presentation.utils.dialogs.NotificationDialog
import com.example.githubclient.presentation.utils.dialogs.ProgressDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseViewFragment<LoginViewModel>(R.layout.fragment_login) {
    override val selfViewModel: LoginViewModel by viewModel()

    private val binding: FragmentLoginBinding by viewBinding()


    private var currentDialog: DialogFragment? = null

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
                        LoginFragmentScreenStatus.ClearState -> currentDialog?.dismiss()
                        is LoginFragmentScreenStatus.Error -> {
                            currentDialog?.dismiss()
                            currentDialog = NotificationDialog.create().apply {
                                onClickListener = { selfViewModel.onDialogOkClick() }
                                headerText = it.header
                                messageText = it.message
                                show(
                                    this@LoginFragment.childFragmentManager,
                                    NotificationDialog.TAG
                                )
                            }
                        }
                        LoginFragmentScreenStatus.Load -> {
                            currentDialog?.dismiss()
                            currentDialog = ProgressDialog.create().apply {
                                show(
                                    this@LoginFragment.childFragmentManager,
                                    ProgressDialog.TAG
                                )
                            }
                        }
                        LoginFragmentScreenStatus.OpenProfile -> {
                            findNavController().navigate(
                                LoginFragmentDirections.actionLoginFragmentToUserInfoFragment()
                            )
                        }
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


