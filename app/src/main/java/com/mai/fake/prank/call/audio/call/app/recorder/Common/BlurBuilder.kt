package com.mai.fake.prank.call.audio.call.app.recorder.Common

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

object BlurBuilder {
    private const val BITMAP_SCALE = 0.4f
    private const val BLUR_RADIUS = 10f

    fun blur(context: Context, image: Bitmap): Bitmap {
        val width = (image.width * BITMAP_SCALE).toInt()
        val height = (image.height * BITMAP_SCALE).toInt()

        val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)

        val rs = RenderScript.create(context)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)

        script.setRadius(BLUR_RADIUS)
        script.setInput(tmpIn)
        script.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)

        return outputBitmap
    }
}
