package com.iwa.birthapp2

import android.widget.ImageView
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object Content {

    /**
     * An array of sample (dummy) items.
     */
    val ITEM: MutableList<ProfileItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, ProfileItem> = HashMap()

    private val COUNT = 3

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    private fun addItem(item: ProfileItem) {
        ITEM.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createDummyItem(position: Int): ProfileItem {
        return ProfileItem(position.toString(),
                        "名前",
                        "20XX年XX月○○日",
                        "3X歳",
                        "100日")
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class ProfileItem(val id: String, val name: String, val date: String, val age: String, val left: String) {
//        override fun toString(): String = content
    }
}
