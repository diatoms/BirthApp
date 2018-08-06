package com.iwa.birthapp2.common

import android.util.Log
import com.iwa.birthapp2.BuildConfig

class LogUtil
{
    companion object {
        val debugFlag = BuildConfig.LOGGING

        //Verbose
        fun verbose (TAG: String, msg: String) {
            if (debugFlag) { Log.v(TAG, msg) }
        }
        fun verbose (TAG: String, msg: String, tr: Throwable) {
            if (debugFlag) { Log.v(TAG, msg, tr) }
        }

        //Debug
        fun debug (TAG: String, msg: String) {
            if (debugFlag) {  Log.d(TAG, msg) }
        }
        fun debug (TAG: String, msg: String, tr: Throwable){
            if (debugFlag) { Log.d(TAG, msg, tr) }
        }

        //Info
        fun info (TAG: String, msg: String) {
            if (debugFlag) { Log.i(TAG, msg) }
        }
        fun info (TAG: String, msg: String, tr: Throwable) {
            if (debugFlag) { Log.i(TAG, msg, tr) }
        }

        //Warning
        fun warning (TAG: String, msg: String) {
            if (debugFlag) { Log.w(TAG, msg) }
        }
        fun warning (TAG: String, msg: String, tr: Throwable) {
            if (debugFlag) { Log.w(TAG, msg, tr) }
        }

        //error
        fun error (TAG: String, msg: String) {
            if (debugFlag) { Log.e(TAG, msg) }
        }
        fun error (TAG: String, msg: String, tr: Throwable) {
            if (debugFlag) { Log.e(TAG, msg, tr) }
        }

    }
}
