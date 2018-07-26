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
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.util.Log
import java.util.*


class MainActivity : AppCompatActivity(), ItemFragment.OnListFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener{
    val TAG: String = "AppCompatActivity"
    var mContext: Context? = null

    var REQUEST_PERMISSIONS: Int = 111

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
            //連絡先同期
            R.id.syncButton ->
                if(checkPermission()){
                    // Permission許可済み
                    mContext.let{
                        val intent = Intent(it, ContactsProviderIntentService::class.java)
                        startService(intent)
                    }
                } else {
                    // TODO Permission未許可の状態ではどうするか
                }

            //設定
            R.id.settingButton ->
                with(supportFragmentManager.beginTransaction()) {
                    replace(R.id.container, SettingFragment())
                    addToBackStack(null)
                    commit()
                }
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     * Permission許諾チェック
     * @return true: 許諾済み, false: 未許諾
     */
    private fun checkPermission(): Boolean{
        var result = false
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
            //Permission許可済み
            result = true
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                //許可ダイアログを一度表示済み
                //2回目以降、かつ「表示しない」チェックボックス未チェックの場合
                AlertDialog.Builder(this)
                        .setTitle(resources.getString(R.string.dialog_permission_title))
                        .setMessage(resources.getString(R.string.dialog_permission_message))
                        .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                            ActivityCompat.requestPermissions(this@MainActivity,
                                    arrayOf(android.Manifest.permission.READ_CONTACTS), 0)
                        }).create().show()
            } else {
                // 許可ダイアログ1回目
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), 0);
            }
        }
        return result
    }

    /**
     * Permissionダイアログ選択結果
     * @param requestCode リクエストを投げた際のコード
     * @param permissions Permission名前
     * @param grantResults PERMISSION_GRANTED or PERMISSION_DENIED
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.d(TAG, "onRequestPermissionsResult() start")
        Log.d(TAG, "requestCode: $requestCode")
        Log.d(TAG, "permissions: " + Arrays.toString(permissions))
        Log.d(TAG, "grantResults: " + Arrays.toString(grantResults))

        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                Log.d(TAG, "requestCode: REQUEST_PERMISSIONS")
                with(supportFragmentManager.beginTransaction()){
                    replace(R.id.container, SettingFragment())
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }
}
