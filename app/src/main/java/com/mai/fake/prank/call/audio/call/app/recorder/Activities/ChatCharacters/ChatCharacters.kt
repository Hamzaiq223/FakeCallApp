package com.mai.fake.prank.call.audio.call.app.recorder.Activities.ChatCharacters

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.Chat.Chat
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.VideoCall.VideoCalling
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.CharacterListAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Model.CharactersModel
import com.mai.fake.prank.call.audio.call.app.recorder.R
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityChatCharactersBinding
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityVideoCharactersBinding

class ChatCharacters : AppCompatActivity() , CharacterListAdapter.Click{
    private lateinit var activityChatCharactersBinding: ActivityChatCharactersBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChatCharactersBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat_characters)

        activityChatCharactersBinding.ivBack.setOnClickListener{
            super.onBackPressed()
            finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.character_status_bar_color)
        }

        val itemList = ArrayList<CharactersModel>()
        itemList.add(CharactersModel("Santa Claus", "Santa", R.drawable.santa))
        itemList.add(CharactersModel("Harry  Potter", "Harry", R.drawable.harry_potter))

        val layoutManager = GridLayoutManager(this, 3)
        activityChatCharactersBinding.rvChatCharacters.layoutManager = layoutManager
        val adapter = CharacterListAdapter(this, itemList,this)
        activityChatCharactersBinding.rvChatCharacters.adapter = adapter
    }

    override fun onItemClick(charactersModel: CharactersModel) {
        Toast.makeText(this, charactersModel.name, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Chat::class.java)
        intent.putExtra("characterName",charactersModel.folder_name)
        startActivity(intent)
    }
}