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

import android.util.Log
import android.util.SparseArray

import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock


class OcrDetectorProcessor internal constructor(private val mGraphicOverlay: GraphicOverlay<OcrGraphic>)// TODO:  Once this implements Detector.Processor<TextBlock>, implement the abstract methods.
