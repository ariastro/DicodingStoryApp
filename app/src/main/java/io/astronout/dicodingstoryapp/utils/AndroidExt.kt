package io.astronout.dicodingstoryapp.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.View
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
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.card.MaterialCardView
import com.kennyc.view.MultiStateView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

fun wait(delay: Long = 300, action: () -> Unit) =
    Handler(Looper.getMainLooper()).postDelayed(action, delay)

private val shimmer =
    Shimmer.AlphaHighlightBuilder()
        .setDuration(1800)
        .setBaseAlpha(0.7F)
        .setHighlightAlpha(0.6F)
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setAutoStart(true)
        .build()

val shimmerDrawable = ShimmerDrawable().apply {
    setShimmer(shimmer)
}

fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
        Locale.getDefault()
    ) else it.toString()
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

fun View.onClick(block: View.OnClickListener) = setOnClickListener(block)

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