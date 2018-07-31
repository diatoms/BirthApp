package com.iwa.birthapp2.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import kotlin.math.round

class BirthdayDAO{

    private var helper: DBOpenHelper ?= null
    constructor(context: Context){
        helper = DBOpenHelper(context)
    }

    fun save(data: Birthday): Birthday?{
        var db:SQLiteDatabase = helper!!.writableDatabase
        var result:Birthday ?= null

        try {
            var values: ContentValues = ContentValues()
            with(values) {
                    // テーブルの生成
                    values.put(Birthday.COLUMN_ID, data.getId())
                    values.put(Birthday.COLUMN_NAME, data.getName())
                    values.put(Birthday.COLUMN_AGE, data.getAge())
                    values.put(Birthday.COLUMN_BIRTHDAY, data.getBirthday())

                    var rowId: Int = data.getId()
                    if(rowId == 0){
                        rowId = (db.insert(DBOpenHelper.TABLE_NAME, null, values)).toInt()
                    } else {
                        db.update(DBOpenHelper.TABLE_NAME, values, Birthday.COLUMN_ID + "=?", arrayOf(rowId.toString()))
                    }
                result = load(rowId)
            }
        } finally {
            db.endTransaction()
        }
        return result
    }

    fun load(itemId: Int): Birthday? {
        val db = helper!!.getReadableDatabase()
        var number: Birthday? = null
        try {
            val query = "select * " +
                    " from " + DBOpenHelper.TABLE_NAME +
                    " where " + Birthday.COLUMN_ID + " = '" + itemId + "';"
            val cursor = db.rawQuery(query, null)
            cursor.moveToFirst()
            number = getItem(cursor)
        } finally {
            db.close()
        }
        return number
    }

    private fun getItem(cursor: Cursor): Birthday {
        val item = Birthday()

        item.setId((cursor.getLong (0)) as Int)
        item.setName(cursor.getString(1))
        item.setAge(cursor.getString(2))
        item.setBirthday(cursor.getString(3))
        return item
    }
}
