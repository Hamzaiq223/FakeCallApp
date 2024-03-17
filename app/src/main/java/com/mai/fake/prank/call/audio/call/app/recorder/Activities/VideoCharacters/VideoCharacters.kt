package com.mai.fake.prank.call.audio.call.app.recorder.Activities.VideoCharacters

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.CharacterListAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Model.CharactersModel
import com.mai.fake.prank.call.audio.call.app.recorder.R
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityVideoCallingBinding
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityVideoCharactersBinding

class VideoCharacters : AppCompatActivity(),CharacterListAdapter.OnItemClickListener  {

    private lateinit var videoCallBinding: ActivityVideoCharactersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoCallBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_characters)

        val itemList = ArrayList<CharactersModel>()
        itemList.add(CharactersModel("Cristiano Ronaldo", "Ronaldo", R.drawable.c_ronaldo))
        itemList.add(CharactersModel("Lionel Messi", "Leo Messi", R.drawable.leo_messi))

        val layoutManager = GridLayoutManager(this, 3)
        videoCallBinding.rvVideoCharacters.layoutManager = layoutManager
        val adapter = CharacterListAdapter(this, itemList)
        videoCallBinding.rvVideoCharacters.adapter = adapter
    }

    override fun onItemClick(character: CharactersModel) {
        val intent = Intent(this, VideoCharacters::class.java)
        intent.putExtra("characterName", character.folder_name) // Pass the string to the next activity
        startActivity(intent)
    }
}