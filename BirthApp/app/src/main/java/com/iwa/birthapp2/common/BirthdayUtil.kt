package com.iwa.birthapp2.common

import java.text.SimpleDateFormat
import java.util.*

class BirthdayUtil{

    companion object {
        /**
         * 誕生日までの時間(ミリ秒)を取得
         */
        fun getDaysBeforBirthday(birthday: String): Long {
            var month: Int = 0
            val date: Int = birthday.substring(birthday.length - 2, birthday.length).toInt()

            if (containsDelimiter(birthday)) {
                // 区切り文字あり(YYYY.MM.DD, YY.MM.DD, MM.DD)
                month = birthday.substring(birthday.length - 5, birthday.length - 3).toInt()
            } else {
                // 区切り文字なし(YYYYMMDD, YYMMDD, MMDD)
                month = birthday.substring(birthday.length - 4, birthday.length - 2).toInt()
            }

            var year: Int = (Calendar.getInstance()).get(Calendar.YEAR) + 1
            val now_month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1
            val now_day: Int = Calendar.getInstance().get(Calendar.DATE)

            if (now_month < month.toInt()) {
                year = year - 1
            } else if (now_month == month.toInt()) {
                if (now_day < date.toInt()) {
                    year = year - 1
                }
            }

            val calenadar: Calendar = Calendar.getInstance()
            calenadar.set(year, month - 1, date)

            return calenadar.timeInMillis - Calendar.getInstance().timeInMillis
        }

        /**
         * 誕生日区切り文字有無チェック
         * @param birthDay 誕生日
         * @return true:区切り文字あり, false:区切り文字なし
         */
        fun containsDelimiter(birthDay: String): Boolean {
            // 日付のフォーマット定義
            val birthdayFormats = listOf(
                    SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE), SimpleDateFormat("yyyyMMdd", Locale.JAPANESE),
                    SimpleDateFormat("yyyy.MM.dd", Locale.JAPANESE), SimpleDateFormat("yy-MM-dd", Locale.JAPANESE),
                    SimpleDateFormat("yyMMdd", Locale.JAPANESE), SimpleDateFormat("yy.MM.dd", Locale.JAPANESE),
                    SimpleDateFormat("yy/MM/dd", Locale.JAPANESE), SimpleDateFormat("MM-dd", Locale.JAPANESE),
                    SimpleDateFormat("MMdd", Locale.JAPANESE), SimpleDateFormat("MM/dd", Locale.JAPANESE), SimpleDateFormat("MM.dd", Locale.JAPANESE)
            )

            for (sdf in birthdayFormats) {
                try {
                    if ((birthDay.matches(Regex(".*" + "-" + ".*")))
                            || (birthDay.matches(Regex(".*" + "/" + ".*")))
                            || (birthDay.matches(Regex(".*" + "." + ".*")))) {
                        // 「-/.」の区切り文字を含む場合
                        return true
                    }
                } catch (e: Exception) {
                    continue;
                }
            }
            // 「-/.」の区切り文字を含まない場合
            return false;
        }

        /**
         * 表示用に整形した誕生日（XXXX年XX月XX日）を取得
         */
        fun getModifiedBirthday(birthday: String): String {
            if (containsDelimiter(birthday)) {
                // 区切り文字あり(YYYY.MM.DD, YY.MM.DD, MM.DD)
                val day: String = birthday.substring(birthday.length - 2, birthday.length)
                val month: String = birthday.substring(birthday.length - 5, birthday.length - 3)
                var year: String = "0"
                if (birthday.length > 5) {
                    // 年あり(YYYY.MM.DD, YY.MM.DD)

                    // 区切り文字チェック("-" or "/" or ".")
                    if (birthday.indexOf("-") >= 0) {
                        year = birthday.substring(0, birthday.indexOf("-"))
                    } else if (birthday.indexOf("/") >= 0) {
                        year = birthday.substring(0, birthday.indexOf("/"))
                    } else if (birthday.indexOf(".") >= 0) {
                        year = birthday.substring(0, birthday.indexOf("."))
                    }

                    if(year.length == 2) {
                        // 年表示2桁
                        val age = (Calendar.getInstance()).get(Calendar.YEAR) - 1900 - year.toInt()
                        if (age >= 100) {
                            //100歳超えの場合は2000年代生まれとする
                            year = "20" + year
                        } else {
                            year = "19" + year
                        }
                    }

                    return year + "年" + month + "月" + day + "日"
                } else {
                    // 年なし(MM.DD)　処理なし
                    return ""
                }
            } else {
                // 区切り文字なし(YYYYMMDD, YYMMDD, MMDD)
                val day: String = birthday.substring(birthday.length - 2, birthday.length - 1)
                val month: String = birthday.substring(birthday.length - 4, birthday.length - 3)
                var year: String = "0"
                if (birthday.length > 5) {
                    // 年あり(YYYYMMDD, YYMMDD)

                    if (birthday.length == 8) {
                        // 年表示4桁
                        year = birthday.substring(0, 3)
                    } else if (birthday.length == 6) {
                        // 年表示2桁
                        year = birthday.substring(0, 1)
                        val age = (Calendar.getInstance()).get(Calendar.YEAR) - 1900 - year.toInt()
                        if (age >= 100) {
                            //100歳超えの場合は2000年代生まれとする
                            year = "20" + year
                        } else {
                            year = "19" + year
                        }
                    }

                    return year + "年" + month + "月" + day + "日"
                } else {
                    // 年なし(MM.DD)　処理なし
                    return ""
                }
            }
        }
    }
}