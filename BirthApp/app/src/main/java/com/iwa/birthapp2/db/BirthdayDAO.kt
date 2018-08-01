package com.iwa.birthapp2.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException


class BirthdayDAO{
    private val TAG: String = "BirthdayDAO"

    fun save(data: Birthday, db: SQLiteDatabase) {
        try {
            var values: ContentValues = ContentValues()
            with(values) {
                values.put(Birthday.COLUMN_NAME, data.getName())
                values.put(Birthday.COLUMN_AGE, data.getAge())
                values.put(Birthday.COLUMN_BIRTHDAY, data.getBirthday())

                var rowId: Int = data.getId()
                if (rowId == 0) {
                    rowId = (db.insertOrThrow(DBOpenHelper.TABLE_NAME, null, values)).toInt()
                } else {
                    db.update(DBOpenHelper.TABLE_NAME, values, Birthday.COLUMN_ID + "=?", arrayOf(rowId.toString()))
                }
            }
        } catch(e: SQLiteException) {

        }finally {
                //          db.endTransaction()
        }
    }

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

//        item.setId((cursor.getLong (0)) as Int)
        item.setName(cursor.getString(1))
        item.setAge(cursor.getInt(2))
        item.setBirthday(cursor.getString(3))
        return item
    }
}
