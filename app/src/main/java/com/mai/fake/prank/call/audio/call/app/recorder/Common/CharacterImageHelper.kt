package com.mai.fake.prank.call.audio.call.app.recorder.Common

import android.content.Context
import android.graphics.drawable.Drawable
import com.mai.fake.prank.call.audio.call.app.recorder.R

object CharacterImageHelper {

    // HashMap to store character names and their corresponding resource IDs
    private var characterImagesMap: HashMap<String, Int>? = null

    // Method to initialize the character images map
    private fun initializeCharacterImagesMap() {
        characterImagesMap = HashMap()
        // Add character names and their corresponding resource IDs here
        characterImagesMap?.put("c_ronaldo", R.drawable.c_ronaldo) // Replace character1 with your actual drawable name
        characterImagesMap?.put("harry_potter",R.drawable.harry_potter);
        // Add more mappings for other characters as needed
    }

    // Method to get character image by name
//    fun getCharacterImage(context: Context, characterName: String): Drawable? {
//        // Initialize the character images map if not already initialized
//        if (characterImagesMap == null) {
//            initializeCharacterImagesMap()
//        }
//
//        // Default drawable
//        var drawable: Drawable? = null
//
//        // Retrieve drawable resource ID for the given character name
//        val resourceId = characterImagesMap?.get(characterName)
//
//        // If resource ID is found, load the drawable
//        resourceId?.let {
//            drawable = context.resources.getDrawable(it, null)
//        }
//
//        return drawable
//    }

    fun getCharacterImageResourceId(context: Context, characterName: String): Int? {
        // Initialize the character images map if not already initialized
        if (characterImagesMap == null) {
            initializeCharacterImagesMap()
        }

        // Retrieve drawable resource ID for the given character name
        return characterImagesMap?.get(characterName)
    }
}
