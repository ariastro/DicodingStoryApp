package io.astronout.dicodingstoryapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import io.astronout.dicodingstoryapp.domain.AuthUsecase
import io.astronout.dicodingstoryapp.domain.StoryUsecase
import io.astronout.dicodingstoryapp.domain.model.Story
import io.astronout.dicodingstoryapp.utils.CoroutinesTestRule
import io.astronout.dicodingstoryapp.utils.Dummies
import io.astronout.dicodingstoryapp.utils.PagedTestDataSource
import io.astronout.dicodingstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
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
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyUsecase: StoryUsecase
    @Mock
    private lateinit var authUsecase: AuthUsecase
    private lateinit var viewModel: HomeViewModel

    private val dummyStories = Dummies.generateDummyListStory()

    @Test
    fun `Get all stories successfully`() = runTest {
        val data = PagedTestDataSource.snapshot(dummyStories)
        val stories = flow { emit(data) }
        Mockito.`when`(storyUsecase.getAllStories()).thenReturn(stories)
        viewModel = HomeViewModel(storyUsecase, authUsecase)
        val actualStories = viewModel.allStories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(actualStories)

        advanceUntilIdle()

        Mockito.verify(storyUsecase).getAllStories()
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
        Assert.assertEquals(dummyStories.first(), differ.snapshot().first())
    }

    @Test
    fun `Get all stories empty`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val expectedStory = flow { emit(data) }
        Mockito.`when`(storyUsecase.getAllStories()).thenReturn(expectedStory)
        viewModel = HomeViewModel(storyUsecase, authUsecase)
        val actualStories = viewModel.allStories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(actualStories)

        Assert.assertEquals(0, differ.snapshot().size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    @Test
    fun `Clear auth token`(): Unit = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val expectedStory = flow { emit(data) }
        Mockito.`when`(storyUsecase.getAllStories()).thenReturn(expectedStory)
        viewModel = HomeViewModel(storyUsecase, authUsecase)
        viewModel.clearToken()
        Mockito.verify(authUsecase).saveAuthToken("")
    }

}