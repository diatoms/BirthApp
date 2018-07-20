package com.iwa.birthapp2

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.iwa.birthapp2.ItemFragment.OnListFragmentInteractionListener
import com.iwa.birthapp2.Content.ProfileItem
import kotlinx.android.synthetic.main.fragment_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [ProfileItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
        private val mValue: List<ProfileItem>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val nameList = arrayOf("田中","山田","佐藤")
    private val dateList = arrayOf("1980年05月14日","1970年08月31日","1989年02月04日")

    init {
        mOnClickListener = View.OnClickListener { v ->
            //TODO 編集画面を起動
//            val item = v.tag as ProfileItem
//            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValue[position]
        holder.mTextName.text = nameList[position]
        holder.mTextDate.text = dateList[position]

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValue.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mImager: ImageView = mView.user_img
        val mTextName: TextView = mView.name_text
        val mTextDate: TextView = mView.date_text
        val mTextAge: TextView = mView.age_text
        val mTextLeft: TextView = mView.daysleft_text
    }
}
