package com.shivichu.passwordsaver.receiver

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Intent
import com.shivichu.passwordsaver.R
import com.shivichu.passwordsaver.view.activity.BaseHomeActivity
import android.os.Bundle
import android.app.PendingIntent
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.shivichu.passwordsaver.database.entity.Todo

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("Tag","On Received")
        showNotification(context,intent)
    }

    fun showNotification(context: Context, intent: Intent) {

        var receivedData : Todo? = null
        if(intent.hasExtra("todo")) {
            Log.e("Extra",intent.getStringExtra("todo")!!)
            val conv = Gson().fromJson(intent.getStringExtra("todo"), Todo::class.java)
            if(conv!=null) {
                receivedData = conv
            }
        }

        val CHANNEL_ID = "Todo_Test_channel" // The id of the channel.
        val name: CharSequence = context.resources.getString(R.string.app_name) // The user-visible name of the channel.
        val mBuilder: NotificationCompat.Builder
        val notificationIntent = Intent(context, BaseHomeActivity::class.java)
        val bundle = Bundle()
        notificationIntent.putExtras(bundle)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mBuilder = if (Build.VERSION.SDK_INT >= 26) {
            val mChannel =
                NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(mChannel)
            NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLights(Color.RED, 300, 300)
                .setChannelId(CHANNEL_ID)
                .setContentTitle(receivedData?.title?:"Title")
        } else {
            NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentTitle(receivedData?.title?:"Title")
        }
        mBuilder.setContentIntent(contentIntent)
        mBuilder.setContentText(receivedData?.description?:"Description comes here")
        mBuilder.setAutoCancel(true)
        mNotificationManager.notify(1, mBuilder.build())
    }
}