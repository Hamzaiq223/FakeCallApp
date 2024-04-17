package com.mai.fake.prank.call.audio.call.app.recorder.Activities.Language

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mai.fake.prank.call.audio.call.app.recorder.Activities.MainActivity
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.LanguagesAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Common.SharedHelper
import com.mai.fake.prank.call.audio.call.app.recorder.LanguageModel
import com.mai.fake.prank.call.audio.call.app.recorder.R
import com.mai.fake.prank.call.audio.call.app.recorder.databinding.ActivityLanguagesBinding

import java.util.*

class Languages : AppCompatActivity(), LanguagesAdapter.Click {

    private lateinit var binding: ActivityLanguagesBinding
    private var arrayList = ArrayList<LanguageModel>()
    private lateinit var languagesAdapter: LanguagesAdapter
    private lateinit var sharedHelper: SharedHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_languages)

        arrayList.add(LanguageModel("English", R.drawable.uk_flag))
        arrayList.add(LanguageModel("Urdu", R.drawable.pakistan))
        arrayList.add(LanguageModel("Espanol", R.drawable.spain_flag))
        arrayList.add(LanguageModel("Hindi", R.drawable.india))
        arrayList.add(LanguageModel("Hindi", R.drawable.france_flag))
        arrayList.add(LanguageModel("Hindi", R.drawable.veitnam_flag))
        arrayList.add(LanguageModel("Hindi", R.drawable.germany))
        arrayList.add(LanguageModel("Hindi", R.drawable.portugal_flag))
        arrayList.add(LanguageModel("Hindi", R.drawable.italy_flag))
        arrayList.add(LanguageModel("Hindi", R.drawable.uae_flag))
        arrayList.add(LanguageModel("Hindi", R.drawable.japan_flag))
        arrayList.add(LanguageModel("Hindi", R.drawable.south_korea_flag))
        arrayList.add(LanguageModel("Hindi", R.drawable.south_africa_flag))
        arrayList.add(LanguageModel("Hindi", R.drawable.israel_flag))
        arrayList.add(LanguageModel("Hindi", R.drawable.china))
        arrayList.add(LanguageModel("Hindi", R.drawable.turkey))
        arrayList.add(LanguageModel("Hindi", R.drawable.indonesia_flag))
        arrayList.add(LanguageModel("Hindi", R.drawable.netherland_flag))
        arrayList.add(LanguageModel("Hindi", R.drawable.nigeria))

        languagesAdapter = LanguagesAdapter(this, arrayList, this)
        binding.rvLanguages.adapter = languagesAdapter

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Kuch nahi karna
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // EditText mein text badalte samay yahan par filter function ko call karein
                languagesAdapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Kuch nahi karna
            }
        })

    }

    override fun onLanguageClick(language: String) {
        changeLanguage(this, language)
        SharedHelper.saveString(this, "language", language)
    }

     fun changeLanguage(context: Context, language: String) {

        val locale: Locale = when (language) {
            "French" -> Locale("fr")
            "Urdu" -> Locale("ur")
            "Espanol" -> Locale("es")
            else -> Locale("en")
        }

        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, context.resources.displayMetrics)

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}
