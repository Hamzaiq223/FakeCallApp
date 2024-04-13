package com.mai.fake.prank.call.audio.call.app.recorder.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.Language.Languages
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.ACAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.ChatCharacterAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.VCAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Model.CharactersModel
import com.mai.fake.prank.call.audio.call.app.recorder.R
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), VCAdapter.ClickListener, ACAdapter.ClickListener, ChatCharacterAdapter.ClickListener {

    private lateinit var binding: ActivityMainBinding
    private val arrayList = ArrayList<CharactersModel>()
    private lateinit var vcAdapter: VCAdapter
    private lateinit var acAdapter: ACAdapter
    private lateinit var chatCharacterAdapter: ChatCharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        arrayList.add(CharactersModel("C Ronaldo", "Ronaldo", R.drawable.c_ronaldo))
        arrayList.add(CharactersModel("Rose", "Rose", R.drawable.rose))
        arrayList.add(CharactersModel("Lisa", "Lisa", R.drawable.lisa))
        arrayList.add(CharactersModel("Taylor Swift", "Taylor Swift", R.drawable.taylor_swift))

        vcAdapter = VCAdapter(this, arrayList, this)
        binding.rvVideoCall.adapter = vcAdapter
        binding.rvVideoCall.layoutManager = LinearLayoutManager(this)

        acAdapter = ACAdapter(this, arrayList, this)
        binding.rvAudioCall.adapter = acAdapter
        binding.rvAudioCall.layoutManager = LinearLayoutManager(this)

        chatCharacterAdapter = ChatCharacterAdapter(this, arrayList, this)
        binding.rvChat.adapter = chatCharacterAdapter
        binding.rvChat.layoutManager = LinearLayoutManager(this)

        binding.btnCLanguage.setOnClickListener {
            startActivity(Intent(this@MainActivity, Languages::class.java))
        }
    }

    override fun onItemClick(charactersModel: CharactersModel) {
        // Handle video call item click
    }

    override fun onClick(charactersModel: CharactersModel) {
        // Handle audio call item click
    }

    override fun onCharacterClick(charactersModel: CharactersModel) {
        // Handle chat character item click
    }
}
