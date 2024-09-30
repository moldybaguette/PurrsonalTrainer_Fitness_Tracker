package za.co.varsitycollege.st10204902.purrsonaltrainer.models

/**
 * Data class representing a table cell.
 *
 * @property type The type of the cell (HEADER, LABEL, DATA).
 * @property text The text content of the cell.
 */
data class TableCell(
    val type: CellType,
    val text: String
)

/**
 * Enum class representing the type of a table cell.
 */
enum class CellType {
    HEADER, // Represents a header cell
    LABEL,  // Represents a label cell
    DATA    // Represents a data cell
}