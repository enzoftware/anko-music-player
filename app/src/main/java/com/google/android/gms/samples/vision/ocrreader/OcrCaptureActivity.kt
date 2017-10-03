
package com.google.android.gms.samples.vision.ocrreader

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.samples.vision.ocrreader.ui.camera.CameraSource
import com.google.android.gms.samples.vision.ocrreader.ui.camera.CameraSourcePreview
import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer

import java.io.IOException
import java.util.Locale

class OcrCaptureActivity : AppCompatActivity() {

    private var mCameraSource: CameraSource? = null
    private var mPreview: CameraSourcePreview? = null
    private var mGraphicOverlay: GraphicOverlay<OcrGraphic>? = null

    // Helper objects for detecting taps and pinches.
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var gestureDetector: GestureDetector? = null

    // A TextToSpeech engine for speaking a String value.
    private val tts: TextToSpeech? = null

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.ocr_capture)

        mPreview = findViewById(R.id.preview) as CameraSourcePreview
        mGraphicOverlay = findViewById(R.id.graphicOverlay) as GraphicOverlay<OcrGraphic>

        // Set good defaults for capturing text.
        val autoFocus = true
        val useFlash = false

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        val rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash)
        } else {
            requestCameraPermission()
        }

        gestureDetector = GestureDetector(this, CaptureGestureListener())
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        Snackbar.make(mGraphicOverlay!!, "Tap to Speak. Pinch/Stretch to zoom",
                Snackbar.LENGTH_LONG)
                .show()

        // TODO: Set up the Text To Speech engine.
    }

    private fun requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission")

        val permissions = arrayOf(Manifest.permission.CAMERA)

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM)
            return
        }

        val thisActivity = this

        val listener = View.OnClickListener {
            ActivityCompat.requestPermissions(thisActivity, permissions,
                    RC_HANDLE_CAMERA_PERM)
        }

        Snackbar.make(mGraphicOverlay!!, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val b = scaleGestureDetector!!.onTouchEvent(e)

        val c = gestureDetector!!.onTouchEvent(e)

        return b || c || super.onTouchEvent(e)
    }

    @SuppressLint("InlinedApi")
    private fun createCameraSource(autoFocus: Boolean, useFlash: Boolean) {
        val context = applicationContext

        // TODO: Create the TextRecognizer
        // TODO: Set the TextRecognizer's Processor.

        // TODO: Check if the TextRecognizer is operational.

        // TODO: Create the mCameraSource using the TextRecognizer.
    }

    override fun onResume() {
        super.onResume()
        startCameraSource()
    }

    override fun onPause() {
        super.onPause()
        if (mPreview != null) {
            mPreview!!.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPreview != null) {
            mPreview!!.release()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode)
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source")
            // We have permission, so create the camerasource
            val autoFocus = intent.getBooleanExtra(AutoFocus, false)
            val useFlash = intent.getBooleanExtra(UseFlash, false)
            createCameraSource(autoFocus, useFlash)
            return
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.size +
                " Result code = " + if (grantResults.size > 0) grantResults[0] else "(empty)")

        val listener = DialogInterface.OnClickListener { dialog, id -> finish() }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show()
    }

    @Throws(SecurityException::class)
    private fun startCameraSource() {
        // check that the device has play services available.
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                applicationContext)
        if (code != ConnectionResult.SUCCESS) {
            val dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS)
            dlg.show()
        }

        if (mCameraSource != null) {
            try {
                mPreview!!.start(mCameraSource!!, mGraphicOverlay!!)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                mCameraSource!!.release()
                mCameraSource = null
            }

        }
    }


    private fun onTap(rawX: Float, rawY: Float): Boolean {
        // TODO: Speak the text when the user taps on screen.
        return false
    }

    private inner class CaptureGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return onTap(e.rawX, e.rawY) || super.onSingleTapConfirmed(e)
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.OnScaleGestureListener {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            return false
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            if (mCameraSource != null) {
                mCameraSource!!.doZoom(detector.scaleFactor)
            }
        }
    }

    companion object {
        private val TAG = "OcrCaptureActivity"

        // Intent request code to handle updating play services if needed.
        private val RC_HANDLE_GMS = 9001

        // Permission request codes need to be < 256
        private val RC_HANDLE_CAMERA_PERM = 2

        // Constants used to pass extra data in the intent
        val AutoFocus = "AutoFocus"
        val UseFlash = "UseFlash"
        val TextBlockObject = "String"
    }
}
