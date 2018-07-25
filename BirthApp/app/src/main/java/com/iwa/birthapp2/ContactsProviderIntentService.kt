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
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.time.MonthDay
import java.util.*



/**
 * Created by joker on 20/04/17.
 */

class ContactsProviderIntentService : IntentService(ContactsProviderIntentService::class.java.simpleName), Loader.OnLoadCompleteListener<Cursor> {

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
                Log.e(javaClass.simpleName, "Birthday can't be extract : " + e.message)
            } finally {

            }

            if (birthDay != null) {
//                NotificationEventReceiver.setupAlarm(this, contact)
                Log.d("ContactsProviderService", display_name + " " + birthDay)
                setupAlarm(applicationContext, birthDay!!)
            }
            // Go to next contact
            cursor.moveToNext()
            //TODO photo
        }
        cursor.close()

        // Stop the cursor loader
        if (mCursorLoader != null) {
            mCursorLoader!!.unregisterListener(this)
            mCursorLoader!!.cancelLoad()
            mCursorLoader!!.stopLoading()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    companion object {
        // Cursor and id
        private val LOADER_ID_NETWORK = 1549
    }

    fun setupAlarm(context: Context, birthDay: String) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ContactsProviderIntentService::class.java)
//        intent.action = ACTION_START_NOTIFICATION_SERVICE
//        intent.putExtra(EXTRA_CONTACT_NOTIFICATION, contact)
        val pendingIntent : PendingIntent = PendingIntent.getBroadcast(context, SecureRandom().nextInt(10000000), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAnniversary(birthDay), pendingIntent)
    }

    fun getNextAnniversary(birthDay: String): Long {
        var year: Int = (Calendar.getInstance()).get(Calendar.YEAR) + 1
        var month: Int = 0
        var date: Int = 0
        if (containsDelimiter(birthDay)) {
            // 区切り文字あり
            try{
                month = Integer.parseInt((birthDay.substring(5)).substring(0, 2))
                date = Integer.parseInt((birthDay.substring(5)).substring(3, 5))
            } catch(e: NumberFormatException){
                //TODO エラー時にどうするか決める
            }
        } else {
            // 区切り文字なし
            birthDay.substring(4)
        }

        var calenadar: Calendar = Calendar.getInstance()
        calenadar.set(year, month, date)

        return calenadar.timeInMillis - Calendar.getInstance().timeInMillis

//        birthDay.substring(birthDay.length - 5)
//        birthDay.substring(birthDay.length - 2)
//
//        val dateTimeNow = DateTime.now()
//        val monthDayNow = MonthDay.now()
//        val monthDayOfNextDate = MonthDay.fromDateFields(date)
//        if (monthDayNow.isEqual(monthDayOfNextDate)) {
//            val inputDate = DateTime(date)
//            return dateTimeNow
//                    .withHourOfDay(inputDate.getHourOfDay())
//                    .withMinuteOfHour(inputDate.getMinuteOfHour())
//                    .withSecondOfMinute(inputDate.getSe
// condOfMinute())
//                    .withMillisOfSecond(inputDate.getMillisOfSecond()).toDate()
//        }
//        if (monthDayNow.isBefore(monthDayOfNextDate))
//            return DateTime(date).withYear(dateTimeNow.getYear()).toDate()
//        else {
//            val dateTimeOfNextDate = DateTime(date).withYear(dateTimeNow.getYear()).plusYears(1)
//            return dateTimeOfNextDate.toDate()
//        }
    }

    /**
     * フォーマットチェック
     * 戻り値true:区切り文字を含む, false:含まない
     */
    fun containsDelimiter(birthDay: String) : Boolean{
        val birthdayFormats = listOf(
                SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE), SimpleDateFormat("yyyyMMdd", Locale.JAPANESE),
                SimpleDateFormat("yyyy.MM.dd", Locale.JAPANESE), SimpleDateFormat("yy-MM-dd", Locale.JAPANESE),
                SimpleDateFormat("yyMMdd", Locale.JAPANESE), SimpleDateFormat("yy.MM.dd", Locale.JAPANESE),
                SimpleDateFormat("yy/MM/dd", Locale.JAPANESE), SimpleDateFormat("MM-dd", Locale.JAPANESE),
                SimpleDateFormat("MMdd", Locale.JAPANESE), SimpleDateFormat("MM/dd", Locale.JAPANESE), SimpleDateFormat("MM.dd", Locale.JAPANESE)
        )

        var date: Date? = null
        for (sdf in birthdayFormats) {
            try {
                date = sdf.parse(birthDay)
                if (date != null) {
                    if(birthDay.matches(Regex(".*" + "-" + ".*"))) {
                        return true
                    } else if(birthDay.matches(Regex(".*" + "/" + ".*"))) {
                        return true
                    } else if(birthDay.matches(Regex(".*" + "." + ".*"))) {
                        return true
                    } else {
                        return false
                    }
                }
            } catch (e: Exception) {
                continue;
            }
        }
        return false;
    }
}
