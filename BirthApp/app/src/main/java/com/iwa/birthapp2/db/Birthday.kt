package com.iwa.birthapp2.db

import java.io.Serializable
import java.sql.RowId

class Birthday : Serializable {
    companion object {
        // カラム名
        val COLUMN_ID = "_id"
        val COLUMN_NAME = "name"
        val COLUMN_AGE = "age"
        val COLUMN_BIRTHDAY = "birthday"

        var id: Int = 0
        var name: String ?= null
        var age: String ?= null
        var birthday: String ?= null
    }

    fun getId(): Int{ return id }
    fun setId(id: Int){ Birthday.id = id }

    fun getName(): String?{ return name }
    fun setName(name: String?){ Birthday.name = name }

    fun getAge(): String?{ return age }
    fun setAge(age: String){ Birthday.age = age }

    fun getBirthday(): String?{ return birthday }
    fun setBirthday(birthday: String?){ Birthday.birthday = birthday }
}
