package com.mai.fake.prank.call.audio.call.app.recorder.Activities.Chat

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.ChatAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.ChatQuestionAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Model.QuestionsAnswer
import com.mai.fake.prank.call.audio.call.app.recorder.Model.QuestionsModel

import com.mai.fake.prank.call.audio.call.app.recorder.R
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class Chat : AppCompatActivity(), ChatAdapter.AnswerSetListener, ChatQuestionAdapter.Click {

    private lateinit var chatQuestionAdapter: ChatQuestionAdapter
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var rvQuestions: RecyclerView
    private lateinit var rvChat: RecyclerView
    private var arrayList = ArrayList<QuestionsAnswer>()
    private var isAdded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        rvQuestions = findViewById(R.id.rvQuestions)
        rvChat = findViewById(R.id.rvChat)

        val context: Context = this
        val resourceId = R.raw.characters
        val characterName = "Santa"
        val questionsAnswers = getQuestionsAnswersForCharacter(context, resourceId, characterName)

        chatQuestionAdapter = ChatQuestionAdapter(context, questionsAnswers, this)
        rvQuestions.adapter = chatQuestionAdapter

        chatAdapter = ChatAdapter(arrayList, rvChat, rvQuestions, this)
        rvChat.adapter = chatAdapter
    }

    private fun getQuestionsAnswersForCharacter(context: Context, resourceId: Int, characterName: String): ArrayList<QuestionsModel.QuestionsAnswer> {
        val questionsAnswers = ArrayList<QuestionsModel.QuestionsAnswer>()
        try {
            val inputStream: InputStream = context.resources.openRawResource(resourceId)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                jsonString.append(line)
            }
            val jsonObject = JSONObject(jsonString.toString())
            val charactersArray = jsonObject.getJSONArray("characters")
            for (i in 0 until charactersArray.length()) {
                val characterObject = charactersArray.getJSONObject(i)
                val name = characterObject.getString("name")
                if (name.equals(characterName, ignoreCase = true)) {
                    val questionsAnswersArray = characterObject.getJSONArray("questions_answers")
                    return parseQuestionsAnswers(questionsAnswersArray)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return questionsAnswers
    }

    private fun parseQuestionsAnswers(questionsAnswersArray: JSONArray): ArrayList<QuestionsModel.QuestionsAnswer> {
        val questionsAnswers = ArrayList<QuestionsModel.QuestionsAnswer>()
        for (i in 0 until questionsAnswersArray.length()) {
            val questionsAnswerObject = questionsAnswersArray.getJSONObject(i)
            val questionsAnswer = QuestionsModel().QuestionsAnswer()
            questionsAnswer.question = questionsAnswerObject.getString("question")
            questionsAnswer.answer = questionsAnswerObject.getString("answer")
            questionsAnswers.add(questionsAnswer)
        }
        return questionsAnswers
    }


    override fun onAnswerSet(check: Boolean) {
        isAdded = check
    }

    override fun onItemClick(position: Int, list: MutableList<QuestionsModel.QuestionsAnswer>) {
        if (isAdded || arrayList.isEmpty()) {
            list[position].question?.let { list[position].answer?.let { it1 ->
                QuestionsAnswer(it,
                    it1
                )
            } }
                ?.let { arrayList.add(it) }
            chatAdapter.notifyDataSetChanged()

            list.removeAt(position)
            chatQuestionAdapter.notifyDataSetChanged()
            val lastItemPosition = arrayList.size - 1
            rvChat.scrollToPosition(lastItemPosition)
            rvChat.smoothScrollToPosition(lastItemPosition)
            isAdded = false
        }
    }

}
