package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.charts.Pie
import com.anychart.enums.Align
import com.anychart.graphics.vector.SolidFill

class PieChartAdapter(private val chartView: AnyChartView) {

    private val pie: Pie = AnyChart.pie()

    init {
        setupChart()
    }

    private fun setupChart() {
        // Add spacing between slices
        pie.innerRadius("50%")
        pie.padding("30px")

        // Set up white circular background
        pie.background().fill("white")
        pie.background().stroke("white")
        pie.background().cornerType("round")
        pie.background().corners(50)

        // Customize labels
        pie.labels().position("outside")
        pie.labels().fontColor("#000000")
        pie.labels().fontSize(12)
        pie.connectorStroke("2 #DADADA")

        // Remove the title to match the design
        pie.title().enabled(false)

        chartView.setChart(pie)
    }

    fun updateChartData(data: Map<String, Int>) {
        val dataEntries = mutableListOf<DataEntry>()

        data.entries.forEachIndexed { index, (category, count) ->
            dataEntries.add(object : DataEntry() {
                init {
                    setValue("x", category)
                    setValue("value", count)
                    // Apply gradient fill
                    val gradientKey = "gradient${index + 1}"
                    pie.palette().itemAt(index, gradientFill(gradientKey))
                }
            })
        }

        if (dataEntries.isEmpty()) {
            dataEntries.add(object : DataEntry() {
                init {
                    setValue("x", "No Data")
                    setValue("value", 1)
                }
            })
        }

        pie.data(dataEntries)
    }

    private fun gradientFill(key: String): SolidFill {
        return when (key) {
            "gradient1" -> SolidFill("#FF9800", 1)
            "gradient2" -> SolidFill("#FFC107", 1)
            "gradient3" -> SolidFill("#8BC34A", 1)
            "gradient4" -> SolidFill("#00BCD4", 1)
            "gradient5" -> SolidFill("#9C27B0", 1)
            else -> SolidFill("#E91E63", 1)
        }
    }
}