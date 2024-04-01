package com.mai.fake.prank.call.audio.call.app.recorder.Adapters

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mai.fake.prank.call.audio.call.app.recorder.Model.QuestionsAnswer
import com.mai.fake.prank.call.audio.call.app.recorder.R


class ChatAdapter(
    private val messageList: MutableList<QuestionsAnswer> = mutableListOf(),
    private val rvChat: RecyclerView,
    private val rvQuestions: RecyclerView,
    private val answerSetListener: AnswerSetListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_QUESTION = 1
        private const val VIEW_TYPE_ANSWER = 2
    }

    private val answeredMap = mutableMapOf<Int, Boolean>()
    private val handler = Handler()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = if (viewType == VIEW_TYPE_QUESTION) {
            LayoutInflater.from(parent.context).inflate(R.layout.item_message_sent, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.item_message_recieved, parent, false)
        }
        return if (viewType == VIEW_TYPE_QUESTION) {
            QuestionViewHolder(view)
        } else {
            AnswerViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if (getItemViewType(position) == VIEW_TYPE_QUESTION) {
            (holder as QuestionViewHolder).bind(message.question, position)
            if (!answeredMap.containsKey(position)) {
                handler.postDelayed({ addAnswer(position) }, 2000)
            }
        } else {
            (holder as AnswerViewHolder).bind(message.answer)
            rvQuestions.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun getItemViewType(position: Int): Int =
        if (messageList[position].question.endsWith("?")) VIEW_TYPE_QUESTION else VIEW_TYPE_ANSWER

    private fun addAnswer(position: Int) {
        if (position < messageList.size) {
            val question = messageList[position]
            val answer = QuestionsAnswer("", question.answer)
            messageList.add(position + 1, answer)

            answeredMap[position] = true
            notifyItemInserted(position + 1)
            rvChat.scrollToPosition(position + 1)
            answerSetListener?.onAnswerSet(true)
        }
    }

    private class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTextView: TextView = itemView.findViewById(R.id.text_message_body)

        fun bind(question: String, position: Int) {
            questionTextView.text = question
        }
    }

    private class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val answerTextView: TextView = itemView.findViewById(R.id.text_message_body)

        fun bind(answer: String) {
            answerTextView.text = answer
        }
    }

    interface AnswerSetListener {
        fun onAnswerSet(check: Boolean)
    }
}
