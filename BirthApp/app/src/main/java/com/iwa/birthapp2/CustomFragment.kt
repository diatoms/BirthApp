package com.iwa.birthapp2

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ProgressBar
import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Handler
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog


class CustomFragment : DialogFragment() {
    private var listener: OnCustomFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCustomFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnCustomFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnCustomFragmentInteractionListener {
        // TODO: Update argument type and name
        fun OnCustomFragmentInteractionListener(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(message: String) =
                CustomFragment().apply {
                    arguments = Bundle().apply {
                        putString("message", message)
                    }
                }

        private val DELAY_MILLISECOND = 450
        private val SHOW_MIN_MILLISECOND = 300
    }

    private var mProgressBar: ProgressBar ?= null
    private var mProgressMessage: TextView ?= null
    private var mStartedShowing: Boolean = false
    private var mStartMillisecond: Long = 0
    private var mStopMillisecond: Long = 0
    private var mMessage: String? = null


    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mMessage = arguments.getString("message")

        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater

        builder.setView(inflater.inflate(R.layout.fragment_custom, null))
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        mProgressBar = dialog.findViewById(R.id.progress)
        mProgressMessage = dialog.findViewById(R.id.progress_message)
        mProgressMessage?.text = mMessage
    }

    override fun show(manager: FragmentManager, tag: String) {
        mStartMillisecond = System.currentTimeMillis()
        mStartedShowing = false
        mStopMillisecond = java.lang.Long.MAX_VALUE

        val handler = Handler()
        handler.postDelayed(Runnable {
            if (mStopMillisecond > System.currentTimeMillis()) {
                showDialogAfterDelay(manager, tag)
            }
        }, DELAY_MILLISECOND.toLong())
    }

    private fun showDialogAfterDelay(manager: FragmentManager, tag: String) {
        mStartedShowing = true
        super.show(manager, tag)
    }

    fun cancel() {
        mStopMillisecond = System.currentTimeMillis()

        if (mStartedShowing) {
            if (mProgressBar != null) {
                cancelWhenShowing()
            } else {
                cancelWhenNotShowing()
            }
        }
    }

    private fun cancelWhenShowing() {
        if (mStopMillisecond < mStartMillisecond + DELAY_MILLISECOND + SHOW_MIN_MILLISECOND) {
            val handler = Handler()
            handler.postDelayed(Runnable { dismissAllowingStateLoss() }, SHOW_MIN_MILLISECOND.toLong())
        } else {
            dismissAllowingStateLoss()
        }
    }

    private fun cancelWhenNotShowing() {
        val handler = Handler()
        handler.postDelayed(Runnable { dismissAllowingStateLoss() }, SHOW_MIN_MILLISECOND.toLong())
    }

    fun setMessage(message: String) {
        if (mProgressMessage == null) {
            mProgressMessage = dialog.findViewById(R.id.progress_message)
        }
        mProgressMessage?.setText(message)
    }
}
