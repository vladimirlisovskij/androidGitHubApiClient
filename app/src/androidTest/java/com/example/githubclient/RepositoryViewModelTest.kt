package com.example.githubclient

import com.example.githubclient.domain.enteties.apiResponse.UserRepoResponse
import com.example.githubclient.domain.exceptions.UserNotFoundException
import com.example.githubclient.domain.useCase.api.GetUserRepositories
import com.example.githubclient.domain.useCase.sharedPreference.ClearUserIdUseCase
import com.example.githubclient.domain.useCase.sharedPreference.LoadUserIdUseCase
import com.example.githubclient.presentation.repositoryList.RepositoryListViewModel
import com.example.githubclient.presentation.repositoryList.RepositoryListScreenState
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryViewModelTest {
    companion object {
        const val EMPTY_REPOS_USERID = "EMPTY_REPOS_USERID"
        const val NON_EMPTY_REPOS_USERID = "REPOS_USERID"
        const val INVALID_USERID = "INVALID_USERID"
        const val TEST_URL = "TEST_URL"
    }

    @MockK
    private lateinit var getUserIdUseCase: LoadUserIdUseCase
    @MockK
    private lateinit var getUserRepositories: GetUserRepositories
    @RelaxedMockK
    private lateinit var clearUserIdUseCase: ClearUserIdUseCase

    private lateinit var repositoryListViewModel: RepositoryListViewModel

    private val emptyRepos = listOf<UserRepoResponse>()
    private val nonEmptyRepos = listOf<UserRepoResponse>(mockk())

    @Before
    fun onCreate() {
        MockKAnnotations.init(this)

        coEvery { getUserRepositories(EMPTY_REPOS_USERID, any()) } returns emptyRepos
        coEvery { getUserRepositories(NON_EMPTY_REPOS_USERID, any()) } returns nonEmptyRepos
        coEvery { getUserRepositories(INVALID_USERID, any()) } throws UserNotFoundException()

        repositoryListViewModel =
            RepositoryListViewModel(
                getUserRepositories = getUserRepositories,
                getUserIdUseCase = getUserIdUseCase,
                clearUserIdUseCase = clearUserIdUseCase
            )
    }

    @Test
    fun getEmptyReposList() {
        runBlocking {
            coEvery { getUserIdUseCase() } returns EMPTY_REPOS_USERID

            assertEquals(repositoryListViewModel.repositories.value, emptyRepos)
            assertEquals(repositoryListViewModel.isLoad.value, true)
            delay(100)

            repositoryListViewModel.requestItems()
            delay(100)

            assertEquals(repositoryListViewModel.repositories.value, emptyRepos)
            assertEquals(repositoryListViewModel.isLoad.value, false)
            coVerify { getUserRepositories(EMPTY_REPOS_USERID, any()) }
        }
    }

    @Test
    fun getInvalidUserId() {
        runBlocking {
            coEvery { getUserIdUseCase() } returns INVALID_USERID

            assertEquals(repositoryListViewModel.screenState.value, RepositoryListScreenState.Default)
            delay(100)

            repositoryListViewModel.requestItems()
            delay(100)

            assertTrue(repositoryListViewModel.screenState.value is RepositoryListScreenState.Error)
            delay(100)

            repositoryListViewModel.onDialogClick()
            delay(100)

            assertEquals(repositoryListViewModel.screenState.value, RepositoryListScreenState.NavigateToLogin)
            coVerify { getUserRepositories(INVALID_USERID, any()) }
        }
    }

    @Test
    fun getNonEmptyReposList() {
        runBlocking {
            coEvery { getUserIdUseCase() } returns NON_EMPTY_REPOS_USERID

            assertEquals(repositoryListViewModel.repositories.value, emptyRepos)
            delay(100)

            repositoryListViewModel.requestItems()
            delay(100)

            assertEquals(repositoryListViewModel.repositories.value, nonEmptyRepos)
            coVerify { getUserRepositories(NON_EMPTY_REPOS_USERID, any()) }
        }
    }

    @Test
    fun openRepoUrl() {
        runBlocking {
            val job = launch {
                repositoryListViewModel.repoUrl.collect {
                    assertEquals(it, TEST_URL)
                }
            }

            repositoryListViewModel.onOpenRepoClick(TEST_URL)
            delay(100)

            job.cancel()
        }
    }
}