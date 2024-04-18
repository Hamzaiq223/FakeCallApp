package com.mai.fake.prank.call.audio.call.app.recorder.Activities.Language

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_languages)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.screen_background_color)
        }

        arrayList.add(LanguageModel("English", R.drawable.uk_flag,"English"))
        arrayList.add(LanguageModel("اردو", R.drawable.pakistan,"Urdu"))
        arrayList.add(LanguageModel("Espanol", R.drawable.spain_flag,"Spanish"))
        arrayList.add(LanguageModel("हिंदी", R.drawable.india,"Hindi"))
        arrayList.add(LanguageModel("français", R.drawable.france_flag,"French"))
        arrayList.add(LanguageModel("Tiếng Việt", R.drawable.veitnam_flag,"vietnamese"))
        arrayList.add(LanguageModel("Deutsch", R.drawable.germany,"German"))
        arrayList.add(LanguageModel("Português", R.drawable.portugal_flag,"Portuguese"))
        arrayList.add(LanguageModel("Italiano", R.drawable.italy_flag,"Italian"))
        arrayList.add(LanguageModel("عربي", R.drawable.uae_flag,"Arabic"))
        arrayList.add(LanguageModel("日本語", R.drawable.japan_flag,"Japnese"))
        arrayList.add(LanguageModel("한국인", R.drawable.south_korea_flag,"Korean"))
        arrayList.add(LanguageModel("Afrikaans", R.drawable.south_africa_flag,"Afrikaans"))
        arrayList.add(LanguageModel("עִברִית", R.drawable.israel_flag,"Hebrew"))
        arrayList.add(LanguageModel("中国人", R.drawable.china,"Chinese"))
        arrayList.add(LanguageModel("Türkçe", R.drawable.turkey,"Turkish"))
        arrayList.add(LanguageModel("bahasa Indonesia", R.drawable.indonesia_flag,"Indonesian"))
        arrayList.add(LanguageModel("Dutch", R.drawable.netherland_flag,"Dutch"))
        arrayList.add(LanguageModel("harshen hausa", R.drawable.nigeria,"Hausa"))

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
            "عربي" -> Locale("ar")
            "اردو" -> Locale("ur")
            "日本語" -> Locale("ja")
            "Português" -> Locale("pt")
            "한국인" -> Locale("ko")
            "Italiano" -> Locale("it")
            "Afrikaans" -> Locale("af")
            "Türkçe" -> Locale("tr")
            "中国人" -> Locale("zh")
            "Tiếng Việt" -> Locale("vi")
            "Dutch" -> Locale("nl")
            "עִברִית" -> Locale("iw")
            "bahasa Indonesia" -> Locale("in")
            "हिंदी" -> Locale("hi")
            "harshen hausa" -> Locale("ha")
            "Deutsch" -> Locale("de")
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
