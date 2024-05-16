package com.mai.fake.prank.call.audio.call.app.recorder.Activities.VideoCall

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.IncomingVideoCall.IncomingVideoCall
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.VideoCharacters.VideoCharacters
import com.mai.fake.prank.call.audio.call.app.recorder.R
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityVideoCallingBinding

import java.util.*

class VideoCalling : AppCompatActivity(),  MediaPlayer.OnCompletionListener {

    private val REQUEST_CAMERA_PERMISSION = 200
    private val TAG = "CameraActivity"
    private lateinit var cameraManager: CameraManager
    private var cameraDevice: CameraDevice? = null
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var captureRequestBuilder: CaptureRequest.Builder

    private var currentCameraId: String? = null
    private var videoUrl: String = ""
    private var backgroundHandler: Handler? = null
    val characterName : String = ""
    private var backgroundThread: HandlerThread? = null

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private lateinit var videoCallBinding: ActivityVideoCallingBinding

    private lateinit var audioManager: AudioManager

    private var receivedString: String? = null

    private var isMuted = false

    private var isHide = false

    private var previousVolume = 0

    private fun toggleMute() {
        if (isMuted) {
            // Unmute the device volume
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, previousVolume, 0)
            isMuted = false
            videoCallBinding.ivVolume.setImageResource(R.drawable.icon_mic_on)


        } else {
            // Mute the device volume
            previousVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
            isMuted = true
            videoCallBinding.ivVolume.setImageResource(R.drawable.icon_mic_off)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoCallBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_calling)

        receivedString = intent.getStringExtra("characterName")

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        showLoader()
        receivedString?.let { playVideo(it) }

        videoCallBinding.ivVolume.setOnClickListener {
            toggleMute()
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }

        playVideoForAMinute()

        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        videoCallBinding.textureView.surfaceTextureListener = surfaceTextureListener

        try {
            currentCameraId = getFrontCameraId()
        } catch (e: CameraAccessException) {
            throw RuntimeException(e)
        }

        videoCallBinding.ivRotateCamera.setOnClickListener {
            switchCamera()
        }

        videoCallBinding.ivCamera.setOnClickListener{
            if (isHide) {
                isHide = false
                videoCallBinding.textureView.visibility = View.VISIBLE
                videoCallBinding.ivCamera.setImageResource(R.drawable.icon_camera_on)

            } else {
                videoCallBinding.textureView.visibility = View.GONE
                isHide = true
                videoCallBinding.ivCamera.setImageResource(R.drawable.icon_camera_off)

            }
        }

        videoCallBinding.videoView.setOnCompletionListener(this)

        videoCallBinding.ivEndCall.setOnClickListener{
            videoCallBinding.videoView.stopPlayback()
            // Finish the activity
            finish()
        }
    }

    private fun playVideo(folderName: String) {

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        var lastPlayedVideoIndex = sharedPreferences.getInt("lastPlayedVideoIndex", -1)

        val folderRef = storageRef.child(folderName)

        folderRef.listAll().addOnSuccessListener { listResult ->
            val videoRefs = listResult.items.filter { it.name.endsWith(".mp4") } // Filter only video files

            if (videoRefs.isNotEmpty()) {
                val nextVideoIndex = (lastPlayedVideoIndex + 1) % videoRefs.size

                val nextVideoRef = videoRefs[nextVideoIndex]

                nextVideoRef.downloadUrl.addOnSuccessListener { uri ->
                    videoUrl = uri.toString()

                    videoCallBinding.videoView.setVideoURI(Uri.parse(videoUrl))
                    hideLoader()
                    videoCallBinding.videoView.start()

                    val editor = sharedPreferences.edit()
                    editor.putInt("lastPlayedVideoIndex", nextVideoIndex)
                    editor.apply()
                }.addOnFailureListener {
                    // Handle error
                }
            }
        }.addOnFailureListener {
            // Handle error
        }

        Handler().postDelayed({
            hideLoader()
        }, 5000)
    }



    private fun showLoader() {
        videoCallBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        videoCallBinding.progressBar.visibility = View.GONE
    }

    private fun playVideoForAMinute() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                videoCallBinding.videoView.stopPlayback()
            }
        }.start()
    }

     override fun onCompletion(mp: MediaPlayer) {
        // This method will be called when the video playback is complete
        // Stop the MediaPlayer
        mp.stop()
        // Finish the activity
        finish()
    }

    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
        }

        override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
            return false
        }

        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
        }
    }

    private val cameraStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            camera.close()
            cameraDevice = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            camera.close()
            cameraDevice = null
        }
    }
    private fun openCamera() {
        try {
            val cameraId = getFrontCameraId()
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                return
            }
            if (cameraId != null) {
                cameraManager.openCamera(cameraId, cameraStateCallback, null)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun createCameraPreview() {
        val texture = videoCallBinding.textureView.surfaceTexture
        if (texture != null) {
            texture.setDefaultBufferSize(640, 480)
        }
        val surface = Surface(texture)

        try {
            captureRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder.addTarget(surface)

            cameraDevice!!.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    cameraCaptureSession = session
                    updatePreview()
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    Toast.makeText(this@VideoCalling, "Configuration change", Toast.LENGTH_SHORT).show()
                }
            }, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun updatePreview() {
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO)
        try {
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun getFrontCameraId(): String? {
        for (cameraId in cameraManager.cameraIdList) {
            if (cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                return cameraId
            }
        }
        return null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        playVideo(receivedString!!)
        showLoader()
        if (videoUrl!!.isNotEmpty()) {
            videoCallBinding.videoView.setVideoURI(Uri.parse(videoUrl))
            videoCallBinding.videoView.start()
            playVideoForAMinute()
        }
    }

    override fun onPause() {
        super.onPause()
        stopBackgroundThread()
    }

    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("Camera Background")
        backgroundThread!!.start()
        backgroundHandler = Handler(backgroundThread!!.looper)
    }

    private fun stopBackgroundThread() {
        backgroundThread!!.quitSafely()
        try {
            backgroundThread!!.join()
            backgroundThread = null
            backgroundHandler = null
        } catch (e: InterruptedException) {
            Log.e(TAG, "Error closing background thread: " + e.message)
        }
    }

    private fun switchCamera() {
        if (cameraDevice != null) {
            cameraDevice!!.close()
            cameraDevice = null
        }

        try {
            var cameraIdToSwitch: String? = null
            if (currentCameraId == null) {
                cameraIdToSwitch = getFrontCameraId()
            } else if (currentCameraId == getFrontCameraId()) {
                cameraIdToSwitch = getBackCameraId()
            } else {
                cameraIdToSwitch = getFrontCameraId()
            }

            if (cameraIdToSwitch != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                cameraManager.openCamera(cameraIdToSwitch, cameraStateCallback, null)
                currentCameraId = cameraIdToSwitch
            } else {
                Toast.makeText(this, "Camera not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun getBackCameraId(): String? {
        for (cameraId in cameraManager.cameraIdList) {
            if (cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                return cameraId
            }
        }
        return null
    }

}
