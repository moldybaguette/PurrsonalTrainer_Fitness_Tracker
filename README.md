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

``` kotlin
// BAD: This comment just restates what the code does 
// Add 5 to total 
total += 5 
// GOOD: This comment explains the reasoning behind the action 
// Adding shipping cost to the total purchase amount 
total += shippingCost
```

Use TODO to mark future work needed to be done
``` kotlin
// TODO: Handle edge case for negative amounts
```

# KDoc or Summaries

kdoc starts with /** and ends with  * /
use tags:
- @param
- @return
- @throws

``` kotlin
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
``` kotlin
  
//  _____          _          __  ______ _ _      
// |  ___|        | |        / _| |  ___(_) |     
// | |__ _ __   __| |   ___ | |_  | |_   _| | ___ 
// |  __| '_ \ / _` |  / _ \|  _| |  _| | | |/ _ \
// | |__| | | | (_| | | (_) | |   | |   | | |  __/
// \____/_| |_|\__,_|  \___/|_|   \_|   |_|_|\___|
//---------------------------------------------------------------------------------
```

## Parameters
``` kotlin
  
//                                       _                
//                                      | |               
//  _ __   __ _ _ __ __ _ _ __ ___   ___| |_ ___ _ __ ___ 
// | '_ \ / _` | '__/ _` | '_ ` _ \ / _ \ __/ _ \ '__/ __|
// | |_) | (_| | | | (_| | | | | | |  __/ ||  __/ |  \__ \
// | .__/ \__,_|_|  \__,_|_| |_| |_|\___|\__\___|_|  |___/
// | |                                                    
// |_|
//---------------------------------------------------------------------------------
```

# constructors

``` kotlin
  
//  _____                 _                   _                 
// /  __ \               | |                 | |                
// | /  \/ ___  _ __  ___| |_ _ __ _   _  ___| |_ ___  _ __ ___ 
// | |    / _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \| '__/ __|
// | \__/\ (_) | | | \__ \ |_| |  | |_| | (__| || (_) | |  \__ \
//  \____/\___/|_| |_|___/\__|_|   \__,_|\___|\__\___/|_|  |___/
//---------------------------------------------------------------------------------
```

## methods
``` kotlin
  
// ___  ___     _   _               _     
// |  \/  |    | | | |             | |    
// | .  . | ___| |_| |__   ___   __| |___ 
// | |\/| |/ _ \ __| '_ \ / _ \ / _` / __|
// | |  | |  __/ |_| | | | (_) | (_| \__ \
// \_|  |_/\___|\__|_| |_|\___/ \__,_|___/
//---------------------------------------------------------------------------------
```


# other boring stuff
