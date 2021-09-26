package com.example.githubclient.presentation.repositoryList

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.githubclient.R
import com.example.githubclient.databinding.FragmentRepositoryListBinding
import com.example.githubclient.presentation.userInfo.UserInfoFragmentDirections
import com.example.githubclient.presentation.userProfile.UserProfileFragmentDirections
import com.example.githubclient.presentation.utils.base.BaseViewFragment
import com.example.githubclient.presentation.utils.dialogs.NotificationDialog
import com.example.githubclient.presentation.utils.sharedViewModels.MainNavigatorViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryListFragment
    : BaseViewFragment<RepositoryListViewModel>(R.layout.fragment_repository_list) {
    override val selfViewModel: RepositoryListViewModel by viewModel()
    private val mainNavigatorViewModel: MainNavigatorViewModel by sharedViewModel()

    private val binding: FragmentRepositoryListBinding by viewBinding()

    private var currentDialog: DialogFragment? = null

    private val adapter = RepositoryListAdapter() // todo extract to module

    private val parentNavigator: NavController by lazy { Navigation.findNavController(requireActivity(), R.id.fcvMainContainer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        with(binding.rvRepoList) {
            layoutManager =
                LinearLayoutManager(context).apply { orientation = LinearLayoutManager.VERTICAL }
            adapter = this@RepositoryListFragment.adapter
        }
        with(adapter) {
            onItemClickListener = {
                selfViewModel.onOpenRepoClick(it)
            }

            onRequestItemsListener = {
                selfViewModel.requestItems()
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launchWhenResumed {
            launch {
                selfViewModel.repositories.collect {
                    adapter.addItems(it)
                }
            }

            launch {
                selfViewModel.isLoad.collect {
                    adapter.isLoad = it
                }
            }

            launch {
                selfViewModel.repoUrl.collect {
                    startActivity(Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(it)
                    })
                }
            }

            launch {
                selfViewModel.screenState.collect {
                    when (it) {
                        RepositoryListScreenState.Default -> currentDialog?.dismiss()
                        is RepositoryListScreenState.Error -> currentDialog =
                            NotificationDialog.create().apply {
                                currentDialog?.dismiss()
                                messageText = it.message
                                headerText = it.header
                                onClickListener = {
                                    selfViewModel.onDialogClick()
                                }
                                show(
                                    this@RepositoryListFragment.childFragmentManager,
                                    NotificationDialog.TAG
                                )
                            }
                        RepositoryListScreenState.NavigateToLogin -> {
                            parentNavigator.navigate(
                                R.id.action_global_loginFragment
                            )
                        }
                    }
                }
            }
        }
    }
}