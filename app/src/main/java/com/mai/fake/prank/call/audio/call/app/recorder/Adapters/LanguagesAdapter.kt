package com.mai.fake.prank.call.audio.call.app.recorder.Adapters

import android.content.Context
import android.content.SharedPreferences
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mai.fake.prank.call.audio.call.app.recorder.Common.SharedHelper
import com.mai.fake.prank.call.audio.call.app.recorder.LanguageModel
import com.mai.fake.prank.call.audio.call.app.recorder.R

class LanguagesAdapter(
    private val context: Context,
    private var list: ArrayList<LanguageModel>,
    private val click: Click
) : RecyclerView.Adapter<LanguagesAdapter.ViewHolder>() {

    private var filteredLanguageList: ArrayList<LanguageModel> = ArrayList(list)
    private var selectedItem = RecyclerView.NO_POSITION
    private var clickPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_languages, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val language = filteredLanguageList[position]
        holder.itemView.apply {
            holder.tvLanguage.text = language.name
            holder.ivFlag.setImageResource(language.image)

            holder.clLanguage.setOnClickListener {
                clickPosition = holder.adapterPosition // Save the clicked position
                click.onLanguageClick(language.name)
                notifyDataSetChanged() // Refresh

            }
            val selectedLanguage = SharedHelper.getString(context,"language", "")
            if (selectedLanguage == language.name) {
                holder.ivTick.visibility = View.VISIBLE
                holder.clLanguage.setBackgroundResource(R.drawable.edit_text_background)
            } else {
                holder.ivTick.visibility = View.GONE
                holder.clLanguage.background = null
            }

//            if (clickPosition == position) {
//                // Show the image for the clicked position
//                holder.ivTick.visibility = View.VISIBLE
//            } else {
//                // Hide the image for other positions
//                holder.ivTick.visibility = View.GONE
//            }
        }
    }

    override fun getItemCount(): Int = filteredLanguageList.size

    fun filter(text: String) {
        filteredLanguageList.clear()
        if (text.isEmpty()) {
            filteredLanguageList.addAll(list)
        } else {
            val filterPattern = text.toLowerCase().trim()
            for (language in list) {
                if (language.language.toLowerCase().contains(filterPattern)) {
                    filteredLanguageList.add(language)
                }
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFlag: ImageView = itemView.findViewById(R.id.ivCountryFlag)
        val ivTick: ImageView = itemView.findViewById(R.id.ivTick)
        val tvLanguage: TextView = itemView.findViewById(R.id.tvLanguage)
        val clLanguage: ConstraintLayout = itemView.findViewById(R.id.clLanguage)
    }

    interface Click {
        fun onLanguageClick(language: String)
    }
}
