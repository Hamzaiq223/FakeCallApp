package com.mai.fake.prank.call.audio.call.app.recorder.Activities.VideoCharacters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mai.fake.prank.call.audio.call.app.recorder.R
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityVideoCallingBinding
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityVideoCharactersBinding

class VideoCharacters : AppCompatActivity() {

    private lateinit var videoCallBinding: ActivityVideoCharactersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoCallBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_characters)
    }
}