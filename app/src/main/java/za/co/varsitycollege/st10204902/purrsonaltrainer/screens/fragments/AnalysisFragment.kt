package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.charts.Pie
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.FragmentAnalysisBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.android.material.datepicker.MaterialDatePicker
import za.co.varsitycollege.st10204902.purrsonaltrainer.R

class AnalysisFragment : Fragment() {
    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("AnalysisFragment", "onCreateView")
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        setupChart(binding.anyChartPie)
        setupDatePicker()
        return binding.root
    }

    private fun setupChart(anyChartPie: AnyChartView) {
        val pie: Pie = AnyChart.pie()

        // pie setup
        pie.title("Set Distribution")
        pie.labels().position("outside")

        anyChartPie.setChart(pie)

        UserManager.user?.let {
            val data = calculateExerciseDistribution(it, "2024-01-01", "2024-12-31")
            println("Data: $data")
            val dataEntries = data.map { (category, count) ->
                object : DataEntry() {
                    init {
                        setValue("x", category)
                        setValue("value", count)
                    }
                }
            }
            val mutableDataEntries: MutableList<DataEntry> = dataEntries.toMutableList()

            if (mutableDataEntries.isEmpty()) {
                mutableDataEntries.add(object : DataEntry() {
                    init {
                        setValue("x", "No Data")
                        setValue("value", 1)
                    }
                })
            }
            pie.data(mutableDataEntries)
        }
    }

    //Gets the dates from the date range picker
    private fun setupDatePicker() {
        val openDatePickerButton = binding.root.findViewById<LinearLayout>(R.id.openDatePickerButton)
        val displayDateRangeSelected = binding.root.findViewById<TextView>(R.id.displayDateRangeSelected)

        openDatePickerButton.setOnClickListener {
            val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select a date range")
                //Here is where i would link the custom style when the time comes
                //She looks ugly still because i am IN THE TRENCHES with the custom style, but i'm working on it :)
                .build()

            dateRangePicker.show(parentFragmentManager, "dateRangePicker")

            dateRangePicker.addOnPositiveButtonClickListener { selection ->
                // Handle the date range selection
                val startDate = selection.first
                val endDate = selection.second

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedStartDate = sdf.format(Date(startDate))
                val formattedEndDate = sdf.format(Date(endDate))

                val dateRangeText = "$formattedStartDate - $formattedEndDate"
                displayDateRangeSelected.text = dateRangeText

                Log.d("AnalysisFragment", "Selected date range: $dateRangeText")
                // Output Example: Selected date range: 1725148800000 to 1725667200000
                // Date range selected: 1 September 2024 - 7 September 2024
                // Included the output example to demonstrate how the date is saved from the date picker
            }
        }
    }


    private fun calculateExerciseDistribution(
        user: User,
        startDate: String,
        endDate: String
    ): Map<String, Int> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val start = sdf.parse(startDate)
        val end = sdf.parse(endDate)
        val categoryCount = mutableMapOf<String, Int>()

        user.userWorkouts.values.forEach { workout ->
            workout.workoutExercises.values.forEach { exercise ->
                if (isWithinDateRange(workout.date, start, end)) {
                    categoryCount[exercise.category] =
                        categoryCount.getOrDefault(exercise.category, 0) + 1
                }
            }
        }

        return categoryCount
    }

    private fun isWithinDateRange(date: Date, start: Date, end: Date): Boolean {
        return date in start..end
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
