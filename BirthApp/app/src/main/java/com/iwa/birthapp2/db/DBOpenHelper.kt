package com.iwa.birthapp2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        createTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }

    private fun createTable(db: SQLiteDatabase) {
        // テーブル作成SQL
        val sql = ("CREATE TABLE schedules ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name TEXT,"
                + " birthday TEXT,"
                + " age INTEGER,"
                + " request_cdde INTEGER"
                + ");")
        db.execSQL(sql)
    }

    companion object {
        private val TAG = "DbOpenHelper"
        private val DATABASE_NAME = "birthday.db"
        private val SCHEDULE_TABLE_NAME = "birthday"
        private val DATABASE_VERSION = 1
    }
}
