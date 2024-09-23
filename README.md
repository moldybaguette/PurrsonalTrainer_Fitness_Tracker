# PurrsonalTrainer

# styleGuide

# Classes

## naming

class naming convention should be pascal case
example: MySuperCoolClass

## organization

classes should be organized as follow:

1. public properties
2. private properties
3. constructor
4. public methods
5. private methods

## Notes

- group methods by functionality
- classes should not be to big and only have one responsibility

# Methods

## naming

method names should be camel case
method names should also be verb based
example: calculateTotalAmount

method parameters should be named descriptively to clarify its role.

## Modifiers

use private where possible
use internal for both classes and methods that are only needed within the module.

## length

Methods should only do one thing
Methods should ideally not be longer than 20 lines

# Comments

Comments should explain why something is done not how something is done
avoid redundant comments

```kotlin
// BAD: This comment just restates what the code does
// Add 5 to total
total += 5
// GOOD: This comment explains the reasoning behind the action
// Adding shipping cost to the total purchase amount
total += shippingCost
```

Use TODO to mark future work needed to be done

```kotlin
// TODO: Handle edge case for negative amounts
```

# KDoc or Summaries

kdoc starts with /\*_ and ends with _ /
use tags:

- @param
- @return
- @throws

```kotlin
/**
 * Handles all user-related operations, including authentication
 * and account management.
 *
 * @property userRepository The repository used to access user data
 */
class UserManager(private val userRepository: UserRepository) {
    // Class implementation
}

/**
 * Calculates the total price of items in the cart, including taxes.
 *
 * @param items List of items in the shopping cart.
 * @param taxRate The tax rate to be applied to the total.
 * @return The total price of the items, including taxes.
 */
fun calculateTotal(items: List<Item>, taxRate: Double): Double {
    // Function implementation
}

/**
 * Sets the user password after validating its strength.
 *
 * @param password The password to be set. Must be at least 8 characters long.
 * @throws IllegalArgumentException if the password is weak.
 */
fun setPassword(password: String) {
    // Function implementation
}

/**
 * Sets the user password after validating its strength.
 *
 * @param password The password to be set. Must be at least 8 characters long.
 * @throws IllegalArgumentException if the password is weak.
 */
fun setPassword(password: String) {
    // Function implementation
}

/**
 * Connects to a remote server.
 *
 * @throws IOException if there is an error connecting to the server.
 */
fun connectToServer() {
    // Implementation
}

```

# Comments

## End of file

```kotlin

//------------------------***EOF***-----------------------------//
```

## Parameters

```kotlin
//                          PROPERTIES                       //
```

# constructors

// CONSTRUCTORS //

```kotlin

//                          CONSTRUCTORS                       //
```

## methods

```kotlin
//                          METHODS                          //
```

## section starts and ends

```kotlin
//-----------------------------------------------------------//
```

# Usage

so when starting a section at the top of a document you'll give the heading of the section followed by the line of dashes to indicate the start of the section.

for instance:

```
//                          METHODS                          //
//-----------------------------------------------------------//
```

this is what a section heading with no other headings above it would look like.

sub sections are titles with a capital letter at the start of each word and spaces between words
they also are left aligned instead of centred.

```
// Authentication Methods
//-----------------------------------------------------------//
```

in this example the Authentication methods would come after the METHODs title. NOTE: there is no //--// line above the Authentication Methods heading because that would indicate the end of the methods section!!!

think of the //----// as brackets. you can nest them!

if you added another methods section underneath the authentication section it would have both an opening and closing line:

```
//-----------------------------------------------------------//
// Validation Methods
//-----------------------------------------------------------//
```
