package com.iwa.birthapp2.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class BirthdayModel{
    private var helper: DBOpenHelper ?= null
    constructor(context: Context){
        helper = DBOpenHelper(context)
    }

    fun save(data: BirthdayModel): BirthdayModel{
        var db:SQLiteDatabase = helper!!.writableDatabase
        var result:BirthdayModel ?= null

        try {
            var values: ContentValues = ContentValues()
            with(values){
                put()
            }
        }
    }
}
