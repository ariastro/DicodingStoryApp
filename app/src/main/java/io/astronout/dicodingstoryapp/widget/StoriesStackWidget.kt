package io.astronout.dicodingstoryapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.net.toUri
import dagger.hilt.android.AndroidEntryPoint
import io.astronout.dicodingstoryapp.R
import io.astronout.dicodingstoryapp.domain.StoryUsecase
import io.astronout.dicodingstoryapp.utils.showToast
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class StoriesStackWidget : AppWidgetProvider() {

    @Inject
    lateinit var storyUsecase: StoryUsecase

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
            getAllStories(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action != null && intent.action == TOAST_ACTION) {
            val name = intent.getStringExtra(EXTRA_ITEM)
            context?.showToast(context.getString(R.string.label_user_story, name))
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val intent = Intent(context, StoriesStackWidgetService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            data = this.toUri(Intent.URI_INTENT_SCHEME).toUri()
        }

        val remoteViews = RemoteViews(context.packageName, R.layout.stories_stack_widget)
        remoteViews.setRemoteAdapter(R.id.svStories, intent)
        remoteViews.setEmptyView(R.id.svStories, R.id.tvNoData)

        val toastIntent = Intent(context, StoriesStackWidget::class.java).apply {
            action = TOAST_ACTION
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        else 0

        val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, flag)
        remoteViews.setPendingIntentTemplate(R.id.svStories, toastPendingIntent)
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    private val supervisorJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + supervisorJob)
    private var getAllStoriesJob: Job? = null

    private fun getAllStories(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        getAllStoriesJob?.cancel()
        getAllStoriesJob = coroutineScope.launch {
            storyUsecase.getStories().collect {
                when (it) {
                    is Resource.Success -> {
                        Intent(StoriesStackRemoteViewsFactory.ACTION_UPDATE_STORIES).apply {
                            putParcelableArrayListExtra(
                                StoriesStackRemoteViewsFactory.EXTRA_STORIES, ArrayList(it.data)
                            )
                        }.run {
                            context.sendBroadcast(this)
                            appWidgetManager.notifyAppWidgetViewDataChanged(
                                appWidgetId,
                                R.id.svStories
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    companion object {
        private const val TOAST_ACTION = "io.astronout.dicodingstoryapp.TOAST_ACTION"
        const val EXTRA_ITEM = "EXTRA_ITEM"
    }
}