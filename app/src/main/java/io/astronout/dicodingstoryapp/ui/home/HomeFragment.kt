package io.astronout.dicodingstoryapp.ui.home

import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import io.astronout.dicodingstoryapp.R
import io.astronout.dicodingstoryapp.databinding.FragmentHomeBinding
import io.astronout.dicodingstoryapp.databinding.ItemStoryBinding
import io.astronout.dicodingstoryapp.domain.model.Story
import io.astronout.dicodingstoryapp.ui.base.BaseFragment
import io.astronout.dicodingstoryapp.utils.*
import io.astronout.dicodingstoryapp.vo.Resource

class HomeFragment : BaseFragment(R.layout.fragment_home), HomeContract {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel: HomeViewModel by viewModels()
    private val navController: NavController? by lazy { findNavController() }
    private val adapter: StoryAdapter by lazy {
        StoryAdapter { story, itemStoryBinding ->
            onNavigateToDetailStory(story, itemStoryBinding)
        }
    }

    override fun initUI() {
        super.initUI()
        with(binding) {
            rvStories.adapter = adapter
            rvStories.apply {
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        collectLifecycleFlow(viewModel.allStories) {
            when(it) {
                is Resource.Error -> onGetAllStoriesFailed(it.message)
                is Resource.Loading -> onGetAllStoriesLoading()
                is Resource.Success -> onGetAllStoriesSuccess(it.data)
            }
        }
    }

    override fun onGetAllStoriesLoading() {
        binding.msvStories.showLoadingLayout()
    }

    override fun onGetAllStoriesSuccess(data: List<Story>) {
        with(binding) {
            if (data.isNotEmpty()) {
                msvStories.showDefaultLayout()
                adapter.submitList(data)
            } else {
                msvStories.showEmptyLayout()
            }
        }
    }

    override fun onGetAllStoriesFailed(message: String) {
        binding.msvStories.showEmptyLayout()
        showToast(message)
    }

    override fun onNavigateToDetailStory(story: Story, itemStoryBinding: ItemStoryBinding) {
        with(itemStoryBinding) {
            val extras = FragmentNavigatorExtras(
                ivStoryImage to story.id,
            )
            navController?.navigate(HomeFragmentDirections.actionHomeFragmentToDetailStoryFragment(story), extras)
        }
    }

}