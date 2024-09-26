package za.co.varsitycollege.st10204902.purrsonaltrainer

/**
 * A utility class for validating user input such as email, passwords, and time.
 */
class Validator {

    /**
     * Validates if the provided email is in a correct format.
     *
     * @param email The email address to validate.
     * @return `true` if the email is valid, `false` otherwise.
     */
    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Checks if the provided passwords match.
     *
     * @param password The password to check.
     * @param confirmPassword The confirmation password to check against.
     * @return `true` if the passwords match, `false` otherwise.
     */
    fun matchPasswords(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    /**
     * Validates the complexity of the provided password.
     * The password must contain at least one digit, one lowercase letter,
     * one uppercase letter, one special character, and be at least 8 characters long.
     *
     * @param password The password to validate.
     * @return `true` if the password meets the complexity requirements, `false` otherwise.
     */
    fun validatePasswordComplexity(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\S+\$).{8,}\$".toRegex()
        return passwordPattern.matches(password)
    }

    /**
     * Validates if the start time is before the end time.
     *
     * @param startTime The start time in a comparable format.
     * @param endTime The end time in a comparable format.
     * @return `true` if the start time is before the end time, `false` otherwise.
     */
    fun validateStartEndTime(startTime: String, endTime: String): Boolean {
        return startTime < endTime
    }
}