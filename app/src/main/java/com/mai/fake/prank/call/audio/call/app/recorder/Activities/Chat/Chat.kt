package com.mai.fake.prank.call.audio.call.app.recorder.Activities.Chat

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.ChatAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Adapters.ChatQuestionAdapter
import com.mai.fake.prank.call.audio.call.app.recorder.Common.BlurBuilder
import com.mai.fake.prank.call.audio.call.app.recorder.Common.CharacterImageHelper
import com.mai.fake.prank.call.audio.call.app.recorder.Common.CharacterNameHelper
import com.mai.fake.prank.call.audio.call.app.recorder.Common.FlashlightController
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
    private lateinit var rvQuestions: RecyclerView
    private lateinit var rvChat: RecyclerView
    private lateinit var ivImage: ImageView

    private lateinit var ivBack: ImageView
    private lateinit var tvName: TextView
    private lateinit var chatAdapter: ChatAdapter
    private val arrayList = ArrayList<QuestionsAnswer>()
    private var isAdded = false
    private var mediaPlayer: MediaPlayer? = null
    private var receivedString: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        rvQuestions = findViewById(R.id.rvQuestions)
        rvChat = findViewById(R.id.rvChat)
        ivImage = findViewById(R.id.ivImage)
        ivBack = findViewById(R.id.ivBack)
        tvName  =findViewById<TextView>(R.id.tvName)
        mediaPlayer = MediaPlayer.create(this, R.raw.iphone_sms_tone)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.white)
        }

        receivedString = intent.getStringExtra("characterName")

        val context: Context = applicationContext // or any valid context
        val characterDrawable: Int? = CharacterImageHelper.getCharacterImageResourceId(context, receivedString!!)
        val characterName = CharacterNameHelper.getCharacterNameByFolderName(receivedString!!)
        tvName.text = characterName

        val originalBitmap: Bitmap =
            characterDrawable?.let { BitmapFactory.decodeResource(resources, it) }!!

        ivImage.setImageBitmap(originalBitmap)

        ivBack.setOnClickListener{
            super.onBackPressed()
        }

        val resourceId = R.raw.characters
        val questionsAnswers =
            receivedString?.let { getQuestionsAnswersForCharacter(this, resourceId, it) }

        chatQuestionAdapter = questionsAnswers?.let { ChatQuestionAdapter(this, it, this) }!!
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

    override fun onItemClick(position: Int, list: List<QuestionsModel.QuestionsAnswer>) {
        if (isAdded || arrayList.isEmpty()) {
            list[position].question?.let { list[position].answer?.let { it1 ->
                QuestionsAnswer(it, it1)
            } }
                ?.let { arrayList.add(it) }

            // Remove the item from the original list used by the adapter
            val mutableList = list.toMutableList()
            mutableList.removeAt(position)
            chatQuestionAdapter.updateData(mutableList) // Update the data in the adapter
            chatAdapter.notifyDataSetChanged()

            val lastItemPosition = arrayList.size - 1
            rvChat.scrollToPosition(lastItemPosition)
            rvChat.smoothScrollToPosition(lastItemPosition)
            isAdded = false
        }
    }

    override fun onAnswerSet(check: Boolean) {
        check?.let { isAdded = it }
        val flashlightController = FlashlightController(this)
        // To turn on the flashlight
        flashlightController.turnOnFlash()
        // To turn off the flashlight
        flashlightController.turnOffFlash()

        mediaPlayer?.start() // Start playing the ringtone if mediaPlayer is not null

    }

    override fun onDestroy() {
        super.onDestroy()
        // Release MediaPlayer resources
        mediaPlayer?.release()
        mediaPlayer = null
    }



}
