package io.astronout.dicodingstoryapp.ui.maps

import androidx.paging.ExperimentalPagingApi
import io.astronout.dicodingstoryapp.domain.StoryUsecase
import io.astronout.dicodingstoryapp.utils.Dummies
import io.astronout.dicodingstoryapp.vo.Resource
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

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class MapsViewModelTest {

    @Mock
    private lateinit var storyUsecase: StoryUsecase
    private lateinit var mapsViewModel: MapsViewModel

    private val dummyStories = Dummies.generateDummyListStory()

    @Before
    fun setup() {
        mapsViewModel = MapsViewModel(storyUsecase)
    }

    @Test
    fun `Get stories with location success - result success`(): Unit = runTest {

        val expectedResponse = flowOf(Resource.Success(dummyStories))

        `when`(mapsViewModel.allStories).thenReturn(expectedResponse)

        mapsViewModel.allStories.collect {
            Assert.assertTrue(it is Resource.Success)
            Assert.assertFalse(it is Resource.Error)

            if (it is Resource.Success) {
                Assert.assertNotNull(it.data)
                Assert.assertSame(it.data, dummyStories)
            }
        }

        verify(storyUsecase).getStories()
    }

    @Test
    fun `Get stories with location failed - result error with message`(): Unit = runTest {

        val expectedResponse = flowOf(Resource.Error("Failed to get stories"))

        `when`(mapsViewModel.allStories).thenReturn(expectedResponse)

        mapsViewModel.allStories.collect {
            Assert.assertFalse(it is Resource.Success)
            Assert.assertTrue(it is Resource.Error)

            if (it is Resource.Error) {
                Assert.assertNotNull(it.message)
            }
        }

        verify(storyUsecase).getStories()
    }

}