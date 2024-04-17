package com.mai.fake.prank.call.audio.call.app.recorder.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mai.fake.prank.call.audio.call.app.recorder.Model.CharactersModel
import com.mai.fake.prank.call.audio.call.app.recorder.R
import de.hdodenhof.circleimageview.CircleImageView

class ChatCharacterAdapter(private val context: Context, private val itemList: List<CharactersModel>, private val click: ClickListener) :
    RecyclerView.Adapter<ChatCharacterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_character, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.userImage.setImageResource(item.image)
        holder.userName.text = item.name
        holder.userName.visibility = View.GONE

        holder.clCharacter.setOnClickListener {
            click.onCharacterClick(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: CircleImageView = itemView.findViewById(R.id.ivUserImage)
        val userName: TextView = itemView.findViewById(R.id.tvUserName)
        val clCharacter: ConstraintLayout = itemView.findViewById(R.id.clCharacter)
    }

    interface ClickListener {
        fun onCharacterClick(charactersModel: CharactersModel)
    }
}
