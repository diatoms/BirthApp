package com.iwa.birthapp2.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.iwa.birthapp2.common.LogUtil


class BirthdayDAO{
    private val TAG: String = "BirthdayDAO"

    /**
     * DB保存
     *
     * @param
     * @param
     */
    fun save(data: Birthday, db: SQLiteDatabase) {
        try {
            val values = ContentValues()
            with(values) {
                values.put(Birthday.COLUMN_NAME, data.getName())
                values.put(Birthday.COLUMN_AGE, data.getAge())
                values.put(Birthday.COLUMN_BIRTHDAY, data.getBirthday())
            }

            val rowId: Int = data.getId()
            if (rowId == 0) {
                db.insertOrThrow(DBOpenHelper.TABLE_NAME, null, values)
            } else {
                db.update(DBOpenHelper.TABLE_NAME, values, Birthday.COLUMN_ID + "=?", arrayOf(rowId.toString()))
            }

        } catch (e: SQLiteException) {
            LogUtil.warning(TAG, "Database save failure", e)
        }
    }

    /**
     * DB読み込み
     */
    fun load(itemId: Int, db:SQLiteDatabase): Birthday? {
        var birthday: Birthday? = null
        try {
            val query = "select * " +
                    " from " + DBOpenHelper.TABLE_NAME +
                    " where " + Birthday.COLUMN_ID + " = '" + itemId + "';"
            val cursor = db.rawQuery(query, null)
            cursor.moveToFirst()
            birthday = getItem(cursor)
        } finally {
//            db.close()
        }
        return birthday
    }

    private fun getItem(cursor: Cursor): Birthday {
        val item = Birthday()
        item.setName(cursor.getString(1))
        item.setAge(cursor.getInt(2))
        item.setBirthday(cursor.getString(3))
        return item
    }
}
