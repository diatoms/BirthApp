package com.iwa.birthapp2

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.iwa.birthapp2.ItemFragment.OnListFragmentInteractionListener
import com.iwa.birthapp2.Content.ProfileItem
import com.iwa.birthapp2.common.BirthdayUtil
import com.iwa.birthapp2.common.LogUtil
import com.iwa.birthapp2.db.Birthday
import com.iwa.birthapp2.db.DBOpenHelper
import kotlinx.android.synthetic.main.fragment_item.view.*
import java.util.*
import kotlin.collections.ArrayList
import android.R.id.edit
import android.content.SharedPreferences



/**
 * [RecyclerView.Adapter] that can display a [ProfileItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
        private val mValue: List<ProfileItem>,
        private val mListener: OnListFragmentInteractionListener?,
        private val context: Context)
    : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private var nameList: ArrayList<String> = ArrayList<String>()
    private var birthdayList: ArrayList<String> = ArrayList<String>()
    private var age: ArrayList<Int> = ArrayList<Int>()
    private var daysLeft: ArrayList<Int> = ArrayList<Int>()

    init {
        mOnClickListener = View.OnClickListener { v ->
            //TODO 編集画面を起動
//            val item = v.tag as ProfileItem
//            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        getData(parent.context)
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mValue[position]
        if(nameList.size != 0) {
            holder.mTextName.text = nameList[position]                  // 名前
            if(birthdayList[position].substring(0).equals("年")){
                holder.mTextDate.text = "-----------"
            } else {
                holder.mTextDate.text = birthdayList[position]          // 誕生日
            }

            if(age[position] != -1) {
                holder.mTextAge.text = age[position].toString() + "歳"   // 年齢
            } else {
                holder.mTextAge.text = "ーー歳"
            }
            holder.mTextDaysLeft.text = daysLeft[position].toString() + "日"   // 残日数
        }
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int {
        val data = context.getSharedPreferences("DataSave", Context.MODE_PRIVATE)
        return data.getInt("DB_COUNT", 0)
    }

//    override fun getItemCount(): Int = mValue.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mImager: ImageView = mView.user_img
        val mTextName: TextView = mView.name_text
        val mTextDate: TextView = mView.date_text
        val mTextAge: TextView = mView.age_text
        val mTextDaysLeft: TextView = mView.daysleft_text
    }

    fun getData(context: Context){
        val helper = DBOpenHelper(context)
        val db: SQLiteDatabase = helper.readableDatabase
        db.beginTransaction()

        // 全レコードを一括取得
        val cursor = db.query(DBOpenHelper.TABLE_NAME, arrayOf(Birthday.COLUMN_ID, Birthday.COLUMN_NAME, Birthday.COLUMN_AGE, Birthday.COLUMN_BIRTHDAY), null, null, null, null, null)
        var i: Int = 0
        if (cursor.moveToFirst()) {
            do {
                nameList.add(cursor.getString(1))
                age.add(cursor.getInt(2))
                birthdayList.add(BirthdayUtil.getModifiedBirthday(cursor.getString(3)))
                daysLeft.add((BirthdayUtil.getDaysBeforBirthday((cursor.getString(3))) / ((1000 * 60 * 60 * 24 ))).toInt())
                i++
            } while (cursor.moveToNext())
        }
        cursor.close()

        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    /**
     * 生年月日の年数が含まれるか確認する
     */
    fun checkYear(birthDay: String){

    }
}
