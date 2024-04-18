package com.mai.fake.prank.call.audio.call.app.recorder.Activities.VideoCharacters

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.VideoCall.VideoCalling
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.CharacterListAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Model.CharactersModel
import com.mai.fake.prank.call.audio.call.app.recorder.R
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityVideoCallingBinding
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityVideoCharactersBinding

class VideoCharacters : AppCompatActivity(),CharacterListAdapter.Click{

    private lateinit var videoCallBinding: ActivityVideoCharactersBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoCallBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_characters)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.character_status_bar_color)
        }

        val itemList = ArrayList<CharactersModel>()
        itemList.add(CharactersModel("Cristiano Ronaldo", "Ronaldo", R.drawable.c_ronaldo))
        itemList.add(CharactersModel("Lionel Messi", "Leo Messi", R.drawable.leo_messi))

        val layoutManager = GridLayoutManager(this, 3)
        videoCallBinding.rvVideoCharacters.layoutManager = layoutManager
        val adapter = CharacterListAdapter(this, itemList,this)
        videoCallBinding.rvVideoCharacters.adapter = adapter
    }

    override fun onItemClick(charactersModel: CharactersModel) {
        Toast.makeText(this, charactersModel.name, Toast.LENGTH_SHORT).show()
        val intent = Intent(this,VideoCalling::class.java)
        intent.putExtra("characterName",charactersModel.folder_name)
        startActivity(intent)
    }

}