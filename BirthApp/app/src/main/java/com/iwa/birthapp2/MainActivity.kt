package com.iwa.birthapp2

import android.content.Context
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.Menu
import android.view.MenuItem


class MainActivity : AppCompatActivity(), ItemFragment.OnListFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener{
    val TAG: String = "AppCompatActivity"
    var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = applicationContext

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
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.addButton ->
                with(supportFragmentManager.beginTransaction()){
                    replace(R.id.container, SettingFragment())
                    addToBackStack(null)
                    commit()
                }
        }
        return super.onOptionsItemSelected(item)
    }
}
