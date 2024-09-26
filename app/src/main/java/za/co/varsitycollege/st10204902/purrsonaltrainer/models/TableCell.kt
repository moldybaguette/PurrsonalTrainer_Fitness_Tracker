package za.co.varsitycollege.st10204902.purrsonaltrainer.models

data class TableCell(
    val type: CellType,
    val text: String
)

enum class CellType{
    HEADER,
    LABEL,
    DATA
}
