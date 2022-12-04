package io.astronout.dicodingstoryapp.ui.home

import android.content.Intent
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import io.astronout.dicodingstoryapp.R
import io.astronout.dicodingstoryapp.databinding.FragmentHomeBinding
import io.astronout.dicodingstoryapp.databinding.ItemStoryBinding
import io.astronout.dicodingstoryapp.domain.model.Story
import io.astronout.dicodingstoryapp.ui.base.BaseFragment
import io.astronout.dicodingstoryapp.utils.showDefaultLayout
import io.astronout.dicodingstoryapp.utils.showEmptyLayout
import io.astronout.dicodingstoryapp.utils.showLoadingLayout
import io.astronout.dicodingstoryapp.utils.showToast

class HomeFragment : BaseFragment(R.layout.fragment_home), HomeContract, MenuProvider {

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
            val activity = activity as AppCompatActivity
            activity.setSupportActionBar(binding.toolbar)
            rvStories.apply {
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }
        }
        setupAdapter()
        setupMenu()
    }

    override fun initData() {
        super.initData()
        fetchAllStories()
    }

    override fun initAction() {
        super.initAction()
        with(binding) {
            fabCreateStory.setOnClickListener {
                navController?.navigate(HomeFragmentDirections.actionHomeFragmentToAddStoryFragment())
            }
            swRefreshLayout.setOnRefreshListener {
                adapter.refresh()
                swRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.refresh()
    }

    override fun fetchAllStories() {
        viewModel.allStories.observe(viewLifecycleOwner) {
            onGetAllStoriesSuccess(it)
        }
    }

    override fun setupAdapter() {
        with(binding) {
            adapter.addLoadStateListener { loadState ->
                if ((loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) || loadState.source.refresh is LoadState.Error) {
                    msvStories.showEmptyLayout()
                } else {
                    msvStories.showDefaultLayout()
                }
            }
            runCatching {
                rvStories.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )
            }
        }
    }

    override fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onGetAllStoriesLoading() {
        binding.msvStories.showLoadingLayout()
    }

    override fun onGetAllStoriesSuccess(data: PagingData<Story>) {
        with(binding) {
            val recyclerViewState = rvStories.layoutManager?.onSaveInstanceState()
            adapter.submitData(lifecycle, data)
            rvStories.layoutManager?.onRestoreInstanceState(recyclerViewState)
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
            navController?.navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailStoryFragment(
                    story
                ), extras
            )
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_maps -> {
                navController?.navigate(HomeFragmentDirections.actionHomeFragmentToMapsFragment())
                true
            }
            R.id.menu_logout -> {
                viewModel.clearToken()
                navController?.navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                true
            }
            R.id.menu_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> false
        }
    }

}