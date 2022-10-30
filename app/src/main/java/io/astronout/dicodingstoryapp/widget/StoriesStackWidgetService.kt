package io.astronout.dicodingstoryapp.widget

import android.content.Intent
import android.widget.RemoteViewsService

class StoriesStackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StoriesStackRemoteViewsFactory(this.applicationContext)
}