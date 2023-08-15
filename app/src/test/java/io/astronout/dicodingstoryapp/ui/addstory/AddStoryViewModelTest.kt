package io.astronout.dicodingstoryapp.ui.addstory

import io.astronout.dicodingstoryapp.domain.StoryUsecase
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {

    @Mock
    private lateinit var storyUsecase: StoryUsecase
    private lateinit var addStoryViewModel: AddStoryViewModel

    private val dummyFile = File("")
    private val dummyDescription = "Desc"

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(storyUsecase)
    }

    @Test
    fun `Upload file success`() = runTest {
        val expectedResponse = flowOf(Resource.Success(Unit))

        Mockito.`when`(
            storyUsecase.addNewStory(
                dummyFile,
                dummyDescription,
                null,
                null
            )
        ).thenReturn(expectedResponse)

        addStoryViewModel.addNewStory(dummyFile, dummyDescription, null, null).collect {
            Assert.assertTrue(it is Resource.Success)
            Assert.assertFalse(it is Resource.Error)

            if (it is Resource.Success) {
                Assert.assertNotNull(it.data)
                Assert.assertSame(Unit, it.data)
            } else {
                Assert.fail()
            }
        }

        Mockito.verify(storyUsecase).addNewStory(dummyFile, dummyDescription, null, null)
    }

    @Test
    fun `Upload file error with message`(): Unit = runTest {
        val expectedResponse = flowOf(Resource.Error("Failed to add new story"))

        Mockito.`when`(
            storyUsecase.addNewStory(
                dummyFile,
                dummyDescription,
                null,
                null
            )
        ).thenReturn(expectedResponse)

        addStoryViewModel.addNewStory(dummyFile, dummyDescription, null, null).collect {
            Assert.assertFalse(it is Resource.Success)
            Assert.assertTrue(it is Resource.Error)

            if (it is Resource.Error) {
                Assert.assertNotNull(it.message)
            } else {
                Assert.fail()
            }
        }

    }

}