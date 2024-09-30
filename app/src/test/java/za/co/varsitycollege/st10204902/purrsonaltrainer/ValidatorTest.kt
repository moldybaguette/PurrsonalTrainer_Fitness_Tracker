package za.co.varsitycollege.st10204902.purrsonaltrainer

import org.junit.Assert.*
import org.junit.Test

class ValidatorTest {

    @Test
    fun matchPasswords_withMatchingPasswords_returnsTrue() {
        val validator = Validator()
        assertTrue(validator.matchPasswords("password123", "password123"))
    }

    @Test
    fun matchPasswords_withNonMatchingPasswords_returnsFalse() {
        val validator = Validator()
        assertFalse(validator.matchPasswords("password123", "password321"))
    }

    @Test
    fun validatePasswordComplexity_withValidPassword_returnsTrue() {
        val validator = Validator()
        assertTrue(validator.validatePasswordComplexity("Valid@123"))
    }

    @Test
    fun validateStartEndTime_withStartTimeBeforeEndTime_returnsTrue() {
        val validator = Validator()
        assertTrue(validator.validateStartEndTime("08:00", "09:00"))
    }

    @Test
    fun validateStartEndTime_withStartTimeAfterEndTime_returnsFalse() {
        val validator = Validator()
        assertFalse(validator.validateStartEndTime("10:00", "09:00"))
    }

    @Test
    fun validateStartEndTime_withStartTimeEqualToEndTime_returnsFalse() {
        val validator = Validator()
        assertFalse(validator.validateStartEndTime("09:00", "09:00"))
    }

    @Test
    fun validatePasswordComplexity_withNoDigit_returnsFalse() {
        val validator = Validator()
        assertFalse(validator.validatePasswordComplexity("Valid@abc"))
    }

    @Test
    fun validatePasswordComplexity_withNoLowercase_returnsFalse() {
        val validator = Validator()
        assertFalse(validator.validatePasswordComplexity("VALID@123"))
    }

    @Test
    fun validatePasswordComplexity_withNoUppercase_returnsFalse() {
        val validator = Validator()
        assertFalse(validator.validatePasswordComplexity("valid@123"))
    }

    @Test
    fun validatePasswordComplexity_withNoSpecialCharacter_returnsFalse() {
        val validator = Validator()
        assertFalse(validator.validatePasswordComplexity("Valid123"))
    }

    @Test
    fun validatePasswordComplexity_withLessThanEightCharacters_returnsFalse() {
        val validator = Validator()
        assertFalse(validator.validatePasswordComplexity("Val@1"))
    }

    @Test
    fun validatePasswordComplexity_withWhitespace_returnsFalse() {
        val validator = Validator()
        assertFalse(validator.validatePasswordComplexity("Valid 123"))
    }
}