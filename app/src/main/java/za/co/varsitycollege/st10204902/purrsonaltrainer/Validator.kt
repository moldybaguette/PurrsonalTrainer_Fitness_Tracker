package za.co.varsitycollege.st10204902.purrsonaltrainer

class Validator {

    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun matchPasswords(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    fun validatePasswordComplexity(password: String): Boolean {
        // Password must contain at least one digit, one lowercase letter,
        // one uppercase letter, one special character, and be at least 8 characters long

        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\S+\$).{8,}\$".toRegex()
        return passwordPattern.matches(password)
    }

    fun validateStartEndTime(startTime: String, endTime: String): Boolean {
        // Start time must be before end time
        return startTime < endTime
    }
}