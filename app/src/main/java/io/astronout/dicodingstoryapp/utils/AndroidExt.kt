package io.astronout.dicodingstoryapp.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.kennyc.view.MultiStateView
import io.astronout.dicodingstoryapp.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun View.toVisible() {
    visibility = View.VISIBLE
}

fun View.toGone() {
    visibility = View.GONE
}

fun View.toInvisible() {
    visibility = View.INVISIBLE
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun <T> Fragment.collectLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}

fun MultiStateView.showDefaultLayout() {
    viewState = MultiStateView.ViewState.CONTENT
}

fun MultiStateView.showLoadingLayout() {
    viewState = MultiStateView.ViewState.LOADING
}

fun MultiStateView.showEmptyLayout() {
    viewState = MultiStateView.ViewState.EMPTY
}

fun AppCompatEditText.setDrawable(
    start: Drawable? = null,
    top: Drawable? = null,
    end: Drawable? = null,
    bottom: Drawable? = null
) {
    setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom)
}

fun ImageView.setImageUrl(url: String) {
    Glide
        .with(context)
        .load(url)
        .placeholder(R.drawable.ic_placeholder)
        .error(R.drawable.ic_placeholder)
        .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
        .into(this)
}

fun String.toDateString(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return try {
        val date = dateFormat.parse(this) ?: Date()
        val hour = 3600 * 1000
        val newDate = Date(date.time + 7 * hour)
        dateFormat.applyPattern("dd MMM yyyy")
        dateFormat.format(newDate)
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}