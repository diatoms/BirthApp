package com.iwa.birthapp2

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.Manifest.permission
import android.content.DialogInterface
import android.support.v7.app.AlertDialog


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
            if(checkPermission()){

            } else {

            }
            R.id.syncButton ->
                with(supportFragmentManager.beginTransaction()){
                    replace(R.id.container, SettingFragment())
                    addToBackStack(null)
                    commit()
                }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPermission(): Boolean{
        var result = false
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
                result = true
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                //拒否された時 Permissionが必要な理由を表示して再度許可を求めたり、機能を無効にしたりします。
                AlertDialog.Builder(this)
                        .setTitle(resources.getString(R.string.dialog_permission_title))
                        .setMessage(resources.getString(R.string.dialog_permission_message))
                        .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                            ActivityCompat.requestPermissions(this@MainActivity,
                                    arrayOf(android.Manifest.permission.READ_CONTACTS), 0)
                        }).create().show()
            } else {
                //まだ許可を求める前の時、許可を求めるダイアログを表示します。
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 0);
            }
        }
        return result
    }
}
