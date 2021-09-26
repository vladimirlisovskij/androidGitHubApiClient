package com.example.githubclient

import com.example.githubclient.domain.enteties.apiResponse.UserInfoResponse
import com.example.githubclient.domain.exceptions.RateLimitException
import com.example.githubclient.domain.exceptions.UserNotFoundException
import com.example.githubclient.domain.useCase.api.GetUserInfoUseCase
import com.example.githubclient.domain.useCase.sharedPreference.SaveUserIdUseCase
import com.example.githubclient.presentation.enteties.LoginFragmentScreenStatus
import com.example.githubclient.presentation.loginFragment.LoginViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {
    companion object {
        const val CORRECT_USER_ID = "CORRECT_USER_ID"
        const val INCORRECT_USER_ID = "INCORRECT_USER_ID"
        const val RATE_LIMIT_USER_ID = "RATE_LIMIT_USER_ID"
        const val UNKNOWN_ERROR_USER_ID = "UNKNOWN_ERROR_USER_ID"
    }

    private lateinit var loginViewModel: LoginViewModel

    @RelaxedMockK
    lateinit var saveUserIdUseCase: SaveUserIdUseCase

    @MockK
    lateinit var getUserInfoUseCase: GetUserInfoUseCase

    @MockK
    lateinit var normalResponse: UserInfoResponse

    @Before
    fun onCreate() {
        MockKAnnotations.init(this)

        coEvery { getUserInfoUseCase(CORRECT_USER_ID) } returns normalResponse
        coEvery { getUserInfoUseCase(INCORRECT_USER_ID) } throws UserNotFoundException()
        coEvery { getUserInfoUseCase(RATE_LIMIT_USER_ID) } throws RateLimitException()
        coEvery { getUserInfoUseCase(UNKNOWN_ERROR_USER_ID) } throws Exception()

        coEvery { normalResponse.login } returns CORRECT_USER_ID

        loginViewModel = LoginViewModel(
            saveUserIdUseCase = saveUserIdUseCase,
            getUserInfoUseCase = getUserInfoUseCase
        )
    }

    @Test
    fun onCorrectUserId() {
        runBlocking {
            assertEquals(loginViewModel.currentScreens.value, LoginFragmentScreenStatus.ClearState)
            loginViewModel.onLoginClick(CORRECT_USER_ID)
            coVerify { getUserInfoUseCase(CORRECT_USER_ID) }
            delay(1000)

            assertEquals(loginViewModel.currentScreens.value, LoginFragmentScreenStatus.OpenProfile)
            coVerify { saveUserIdUseCase(CORRECT_USER_ID) }
        }
    }

    @Test
    fun onInCorrectUserId() {
        runBlocking {
            assertEquals(loginViewModel.currentScreens.value, LoginFragmentScreenStatus.ClearState)
            loginViewModel.onLoginClick(INCORRECT_USER_ID)
            coVerify { getUserInfoUseCase(INCORRECT_USER_ID) }
            delay(100)

            assertTrue(loginViewModel.currentScreens.value is LoginFragmentScreenStatus.Error)
        }
    }
}