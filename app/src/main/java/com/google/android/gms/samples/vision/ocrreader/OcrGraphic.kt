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
package com.google.android.gms.samples.vision.ocrreader

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay
import com.google.android.gms.vision.text.Text
import com.google.android.gms.vision.text.TextBlock

class OcrGraphic internal constructor(overlay: GraphicOverlay<*>, val textBlock: TextBlock) : GraphicOverlay.Graphic(overlay) {

    var id: Int = 0

    init {

        if (sRectPaint == null) {
            sRectPaint = Paint()
            sRectPaint!!.color = TEXT_COLOR
            sRectPaint!!.style = Paint.Style.STROKE
            sRectPaint!!.strokeWidth = 4.0f
        }

        if (sTextPaint == null) {
            sTextPaint = Paint()
            sTextPaint!!.color = TEXT_COLOR
            sTextPaint!!.textSize = 54.0f
        }
        // Redraw the overlay, as this graphic has been added.
        postInvalidate()
    }

    override fun contains(x: Float, y: Float): Boolean {
        // TODO: Check if this graphic's text contains this point.
        return false
    }

    override fun draw(canvas: Canvas) {
        // TODO: Draw the text onto the canvas.
    }

    companion object {

        private val TEXT_COLOR = Color.WHITE

        private var sRectPaint: Paint? = null
        private var sTextPaint: Paint? = null
    }
}
