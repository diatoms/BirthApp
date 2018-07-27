package com.iwa.birthapp2.db

import java.io.Serializable

class Birthday : Serializable {
    companion object {
        // カラム名
        val COLUMN_ID = "_id"
        val COLUMN_NAME = "name"
        val COLUMN_AGE = "age"
        val COLUMN_BIRTHDAY = "birthday"

        // プロパティ
        var id: Int = 0
        var name: String ?= null
        var age: String ?= null
        var birthday: String ?= null
    }
}
