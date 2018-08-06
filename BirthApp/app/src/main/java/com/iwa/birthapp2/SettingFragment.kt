package com.iwa.birthapp2

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.provider.ContactsContract.CommonDataKinds
import android.support.constraint.Constraints
import android.util.Log


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SettingFragment.OnSettingFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnSettingFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

//
//        val c1 = GetContacts()
//        c1?.moveToFirst()
//        while (c1!!.moveToNext()) {
//            var contract_id = c1.getString(c1.getColumnIndex(ContactsContract.Contacts._ID))
//            val c2 = GetContactBirthday((contract_id).toInt())
//            var birthDay = c2?.getColumnIndex((ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY).toString())
//            Log.d("iwa", "")
//            if (birthDay != null && birthDay != -1) {
//                Log.d("iwa", c2?.getString(birthDay))
//            }
//        }

//            Log.d("iwa", c2?.getString(birthDay!!))
//            if (c2?.getString(birthDay!!) != null) {
//                Log.d("iwa2", c2?.getString(birthDay!!))
//            } else {
//                Log.d("iwa3", "")
//            }

//        getBirthdayData();
            //取得するカラムをは、名前とIDと誕生日
//            val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Event.CONTACT_ID, ContactsContract.CommonDataKinds.Event.START_DATE)
//            // 検索条件で、Event.TYPEがTYPE_BIRTHDAYのみに絞り込む
//            val selection = ContactsContract.Data.MIMETYPE + "= ? AND " +
//                    ContactsContract.CommonDataKinds.Event.TYPE + "=" +
//                    ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY
//            val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
//            // ソートの指定はなし
//            val sortOrder: String? = null
//            val cursor = activity?.getContentResolver()?.query(
//                    ContactsContract.Data.CONTENT_URI,
//                    projection,
//                    ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.Event.TYPE + " = ?",
//                    selectionArgs,
//                    sortOrder)
//
//            // あとは、whileで結果を取得
//            cursor?.moveToFirst()
//            while (cursor?.moveToNext() != null) {
////                val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
////                val name = cursor.getString(nameIndex)
//
//                val birthIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
//                val birthDay = cursor.getString(birthIndex)
////                Toast.mak
//// eText(activity, name + "*" + birthDay, Toast.LENGTH_SHORT).show()
//            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.OnSettingFragmentInteractionListener(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSettingFragmentInteractionListener) {
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
    interface OnSettingFragmentInteractionListener {
        // TODO: Update argument type and name
        fun OnSettingFragmentInteractionListener(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SettingFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


    private fun getBirthdayData(): List<PersonalData> {

        val result = ArrayList<PersonalData>()

        val uri = ContactsContract.Data.CONTENT_URI; //ContactsContract.Contacts.CONTENT_URI
        var projection = arrayOf<String>(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Event.START_DATE)
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        val sortOrder: String? = null

        val c1 = activity?.getContentResolver()?.query(uri, projection, selection, selectionArgs,
                sortOrder)
        selection = (ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ? AND "
                + CommonDataKinds.Event.TYPE + " = ?")
        projection = arrayOf(CommonDataKinds.Event.START_DATE)
        while
                (c1!!.moveToNext()) {
            selectionArgs = arrayOf(c1.getString(0), CommonDataKinds.Event.CONTENT_ITEM_TYPE, (CommonDataKinds.Event.TYPE_BIRTHDAY).toString())
//                val nameIndex
//                val name =
                        Log.d("iwa", c1.getString(c1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
            var birthDay = c1.getColumnIndex(Integer.toString(ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY))
            if(c1.getString(birthDay) != null) {
                Log.d("iwa2", c1.getString(birthDay))
            } else {
                Log.d("iwa3", "")
            }
//            val c2 = activity?.getContentResolver()?.query(ContactsContract.Data.CONTENT_URI, projection, selection,
//                    selectionArgs, null)
//
//            if (c2!!.moveToNext()) {
//                val data = PersonalData(c1.getString(1), c2
//                        .getString(0))
//                result.add(data)
//            }
        }

        return result
    }

    inner class PersonalData(var displayName: String?, var birthDay: String?)


    fun GetContacts(): Cursor? {
        val cr = activity.getContentResolver()

        try {
            val uri = ContactsContract.Contacts.CONTENT_URI

            val projection = arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER)

            val where = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'"

            val selectionArgs: Array<String>? = null

            val sortOrder = ContactsContract.Contacts.DISPLAY_NAME

            return cr.query(uri, projection, where, selectionArgs, sortOrder)
        } catch (ex: Exception) {
            val message = ex.message
            Log.e(Constraints.TAG, "Error: $message", ex)

            return null
        }

    }

    fun GetContactBirthday(contactId: Int): Cursor? {
        val cr = activity.getContentResolver()

        try {
            val uri = ContactsContract.Data.CONTENT_URI

            val projection = arrayOf(ContactsContract.Data.CONTACT_ID, ContactsContract.CommonDataKinds.Event.START_DATE, ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.TYPE)

            val where = (ContactsContract.Data.CONTACT_ID + "=?"
                    + " AND " + ContactsContract.Data.MIMETYPE + "=?"
                    + " AND " + ContactsContract.CommonDataKinds.Event.TYPE + "=?")

            // Add contactId filter.
            val selectionArgs = arrayOf(contactId.toString(), ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE, (ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY).toString())

            val sortOrder: String? = null

            return cr.query(uri, projection, where, selectionArgs, sortOrder)
        } catch (ex: Exception) {
            val message = ex.message
            Log.d(Constraints.TAG, "Error: $message")

            return null
        }

    }
}
