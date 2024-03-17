package com.mai.fake.prank.call.audio.call.app.recorder.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mai.fake.prank.call.audio.call.app.recorder.Model.CharactersModel
import com.mai.fake.prank.call.audio.call.app.recorder.R
import de.hdodenhof.circleimageview.CircleImageView

class CharacterListAdapter(
    private val context: Context,
    private val itemList: List<CharactersModel>
) : RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {

    // Interface for item click listener
    interface OnItemClickListener {
        fun onItemClick(character: CharactersModel)
    }

    // Listener instance
    private var listener: OnItemClickListener? = null

    // Setter for item click listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_characters_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to your item views here
        holder.userImage.setImageResource(itemList[position].image)
        holder.userName.text = itemList[position].name
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        // Views in the item layout
        val userImage: CircleImageView = itemView.findViewById(R.id.ivUserImage)
        val userName: TextView = itemView.findViewById(R.id.tvUserName)

        init {
            // Set click listener on item view
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            // Get clicked item position
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Check if listener is set and not null
                listener?.onItemClick(itemList[position])
            }
        }
    }
}
