package com.example.githubclient.presentation.userProfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.githubclient.R
import com.example.githubclient.databinding.FragmentProfileBinding
import com.example.githubclient.presentation.userInfo.UserInfoFragmentDirections
import com.example.githubclient.presentation.utils.base.BaseViewFragment
import com.example.githubclient.presentation.utils.dialogs.NotificationDialog
import com.example.githubclient.presentation.utils.dialogs.ProgressDialog
import com.github.terrakok.cicerone.NavigatorHolder
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserProfileFragment : BaseViewFragment<UserProfileViewModel>(R.layout.fragment_profile) {
    override val selfViewModel: UserProfileViewModel by viewModel()

    private val binding: FragmentProfileBinding by viewBinding()

    private var currentDialog: DialogFragment? = null
    private lateinit var profileUrl: String

    private val parentNavigator: NavController by lazy { Navigation.findNavController(requireActivity(), R.id.fcvMainContainer) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObservers()
        initListeners()
        super.onViewCreated(view, savedInstanceState)
        selfViewModel.onScreenCreate()
    }

    private fun initListeners() {
        with(binding) {
            btnLogout.setOnClickListener {
                selfViewModel.onLogOutClick()
            }

            btnOpenProfile.setOnClickListener {
                selfViewModel.onOpenProfile(profileUrl)
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launchWhenResumed {
            launch {
                selfViewModel.profileInfo.collect {
                    with(binding) {
                        profileUrl = it.profileUrl

                        tvName.text = it.login
                        tvFollowers.text = getString(R.string.followers, it.followers)
                        tvFollowing.text = getString(R.string.following, it.following)
                        tvPublicRepos.text = getString(R.string.publicRepos, it.publicRepos)
                        Glide.with(this@UserProfileFragment)
                            .load(it.avatarUrl)
                            .centerCrop()
                            .into(ivAvatar)
                    }
                }
            }

            launch {
                selfViewModel.screenState.collect {
                    when (it) {
                        UserProfileScreenState.Default -> currentDialog?.dismiss()
                        is UserProfileScreenState.Error -> currentDialog =
                            NotificationDialog.create().apply {
                                currentDialog?.dismiss()
                                headerText = it.header
                                messageText = it.message
                                onClickListener = {
                                    selfViewModel.onErrorDialogOkClick()
                                }
                                show(
                                    this@UserProfileFragment.childFragmentManager,
                                    NotificationDialog.TAG
                                )
                            }
                        UserProfileScreenState.Progress -> currentDialog =
                            ProgressDialog.create().apply {
                                currentDialog?.dismiss()
                                show(
                                    this@UserProfileFragment.childFragmentManager,
                                    ProgressDialog.TAG
                                )
                            }
                        UserProfileScreenState.NavigateToLogin -> {
                            parentNavigator.navigate(
                                R.id.action_global_loginFragment
                            )
                        }
                    }
                }
            }

            launch {
                selfViewModel.profileUrl.collect {
                    startActivity(Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(it)
                    })
                }
            }
        }
    }
}

