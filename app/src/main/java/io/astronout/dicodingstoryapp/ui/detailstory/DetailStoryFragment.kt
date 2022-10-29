package io.astronout.dicodingstoryapp.ui.detailstory

import android.os.Bundle
import android.transition.TransitionInflater
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import io.astronout.dicodingstoryapp.R
import io.astronout.dicodingstoryapp.databinding.FragmentDetailStoryBinding
import io.astronout.dicodingstoryapp.ui.base.BaseFragment
import io.astronout.dicodingstoryapp.utils.setImageUrl
import io.astronout.dicodingstoryapp.utils.toDateString

class DetailStoryFragment : BaseFragment(R.layout.fragment_detail_story) {

    private val binding: FragmentDetailStoryBinding by viewBinding()
    private val args: DetailStoryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun initUI() {
        super.initUI()
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun initData() {
        super.initData()
        with(binding) {
            args.story.let {
                ivStoryImage.transitionName = it.id
                ivStoryImage.setImageUrl(it.photoUrl)
                tvUsername.text = it.name
                tvDescription.text = it.description
                tvDate.text = it.createdAt.toDateString()
            }
        }
    }

}