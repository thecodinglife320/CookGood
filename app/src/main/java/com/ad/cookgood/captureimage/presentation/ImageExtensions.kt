package com.ad.cookgood.captureimage.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image

fun Image.toBitmap(): Bitmap {
   val buffer = planes[0].buffer
   buffer.rewind()
   val bytes = ByteArray(buffer.capacity())
   buffer.get(bytes)
   return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}