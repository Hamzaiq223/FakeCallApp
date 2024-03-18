package com.mai.fake.prank.call.audio.call.app.recorder.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mai.fake.prank.call.audio.call.app.recorder.Model.CharactersModel
import com.mai.fake.prank.call.audio.call.app.recorder.R
import de.hdodenhof.circleimageview.CircleImageView

class CharacterListAdapter(
    private val context: Context,
    private val itemList: List<CharactersModel>,
    private val click: Click
) : RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_characters_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to your item views here
        holder.userImage.setImageResource(itemList[position].image)
        holder.userName.text = itemList[position].name

        holder.clCharacter.setOnClickListener {
            click.onItemClick(itemList.get(position))
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Views in the item layout
        val userImage: CircleImageView = itemView.findViewById(R.id.ivUserImage)
        val userName: TextView = itemView.findViewById(R.id.tvUserName)
        val clCharacter: ConstraintLayout = itemView.findViewById(R.id.clCharacter)
    }

    interface Click {
        fun onItemClick(charactersModel: CharactersModel)
    }
}
