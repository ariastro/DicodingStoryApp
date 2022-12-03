package io.astronout.dicodingstoryapp.ui.splash

import io.astronout.dicodingstoryapp.domain.AuthUsecase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SplashScreenViewModelTest {

    @Mock
    private lateinit var authUsecase: AuthUsecase
    private lateinit var splashScreenViewModel: SplashScreenViewModel

    private val dummyToken = "Bearer token"

    @Before
    fun setup() {
        splashScreenViewModel = SplashScreenViewModel(authUsecase)
    }

    @Test
    fun `Get auth token successfully`() = runTest {
        val expectedToken = flowOf(dummyToken)

        `when`(splashScreenViewModel.getAuthToken()).thenReturn(expectedToken)

        splashScreenViewModel.getAuthToken().collect {
            Assert.assertNotNull(it)
            Assert.assertEquals(dummyToken, it)
        }

        verify(authUsecase).getAuthToken()
    }

    @Test
    fun `Get authentication token empty`() = runTest {
        val expectedToken = flowOf("")

        `when`(splashScreenViewModel.getAuthToken()).thenReturn(expectedToken)

        splashScreenViewModel.getAuthToken().collect {
            Assert.assertEquals("", it)
        }

        verify(authUsecase).getAuthToken()
    }

}