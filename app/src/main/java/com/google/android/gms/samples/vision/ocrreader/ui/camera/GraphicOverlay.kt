/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.ocrreader.ui.camera

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

import com.google.android.gms.vision.CameraSource

import java.util.HashSet

class GraphicOverlay<T : GraphicOverlay.Graphic>(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val mLock = Any()
    private var mPreviewWidth: Int = 0
    private var mWidthScaleFactor = 1.0f
    private var mPreviewHeight: Int = 0
    private var mHeightScaleFactor = 1.0f
    private var mFacing = CameraSource.CAMERA_FACING_BACK
    private val mGraphics = HashSet<T>()

    abstract class Graphic(private val mOverlay: GraphicOverlay<*>) {

        abstract fun draw(canvas: Canvas)

        abstract fun contains(x: Float, y: Float): Boolean


        fun scaleX(horizontal: Float): Float {
            return horizontal * mOverlay.mWidthScaleFactor
        }

        fun scaleY(vertical: Float): Float {
            return vertical * mOverlay.mHeightScaleFactor
        }


        fun translateX(x: Float): Float {
            if (mOverlay.mFacing == CameraSource.CAMERA_FACING_FRONT) {
                return mOverlay.width - scaleX(x)
            } else {
                return scaleX(x)
            }
        }

        fun translateY(y: Float): Float {
            return scaleY(y)
        }

        fun postInvalidate() {
            mOverlay.postInvalidate()
        }
    }


    fun clear() {
        synchronized(mLock) {
            mGraphics.clear()
        }
        postInvalidate()
    }

    fun add(graphic: T) {
        synchronized(mLock) {
            mGraphics.add(graphic)
        }
        postInvalidate()
    }


    fun remove(graphic: T) {
        synchronized(mLock) {
            mGraphics.remove(graphic)
        }
        postInvalidate()
    }


    fun getGraphicAtLocation(rawX: Float, rawY: Float): T? {
        synchronized(mLock) {
            // Get the position of this View so the raw location can be offset relative to the view.
            val location = IntArray(2)
            this.getLocationOnScreen(location)
            for (graphic in mGraphics) {
                if (graphic.contains(rawX - location[0], rawY - location[1])) {
                    return graphic
                }
            }
            return null
        }
    }

    fun setCameraInfo(previewWidth: Int, previewHeight: Int, facing: Int) {
        synchronized(mLock) {
            mPreviewWidth = previewWidth
            mPreviewHeight = previewHeight
            mFacing = facing
        }
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        synchronized(mLock) {
            if (mPreviewWidth != 0 && mPreviewHeight != 0) {
                mWidthScaleFactor = canvas.width.toFloat() / mPreviewWidth.toFloat()
                mHeightScaleFactor = canvas.height.toFloat() / mPreviewHeight.toFloat()
            }

            for (graphic in mGraphics) {
                graphic.draw(canvas)
            }
        }
    }
}
