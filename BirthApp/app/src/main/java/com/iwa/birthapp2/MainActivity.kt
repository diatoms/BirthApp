package com.iwa.birthapp2

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

class MainActivity : AppCompatActivity(), ItemFragment.OnListFragmentInteractionListener {
    val TAG: String = "AppCompatActivity"
    var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = applicationContext

        if(savedInstanceState == null){
            val fragmentManager: FragmentManager = getSupportFragmentManager()
            val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, ItemFragment())
            fragmentTransaction.commit()
        }
    }

    override fun onListFragmentInteraction(item: Content.ProfileItem?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
