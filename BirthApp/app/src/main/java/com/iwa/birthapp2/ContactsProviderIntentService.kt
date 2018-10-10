package com.iwa.birthapp2

import android.app.IntentService
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.util.Log
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import com.iwa.birthapp2.common.BirthdayUtil
import com.iwa.birthapp2.common.LogUtil
import com.iwa.birthapp2.db.Birthday
import com.iwa.birthapp2.db.BirthdayDAO
import com.iwa.birthapp2.db.DBOpenHelper
import java.io.IOException
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.time.MonthDay
import java.util.*
import android.R.id.edit
import android.content.SharedPreferences




/**
 * 電話帳連携Service
 * 　　★年齢が一年ずれる場合があるので要修正
 * 　　★通知を返してもらう
 */
class ContactsProviderIntentService : IntentService(ContactsProviderIntentService::class.java.simpleName), Loader.OnLoadCompleteListener<Cursor> {

    val TAG: String = "ContactsProviderIntentService"

    // Redefined Query
    private val contentUri = ContactsContract.Data.CONTENT_URI
    private val projection = arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY, ContactsContract.Contacts.PHOTO_THUMBNAIL_URI, ContactsContract.Contacts.PHOTO_URI, ContactsContract.CommonDataKinds.Event.START_DATE, ContactsContract.CommonDataKinds.Event.TYPE)
    private val selection = ContactsContract.Data.MIMETYPE + "= ? AND (" +
            ContactsContract.CommonDataKinds.Event.TYPE + "=" +
            ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY + " OR " +
            ContactsContract.CommonDataKinds.Event.TYPE + "=" +
            ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY +
            " ) "
    private val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
    protected var orderBy: String? = null
    private var mCursorLoader: CursorLoader? = null

    private var id: Long = 0
    private var loopKey: String? = null
    private var display_name: String? = null
    private var birthDay: String? = null

    override fun onHandleIntent(intent: Intent?) {
        mCursorLoader = CursorLoader(
                this,
                contentUri,
                projection,
                selection,
                selectionArgs,
                orderBy)
        mCursorLoader!!.registerListener(LOADER_ID_NETWORK, this)
        mCursorLoader!!.startLoading()
    }

    override fun onLoadComplete(loader: Loader<Cursor>, cursor: Cursor) {
        LogUtil.debug(TAG, "onLoadComplete")
        val context: Context = applicationContext

        val helper = DBOpenHelper(context)
        var db: SQLiteDatabase = helper.readableDatabase
        db.delete(DBOpenHelper.TABLE_NAME, null, null)

        db = helper.writableDatabase
        db.beginTransaction()
        val birthDaydao = BirthdayDAO()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            loopKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
            display_name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))

            try {
                when (cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE))) {
                    ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY -> birthDay =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE))
                    ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY -> {
                        // TODO 必要なら入れる
                    }
                }
            } catch (e: Exception) {
                LogUtil.error(TAG, "Birthday can't be extract : ", e)
            } finally {

            }

            if (birthDay != null) {
                LogUtil.debug(TAG, display_name + " " + birthDay)
                with(Birthday()){
                    setName(display_name)
                    setAge(getAge(birthDay!!))
                    setBirthday(birthDay)
                    birthDaydao.save(this,db)
                }
            }
            // Go to next contact
            cursor.moveToNext()
            //TODO photo
        }
        LogUtil.debug(TAG,"DB数:" + cursor.count.toString())
        val data = getSharedPreferences("DataSave", Context.MODE_PRIVATE)
        val editor = data.edit()
        editor.putInt("DB_COUNT", cursor.count)
        editor.apply()

        cursor.close()
        db.setTransactionSuccessful()
        db.endTransaction()

        // Stop the cursor loader
        if (mCursorLoader != null) {
            mCursorLoader!!.unregisterListener(this)
            mCursorLoader!!.cancelLoad()
            mCursorLoader!!.stopLoading()
        }

        // 登録ずみのアラームをキャンセル後に再登録
        CancelAlarmTask().execute()
        SetAlarmTask().execute()
        sendMessage("")
    }

    // Activityに戻すタイミングで呼ぶ
    fun sendMessage(msg: String) {
        val broadcast = Intent()
        broadcast.putExtra("message", msg)
        broadcast.action = packageName + ".ON_LOAD_COMPLETED_RECEIVER"
        baseContext.sendBroadcast(broadcast)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    companion object {
        // Cursor and id
        private val LOADER_ID_NETWORK = 1549
    }


    /**
     * 誕生日までの日数を取得する
     * @param birthDay: 誕生日
     * @return 次の誕生日までの日数(ミリ秒単位)
     */
    fun getNextAnniversary(birthDay: String): Long {
        var month: Int = 0
        val date: Int = birthDay.substring(birthDay.length-2,birthDay.length).toInt()

        if (BirthdayUtil.containsDelimiter(birthDay)) {
            // 区切り文字あり(YYYY.MM.DD, YY.MM.DD, MM.DD)
            month = birthDay.substring(birthDay.length-5,birthDay.length-3).toInt()
        } else {
            // 区切り文字なし(YYYYMMDD, YYMMDD, MMDD)
            month = birthDay.substring(birthDay.length-4,birthDay.length-2).toInt()
        }

        var year: Int = (Calendar.getInstance()).get(Calendar.YEAR) + 1
        val now_month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1
        val now_day: Int = Calendar.getInstance().get(Calendar.DATE)

        if(now_month < month.toInt()){
            year = year - 1
        } else if(now_month == month.toInt()){
            if(now_day < date.toInt()){
                year = year - 1
            }
        }

        val calenadar: Calendar = Calendar.getInstance()
        calenadar.set(year, month - 1, date)

        return calenadar.timeInMillis - Calendar.getInstance().timeInMillis
    }


    /**
     * 年齢取得
     * @param birthDay 誕生日
     * @return 年齢（値が不正な場合は-1を返す）
     */
    private fun getAge(birthDay: String): Int{
        var age: Int = -1

        if (BirthdayUtil.containsDelimiter(birthDay)) {
            // 区切り文字あり(YYYY.MM.DD, YY.MM.DD, MM.DD)
            val day: String = birthDay.substring(birthDay.length-2,birthDay.length)
            val month: String = birthDay.substring(birthDay.length-5,birthDay.length-3)
            var year: String = "0"
            if(birthDay.length > 5) {
                // 年あり(YYYY.MM.DD, YY.MM.DD)

                // 区切り文字チェック("-" or "/" or ".")
                if (birthDay.indexOf("-") >= 0) {
                    year = birthDay.substring(0, birthDay.indexOf("-"))
                } else if (birthDay.indexOf("/") >= 0) {
                    year = birthDay.substring(0, birthDay.indexOf("/"))
                } else if (birthDay.indexOf(".") >= 0) {
                    year = birthDay.substring(0, birthDay.indexOf("."))
                }

                if (year.length == 4) {
                    // 年表示4桁
                    age = (Calendar.getInstance()).get(Calendar.YEAR) - year.toInt()
                } else if (year.length == 2) {
                    // 年表示2桁
                    age = (Calendar.getInstance()).get(Calendar.YEAR) - 1900 - year.toInt()
                    if (age >= 100) {
                        //100歳超えの場合は2000年代生まれとする
                        age = age - 100
                    }
                }

                val now_month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1
                val now_day: Int = Calendar.getInstance().get(Calendar.DATE)

                if(age != -1){
                    if(now_month < month.toInt()){
                        age = age - 1
                    } else if(now_month == month.toInt()){
                        if(now_day < day.toInt()){
                            age = age - 1
                        }
                    }
                }

            } else {
                // 年なし(MM.DD)　処理なし
            }
        } else {
            // 区切り文字なし(YYYYMMDD, YYMMDD, MMDD)
            val day: String = birthDay.substring(birthDay.length-2,birthDay.length-1)
            val month: String = birthDay.substring(birthDay.length-4,birthDay.length-3)
            if(birthDay.length > 5) {
                // 年あり(YYYYMMDD, YYMMDD)

                if (birthDay.length == 8) {
                    // 年表示4桁
                    var year: String = birthDay.substring(0, 3)
                    age = (Calendar.getInstance()).get(Calendar.YEAR) - year.toInt()
                } else if (birthDay.length == 6) {
                    // 年表示2桁
                    var year: String = birthDay.substring(0, 1)
                    age = (Calendar.getInstance()).get(Calendar.YEAR) - 1900 - year.toInt()
                    if (age >= 100) {
                        //100歳超えの場合は2000年代生まれとする
                        age = age - 100
                    }
                }

            } else {
                // 年なし(MM.DD)　処理なし
            }
        }
        return age

//        getNextAnniversary(birthDay)
//        if (containsDelimiter(birthDay)) {
//            // 区切り文字あり
//            try{
//                if(birthDay.length >= 5){
//                    // 年が2桁 or 4桁区の場合
//                    if(birthDay.indexOf("-") >= 0) {
//                        age = (birthDay.substring(0, birthDay.indexOf("-"))).toInt()
//                    }
//
//                    if(birthDay.indexOf("/") >= 0) {
//                        age = (birthDay.substring(0, birthDay.indexOf("/"))).toInt()
//                    }
//
//                    if(birthDay.indexOf(".") >= 0) {
//                        age = (birthDay.substring(0, birthDay.indexOf("."))).toInt()
//                    }
//
//                    if(age < 100){
//                        // 2桁の場合は2000年以降と判断
//                        return (Calendar.getInstance()).get(Calendar.YEAR) - age - 2000
//                    } else {
//                        return (Calendar.getInstance()).get(Calendar.YEAR) - age
//                    }
//                }
//            } catch(e: NumberFormatException){
//                LogUtil.error(TAG, "誕生日データ不正" ,e)
//            }
//        } else {
//            // 区切り文字なし
//            if(birthDay.length >= 5){
//                if(birthDay.length >= 7 ){
//                    //年が4桁
//                    age = Integer.parseInt(birthDay.substring(0,4))
//                } else {
//                    //年が2桁
//                    age = Integer.parseInt(birthDay.substring(0, 2))
//                }
//
//                if(age < 100){
//                    // 2桁の場合は2000年以降と判断
//                    return (Calendar.getInstance()).get(Calendar.YEAR) - age - 2000
//                } else {
//                    return (Calendar.getInstance()).get(Calendar.YEAR) - age
//                }
//            }
//        }
//        return age
    }


    /**
     * アラームセット
     */
    private inner class SetAlarmTask : AsyncTask<Int, Int, Int>() {
        override fun doInBackground(vararg params: Int?): Int? {
            val context: Context = applicationContext
            val helper = DBOpenHelper(context)
            val db: SQLiteDatabase = helper.readableDatabase
            db.beginTransaction()

            // 全レコードを一括取得
            val cursor = db.query(DBOpenHelper.TABLE_NAME, arrayOf(Birthday.COLUMN_ID, Birthday.COLUMN_NAME, Birthday.COLUMN_AGE, Birthday.COLUMN_BIRTHDAY), null, null, null, null, null)
            if (cursor.moveToFirst()) {
                do {
                    LogUtil.debug(TAG, cursor.getInt(0).toString() + " " + cursor.getString(1) + " " + cursor.getInt(2) + " " + cursor.getString(3))
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(context, AlarmReceiver::class.java)
                    intent.putExtra(Birthday.COLUMN_NAME, cursor.getInt(1))
                    val time: Long = getNextAnniversary(cursor.getString(3))
                    val pendingIntent : PendingIntent = PendingIntent.getBroadcast(context, cursor.getInt(0), intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
                } while (cursor.moveToNext())
            }
            cursor.close()

            db.setTransactionSuccessful()
            db.endTransaction()
            db.close()

//            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            val intent = Intent(context, AlarmReceiver::class.java)
//            val pendingIntent : PendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, 1000*20, pendingIntent)

            return null
        }
    }


    /**
     * アラームキャンセル
     */
    private inner class CancelAlarmTask : AsyncTask<Int, Int, Int>() {
        override fun doInBackground(vararg params: Int?): Int? {
            val context: Context = applicationContext
            val helper = DBOpenHelper(context)
            val db: SQLiteDatabase = helper.readableDatabase
            db.beginTransaction()

            // 全レコードを一括取得
            val cursor = db.query(DBOpenHelper.TABLE_NAME, arrayOf("_id","birthday"), null, null, null, null, null)
            if (cursor.moveToFirst()) {
                do {
                    LogUtil.debug(TAG, cursor.getInt(0).toString() + " " + cursor.getString(1))
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(context, ContactsProviderIntentService::class.java)
                    intent.putExtra("ID",cursor.getInt(0))
                    val pendingIntent : PendingIntent = PendingIntent.getBroadcast(context, cursor.getInt(0), intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.cancel(pendingIntent)
                } while (cursor.moveToNext())
            }
            cursor.close()

            db.setTransactionSuccessful()
            db.endTransaction()
            db.close()

            return null
        }
    }
}
