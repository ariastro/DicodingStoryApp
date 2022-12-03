package io.astronout.dicodingstoryapp.ui.auth.login

import io.astronout.dicodingstoryapp.domain.AuthUsecase
import io.astronout.dicodingstoryapp.utils.CoroutinesTestRule
import io.astronout.dicodingstoryapp.utils.Dummies
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var authUsecase: AuthUsecase
    private lateinit var loginViewModel: LoginViewModel

    private val dummyLoginResponse = Dummies.generateDummyLogin()
    private val dummyToken = "Bearer token"
    private val dummyEmail = "email@mail.com"
    private val dummyPassword = "password"

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(authUsecase)
    }

    @Test
    fun `Login successfully - result success`(): Unit = runTest {
        val expectedResponse = flow {
            emit(Resource.Success(dummyLoginResponse))
        }

        Mockito.`when`(loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        loginViewModel.login(dummyEmail, dummyPassword).collect {
            Assert.assertTrue(it is Resource.Success)
            Assert.assertFalse(it is Resource.Error)

            if (it is Resource.Success) {
                Assert.assertNotNull(it.data)
                Assert.assertSame(dummyLoginResponse, it.data)
            }
        }

        Mockito.verify(authUsecase).login(dummyEmail, dummyPassword)
    }

    @Test
    fun `Login failed - result error with message`(): Unit = runTest {
        val expectedResponse = flow {
            emit(Resource.Error("login failed"))
        }

        Mockito.`when`(loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        loginViewModel.login(dummyEmail, dummyPassword).collect {
            Assert.assertFalse(it is Resource.Success)
            Assert.assertTrue(it is Resource.Error)

            if (it is Resource.Error) {
                Assert.assertNotNull(it.message)
            }
        }

        Mockito.verify(authUsecase).login(dummyEmail, dummyPassword)
    }

    @Test
    fun `Save auth token successfully`(): Unit = runTest {
        loginViewModel.saveAuthToken(dummyToken)
        Mockito.verify(authUsecase).saveAuthToken(dummyToken)
    }

}