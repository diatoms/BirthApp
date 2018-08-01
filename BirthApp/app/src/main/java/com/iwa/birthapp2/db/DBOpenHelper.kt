package com.iwa.birthapp2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class DBOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val TAG = "DbOpenHelper"
        val DATABASE_NAME = "birthday.db"
        val TABLE_NAME = "tbl_birthday"
        val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        createTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }

    private fun createTable(db: SQLiteDatabase) {
        db.beginTransaction()

        try {
            // テーブルの生成
            val createSql = StringBuilder()
            createSql.append("create table " + TABLE_NAME + " (")
            createSql.append(Birthday.COLUMN_ID + " integer primary key autoincrement not null,")
            createSql.append(Birthday.COLUMN_NAME + " text,")
            createSql.append(Birthday.COLUMN_AGE + " Integer,")
//            createSql.append(Birthday.COLUMN_BIRTHDAY + " text")
            createSql.append(Birthday.COLUMN_BIRTHDAY + " text," + "unique(" + Birthday.COLUMN_NAME + "," + Birthday.COLUMN_BIRTHDAY + ")")
            createSql.append(")")

            db.execSQL(createSql.toString())
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction()
        }
    }
}
