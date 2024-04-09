package com.mai.fake.prank.call.audio.call.app.recorder.Common

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager

class FlashlightController(private val context: Context) {

    private val cameraManager: CameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var cameraId: String? = null
    private var isFlashOn = false

    init {
        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun turnOnFlash() {
        if (!isFlashOn) {
            try {
                cameraManager.setTorchMode(cameraId!!, true)
                isFlashOn = true
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
    }

    fun turnOffFlash() {
        if (isFlashOn) {
            try {
                cameraManager.setTorchMode(cameraId!!, false)
                isFlashOn = false
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
    }

    fun toggleFlash() {
        if (isFlashOn) {
            turnOffFlash()
        } else {
            turnOnFlash()
        }
    }
}
