package com.mai.fake.prank.call.audio.call.app.recorder.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mai.fake.prank.call.audio.call.app.recorder.LanguageModel
import com.mai.fake.prank.call.audio.call.app.recorder.R

class LanguagesAdapter(
    private val context: Context,
    private var list: ArrayList<LanguageModel>,
    private val click: Click
) : RecyclerView.Adapter<LanguagesAdapter.ViewHolder>() {

    private var filteredLanguageList: ArrayList<LanguageModel> = ArrayList(list)
    private var selectedItem = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_languages, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val language = filteredLanguageList[position]
        holder.itemView.apply {
            holder.tvLanguage.text = language.name
            holder.ivFlag.setImageResource(language.image)
            holder.rbLanguage.isChecked = position == selectedItem

            holder.rbLanguage.setOnClickListener {
                selectedItem = holder.adapterPosition
                notifyDataSetChanged()
            }

            holder.cvLanguage.setOnClickListener {
                holder.rbLanguage.isChecked = position == selectedItem
                click.onLanguageClick(language.name)
            }
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
                if (language.name.toLowerCase().contains(filterPattern)) {
                    filteredLanguageList.add(language)
                }
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFlag: ImageView = itemView.findViewById(R.id.ivCountryFlag)
        val rbLanguage: RadioButton = itemView.findViewById(R.id.rbLanguage)
        val tvLanguage: TextView = itemView.findViewById(R.id.tvLanguage)
        val cvLanguage: CardView = itemView.findViewById(R.id.cvLanguage)
    }

    interface Click {
        fun onLanguageClick(language: String)
    }
}
