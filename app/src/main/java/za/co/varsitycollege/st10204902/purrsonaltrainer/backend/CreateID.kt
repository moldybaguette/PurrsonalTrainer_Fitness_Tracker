package za.co.varsitycollege.st10204902.purrsonaltrainer.backend

object CreateID {
    /**
     * uses UUID to create a unique ID for any object in the realtime database
     * @return A unique ID
     */
    fun GenerateID(): String {
        val ID = java.util.UUID.randomUUID().toString()
        return ID
    }
}