package com.iwa.birthapp2.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class BirthdayDAO{
    private var helper: DBOpenHelper ?= null
    constructor(context: Context){
        helper = DBOpenHelper(context)
    }

    fun save(data: Birthday): BirthdayDAO{
        var db:SQLiteDatabase = helper!!.writableDatabase
        var result:BirthdayDAO ?= null

        try {
            var values: ContentValues = ContentValues()
            with(values) {
                try {
                    // テーブルの生成
                    values.put(Birthday.COLUMN_ID, data.id)
                    values.put(Birthday.COLUMN_NAME + " text,")
                    values.put(Birthday.COLUMN_AGE + " Integer,")
                    values.put(Birthday.COLUMN_BIRTHDAY + " text")
                    values.put(")")

                    db.execSQL(createSql.toString())
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction()
                }
            }
        } finally {

        }
        return BirthdayDAO(c)
    }
}
