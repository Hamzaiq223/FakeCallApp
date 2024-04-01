package com.mai.fake.prank.call.audio.call.app.recorder.Model

class QuestionsModel {
    var characters: ArrayList<Character>? = null

    inner class Character {
        var name: String? = null
        var questions_answers: ArrayList<QuestionsAnswer>? = null
    }

    inner class QuestionsAnswer {
        var question: String? = null
        var answer: String? = null
    }
}
