package io.astronout.dicodingstoryapp.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.astronout.dicodingstoryapp.customviews.AppProgressDialog

@AndroidEntryPoint
abstract class BaseFragment(@LayoutRes layoutId: Int): Fragment(layoutId) {

    open fun initData() {}
    open fun initUI() {
        setupProgress()
    }
    open fun initAction() {}
    open fun initObserver() {}

    lateinit var progress : AppProgressDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initUI()
        initAction()
        initObserver()

    }

    private fun setupProgress() {
        progress = AppProgressDialog(requireContext())
        progress.setCancelable(false)
        progress.setCanceledOnTouchOutside(false)
    }

}