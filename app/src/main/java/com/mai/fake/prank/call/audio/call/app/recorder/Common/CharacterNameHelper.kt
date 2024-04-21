package com.mai.fake.prank.call.audio.call.app.recorder.Common

object CharacterNameHelper {

    // HashMap to store folder names and their corresponding character names
    private var folderCharacterMap: HashMap<String, String>? = null

    // Method to initialize the folder-character map
    private fun initializeFolderCharacterMap() {
        folderCharacterMap = HashMap()
        // Add folder names and their corresponding character names here
        folderCharacterMap?.put("folder1", "c_ronaldo") // Replace folder1 with your actual folder name
        folderCharacterMap?.put("Harry", "Harry Potter")
        folderCharacterMap?.put("Santa", "Santa Claus")
        // Add more mappings for other folders as needed
    }

    // Method to get character name by folder name
    fun getCharacterNameByFolderName(folderName: String): String? {
        // Initialize the folder-character map if not already initialized
        if (folderCharacterMap == null) {
            initializeFolderCharacterMap()
        }

        // Retrieve character name for the given folder name
        return folderCharacterMap?.get(folderName)
    }
}