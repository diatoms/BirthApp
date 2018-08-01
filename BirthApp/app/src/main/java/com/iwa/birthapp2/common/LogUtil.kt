package com.iwa.birthapp2.common

import android.util.Log

class LogUtil
{
    companion object {
        val debugFlag = true //TODO gradleに移す

        fun debug (TAG: String, msg: String) {
            if (debugFlag) {
                Log.d(TAG, msg)
            }
        }

        fun warning (TAG: String, msg: String, e:Throwable) {
            if (debugFlag) {
                Log.w(TAG, msg, e)
            }
        }

        fun error (TAG: String, msg: String, e:Throwable) {
            if (debugFlag) {
                Log.e(TAG, msg, e)
            }
        }
    }
}
