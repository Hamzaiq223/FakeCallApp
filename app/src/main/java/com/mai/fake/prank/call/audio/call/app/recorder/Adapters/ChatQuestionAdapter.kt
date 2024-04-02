package com.mai.fake.prank.call.audio.call.app.recorder.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mai.fake.prank.call.audio.call.app.recorder.Model.QuestionsModel
import com.mai.fake.prank.call.audio.call.app.recorder.R


class ChatQuestionAdapter(
    private val context: Context,
    private var itemList: List<QuestionsModel.QuestionsAnswer>,
    private val click: Click
) : RecyclerView.Adapter<ChatQuestionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_questions_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvQuestion.text = itemList[position].question

        holder.clQuestion.setOnClickListener {
            click.onItemClick(position, itemList)
            // itemList.removeAt(position)
            // notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        var clQuestion: ConstraintLayout = itemView.findViewById(R.id.clQuestion)
    }

    interface Click {
        fun onItemClick(position: Int, itemList: List<QuestionsModel.QuestionsAnswer>)
    }

    fun updateData(newData: List<QuestionsModel.QuestionsAnswer>) {
        itemList = newData
        notifyDataSetChanged()
    }
}