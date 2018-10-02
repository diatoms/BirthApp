package com.iwa.birthapp2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.support.v4.app.NotificationCompat
import com.iwa.birthapp2.common.LogUtil
import com.iwa.birthapp2.db.Birthday
import com.iwa.birthapp2.db.BirthdayDAO
import com.iwa.birthapp2.db.DBOpenHelper

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        LogUtil.debug("AlarmReceiver", "iwa")
        // カテゴリー名（通知設定画面に表示される情報）
        val name = "通知のタイトル的情報を設定"
        // システムに登録するChannelのID
        val id = "casareal_chanel"
        // 通知の詳細情報（通知設定画面に表示される情報）
        val notifyDescription = "この通知の詳細情報を設定します"

        // Channelの取得と生成
//        if (notificationManager.getNotificationChannel(id) == null) {
//            val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
//            mChannel.apply {
//                description = notifyDescription
//            }
//            notificationManager.createNotificationChannel(mChannel)
//        }

        val notification = NotificationCompat
                .Builder(context, id)
                .apply {
                    setSmallIcon(R.drawable.ic_launcher_background)
                    mContentTitle = "誕生日"
                    mContentText = intent.getStringExtra(Birthday.COLUMN_NAME) + "さんの誕生日です"
                }.build()
        notificationManager.notify(1, notification)
    }
}
