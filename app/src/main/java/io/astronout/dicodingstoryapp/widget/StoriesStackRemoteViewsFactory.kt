package io.astronout.dicodingstoryapp.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.astronout.dicodingstoryapp.R
import io.astronout.dicodingstoryapp.domain.model.Story

internal class StoriesStackRemoteViewsFactory(
    private val context: Context
) : RemoteViewsService.RemoteViewsFactory, BroadcastReceiver() {

    private var storiesList = emptyList<Story>()

    private val updateStoriesIntentFilter = IntentFilter().apply {
        addAction(ACTION_UPDATE_STORIES)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        storiesList = intent?.getParcelableArrayListExtra(EXTRA_STORIES) ?: emptyList()
        onDataSetChanged()
    }

    override fun onCreate() {
        context.registerReceiver(this, updateStoriesIntentFilter)
    }

    override fun onDestroy() {
        context.unregisterReceiver(this)
    }

    override fun onDataSetChanged() {}

    override fun getCount(): Int = storiesList.size

    override fun getViewAt(position: Int): RemoteViews {
        val story = storiesList[position]

        val remoteViews = RemoteViews(context.packageName, R.layout.item_story_stack)

        remoteViews.setTextViewText(R.id.tvName, story.name)

        val storyPhotoBitmap = Glide
            .with(context)
            .asBitmap()
            .apply(RequestOptions().override(200, 200))
            .load(story.photoUrl)
            .submit()
            .get()

        remoteViews.setImageViewBitmap(R.id.ivImage, storyPhotoBitmap)

        val extras = bundleOf(StoriesStackWidget.EXTRA_ITEM to story.name)

        remoteViews.setOnClickFillInIntent(
            R.id.ivImage,
            Intent().apply { putExtras(extras) }
        )

        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    companion object {
        const val ACTION_UPDATE_STORIES = "action_update_stories"
        const val EXTRA_STORIES = "extra_stories"
    }
}