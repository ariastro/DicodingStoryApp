package io.astronout.dicodingstoryapp.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.kennyc.view.MultiStateView
import io.astronout.dicodingstoryapp.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DateFormat
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

fun Context.getColorResource(@ColorRes color: Int) = ContextCompat.getColor(this, color)
fun Context.getColorStateListResource(@ColorRes color: Int) = ContextCompat.getColorStateList(this, color)
fun Context.getDrawableResource(@DrawableRes drawable: Int) = ContextCompat.getDrawable(this, drawable)

fun TextView.setTextColorResource(@ColorRes colorId: Int) {
    setTextColor(context.getColorResource(colorId))
}

fun MaterialCardView.setCardBackgroundColorResource(colorId: Int) {
    setCardBackgroundColor(
        context.getColorResource(colorId)
    )
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
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val date = sdf.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.FULL).format(date)
}