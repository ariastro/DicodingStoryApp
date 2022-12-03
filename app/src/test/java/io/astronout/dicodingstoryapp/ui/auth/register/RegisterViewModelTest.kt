package io.astronout.dicodingstoryapp.ui.auth.register

import io.astronout.dicodingstoryapp.domain.AuthUsecase
import io.astronout.dicodingstoryapp.utils.Dummies
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
internal class RegisterViewModelTest {

    @Mock
    private lateinit var authUsecase: AuthUsecase
    private lateinit var registerViewModel: RegisterViewModel

    private val dummyRegisterResponse = Dummies.generateDummyRegister()
    private val dummyName = "Full Name"
    private val dummyEmail = "email@mail.com"
    private val dummyPassword = "password"

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(authUsecase)
    }

    @Test
    fun `Registration success - result success`(): Unit = runTest {
        val expectedResponse = flowOf(Resource.Success(dummyRegisterResponse))

        `when`(registerViewModel.register(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedResponse
        )

        registerViewModel.register(dummyName, dummyEmail, dummyPassword).collect {
            Assert.assertTrue(it is Resource.Success)
            Assert.assertFalse(it is Resource.Error)

            if (it is Resource.Success) {
                Assert.assertNotNull(it.data)
                Assert.assertSame(dummyRegisterResponse, it.data)
            }
        }

        verify(authUsecase).register(dummyName, dummyEmail, dummyPassword)
    }

    @Test
    fun `Registration failed - result error with message`(): Unit = runTest {
        val expectedResponse = flow {
            emit(Resource.Error("Register failed"))
        }

        `when`(registerViewModel.register(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedResponse
        )

        registerViewModel.register(dummyName, dummyEmail, dummyPassword).collect {
            Assert.assertFalse(it is Resource.Success)
            Assert.assertTrue(it is Resource.Error)

            if (it is Resource.Error) {
                Assert.assertNotNull(it.message)
            }
        }
    }

}