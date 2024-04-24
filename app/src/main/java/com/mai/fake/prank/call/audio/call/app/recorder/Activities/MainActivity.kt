package com.mai.fake.prank.call.audio.call.app.recorder.Activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.AudioCharacters.AudioCharacters
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.ChatCharacters.ChatCharacters
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.Language.Languages
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.VideoCharacters.VideoCharacters
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.ACAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.ChatCharacterAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.VCAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Common.SharedHelper
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.app_color)
        }

        arrayList.add(CharactersModel("C Ronaldo", "Ronaldo", R.drawable.c_ronaldo))
        arrayList.add(CharactersModel("Rose", "Rose", R.drawable.rose))
        arrayList.add(CharactersModel("Lisa", "Lisa", R.drawable.lisa))
        arrayList.add(CharactersModel("Taylor Swift", "Taylor Swift", R.drawable.taylor_swift))

        vcAdapter = VCAdapter(this, arrayList, this)
        binding.rvVideoCall.adapter = vcAdapter


        acAdapter = ACAdapter(this, arrayList, this)
        binding.rvAudioCall.adapter = acAdapter


        chatCharacterAdapter = ChatCharacterAdapter(this, arrayList, this)
        binding.rvChat.adapter = chatCharacterAdapter

        binding.btnSetting.setOnClickListener {
            showSettingDialog(this)
        }


        binding.clMoreVideos.setOnClickListener {
            startActivity(Intent(this@MainActivity,VideoCharacters::class.java))
        }

        binding.clMoreAudios.setOnClickListener {
            startActivity(Intent(this@MainActivity,ChatCharacters::class.java))
        }

        binding.clMoreChat.setOnClickListener {
            startActivity(Intent(this@MainActivity,AudioCharacters::class.java))
        }

    }

    companion object {
        fun showSettingDialog(context: Context) {
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.layout_setting_dialog, null)
            dialogBuilder.setView(dialogView)

            val ratingAlertDialog = dialogBuilder.create()
            ratingAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            ratingAlertDialog.setCancelable(false)

            // Find views
            val ivVolume = dialogView.findViewById<ImageView>(R.id.ivVolume)
            val ivVibration = dialogView.findViewById<ImageView>(R.id.ivVibation)
            val ivFlash = dialogView.findViewById<ImageView>(R.id.ivFlash)
            val tvLanguage = dialogView.findViewById<TextView>(R.id.tvLanguage)
            val tvRateUs = dialogView.findViewById<TextView>(R.id.tvRateUs)
            val ivCross = dialogView.findViewById<ImageView>(R.id.ivCross)

            var volume : Boolean
            var vibration : Boolean
            var flash : Boolean

            volume = SharedHelper.getBoolean(context,"volume_off",false)
            vibration = SharedHelper.getBoolean(context,"vibration_off",false)
            flash = SharedHelper.getBoolean(context,"flash_off",false)

            if(volume){
                ivVolume.setImageResource(R.drawable.sound_off)
            }

            if(vibration){
                ivVibration.setImageResource(R.drawable.vibrating_off)
            }

            if(flash){
                ivFlash.setImageResource(R.drawable.flash_off)
            }

            ivVolume.setOnClickListener{
                if(volume){
                    ivVolume.setImageResource(R.drawable.icon_sound)
                    SharedHelper.saveBoolean(context,"volume_off",false)
                    volume = false
                }else{
                    volume = true
                    ivVolume.setImageResource(R.drawable.sound_off)
                    SharedHelper.saveBoolean(context,"volume_off",true)
                }
            }

            ivVibration.setOnClickListener{
                if(vibration){
                    ivVibration.setImageResource(R.drawable.ic_vibration)
                    SharedHelper.saveBoolean(context,"vibration_off",false)
                    vibration = false
                }else{
                    vibration = true
                    ivVibration.setImageResource(R.drawable.vibrating_off)
                    SharedHelper.saveBoolean(context,"vibration_off",true)
                }
            }

            ivFlash.setOnClickListener{
                if(flash){
                    flash = false
                    ivFlash.setImageResource(R.drawable.ic_flash)
                    SharedHelper.saveBoolean(context,"flash_off",false)
                }else{
                    flash = true
                    ivFlash.setImageResource(R.drawable.flash_off)
                    SharedHelper.saveBoolean(context,"flash_off",true)
                }
            }

            ivCross.setOnClickListener{
                ratingAlertDialog.dismiss()
            }

            // Set onClickListener for tvLanguage
            tvLanguage.setOnClickListener {
                context.startActivity(Intent(context, Languages::class.java))
                ratingAlertDialog.dismiss()
            }

            ratingAlertDialog.show()
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
