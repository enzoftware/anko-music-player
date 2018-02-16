package com.experiments.enzoftware.ankomusicplayer

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var mediaPlayer : MediaPlayer ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }else{
            createPlayer()
        }
    }

    private fun createPlayer() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }



    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }

}
