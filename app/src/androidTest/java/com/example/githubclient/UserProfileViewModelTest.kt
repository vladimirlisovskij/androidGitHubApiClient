package com.example.githubclient

import com.example.githubclient.domain.enteties.apiResponse.UserInfoResponse
import com.example.githubclient.domain.exceptions.RateLimitException
import com.example.githubclient.domain.exceptions.UserNotFoundException
import com.example.githubclient.domain.useCase.api.GetUserInfoUseCase
import com.example.githubclient.domain.useCase.sharedPreference.ClearUserIdUseCase
import com.example.githubclient.domain.useCase.sharedPreference.LoadUserIdUseCase
import com.example.githubclient.presentation.userProfile.UserProfileScreenState
import com.example.githubclient.presentation.userProfile.UserProfileViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserProfileViewModelTest {
    companion object {
        const val CORRECT_USER = "CORRECT_USER"
        const val INVALID_ID_ERROR_USER = "INVALID_ID_ERROR_USER"
        const val RATE_LIMIT_ERROR_USER = "RATE_LIMIT_ERROR_USER"
        const val UNKNOWN_ERROR_USER = "UNKNOWN_ERROR_USER"
        const val TEST_PROFILE_URL = "TEST_PROFILE_URL"
    }

    private val correctUserProfile = mockk<UserInfoResponse>()
    @RelaxedMockK
    private lateinit var clearUserIdUseCase: ClearUserIdUseCase
    @MockK
    private lateinit var loadUserIdUseCase: LoadUserIdUseCase
    @MockK
    private lateinit var getUserInfoUseCase: GetUserInfoUseCase

    private lateinit var userProfileViewModel: UserProfileViewModel


    @Before
    fun onCreate() {
        MockKAnnotations.init(this)

        userProfileViewModel = UserProfileViewModel(
            clearUserIdUseCase = clearUserIdUseCase,
            loadUserIdUseCase = loadUserIdUseCase,
            getUserInfoUseCase = getUserInfoUseCase
        )

        coEvery { getUserInfoUseCase(CORRECT_USER) } returns correctUserProfile
        coEvery { getUserInfoUseCase(INVALID_ID_ERROR_USER) } throws UserNotFoundException()
        coEvery { getUserInfoUseCase(RATE_LIMIT_ERROR_USER) } throws RateLimitException()
        coEvery { getUserInfoUseCase(UNKNOWN_ERROR_USER) } throws Exception()
    }

    @Test
    fun onCorrectUserId() {
        runBlocking {
            coEvery { loadUserIdUseCase() } returns CORRECT_USER

            val job = launch {
                userProfileViewModel.profileInfo.collect {
                    assertEquals(it, correctUserProfile)
                }
            }

            assertEquals(userProfileViewModel.screenState.value, UserProfileScreenState.Default)
            userProfileViewModel.onScreenCreate()
            coVerify { loadUserIdUseCase() }
            delay(100)

            job.cancel()
            coVerify { getUserInfoUseCase(CORRECT_USER) }
        }
    }

    @Test
    fun onInvalidUserId() {
        runBlocking {
            coEvery { loadUserIdUseCase() } returns INVALID_ID_ERROR_USER

            assertEquals(userProfileViewModel.screenState.value, UserProfileScreenState.Default)
            userProfileViewModel.onScreenCreate()
            coVerify { loadUserIdUseCase() }
            delay(100)

            coVerify { getUserInfoUseCase(INVALID_ID_ERROR_USER) }
            assertTrue(userProfileViewModel.screenState.value is UserProfileScreenState.Error)
            userProfileViewModel.onErrorDialogOkClick()
            delay(100)

            coVerify { clearUserIdUseCase() }
            assertEquals(
                userProfileViewModel.screenState.value,
                UserProfileScreenState.NavigateToLogin
            )
        }
    }

    @Test
    fun onRateLimitExceeded() {
        runBlocking {
            coEvery { loadUserIdUseCase() } returns RATE_LIMIT_ERROR_USER

            assertEquals(userProfileViewModel.screenState.value, UserProfileScreenState.Default)
            userProfileViewModel.onScreenCreate()
            coVerify { loadUserIdUseCase() }
            delay(100)

            coVerify { getUserInfoUseCase(RATE_LIMIT_ERROR_USER) }
            assertTrue(userProfileViewModel.screenState.value is UserProfileScreenState.Error)
            userProfileViewModel.onErrorDialogOkClick()
            delay(100)

            coVerify { clearUserIdUseCase() }
            assertEquals(
                userProfileViewModel.screenState.value,
                UserProfileScreenState.NavigateToLogin
            )
        }
    }

    @Test
    fun onUnknownException() {
        runBlocking {
            coEvery { loadUserIdUseCase() } returns UNKNOWN_ERROR_USER

            assertEquals(userProfileViewModel.screenState.value, UserProfileScreenState.Default)
            userProfileViewModel.onScreenCreate()
            coVerify { loadUserIdUseCase() }
            delay(100)

            coVerify { getUserInfoUseCase(UNKNOWN_ERROR_USER) }
            assertTrue(userProfileViewModel.screenState.value is UserProfileScreenState.Error)
            userProfileViewModel.onErrorDialogOkClick()
            delay(100)

            coVerify { clearUserIdUseCase() }
            assertEquals(
                userProfileViewModel.screenState.value,
                UserProfileScreenState.NavigateToLogin
            )
        }
    }

    @Test
    fun onLogout() {
        runBlocking {
            assertEquals(userProfileViewModel.screenState.value, UserProfileScreenState.Default)
            userProfileViewModel.onLogOutClick()
            delay(100)

            coVerify { clearUserIdUseCase() }
            assertEquals(
                userProfileViewModel.screenState.value,
                UserProfileScreenState.NavigateToLogin
            )
        }
    }

    @Test
    fun onProfileOpen() {
        runBlocking {
            val job = launch {
                userProfileViewModel.profileUrl.collect {
                    assertEquals(it, TEST_PROFILE_URL)
                }
            }

            userProfileViewModel.onOpenProfile(TEST_PROFILE_URL)
            delay(100)

            job.cancel()
        }
    }
}

