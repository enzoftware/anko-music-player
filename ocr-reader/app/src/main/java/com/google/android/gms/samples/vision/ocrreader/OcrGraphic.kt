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


class OcrGraphic internal constructor(overlay: GraphicOverlay<*>, val textBlock: TextBlock?) : GraphicOverlay.Graphic(overlay) {

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
        if (textBlock == null) {
            return false
        }
        val rect = RectF(textBlock!!.getBoundingBox())
        rect.left = translateX(rect.left)
        rect.top = translateY(rect.top)
        rect.right = translateX(rect.right)
        rect.bottom = translateY(rect.bottom)
        return rect.left < x && rect.right > x && rect.top < y && rect.bottom > y
    }


    override fun draw(canvas: Canvas) {
        if (textBlock == null) {
            return
        }

        // Draws the bounding box around the TextBlock.
        val rect = RectF(textBlock!!.getBoundingBox())
        rect.left = translateX(rect.left)
        rect.top = translateY(rect.top)
        rect.right = translateX(rect.right)
        rect.bottom = translateY(rect.bottom)
        canvas.drawRect(rect, sRectPaint!!)

        // Break the text into multiple lines and draw each one according to its own bounding box.
        val textComponents = textBlock!!.getComponents()
        for (currentText in textComponents) {
            val left = translateX(currentText.getBoundingBox().left.toFloat())
            val bottom = translateY(currentText.getBoundingBox().bottom.toFloat())
            canvas.drawText(currentText.getValue(), left, bottom, sTextPaint!!)
        }
    }

    companion object {

        private val TEXT_COLOR = Color.WHITE

        private var sRectPaint: Paint? = null
        private var sTextPaint: Paint? = null
    }
}
