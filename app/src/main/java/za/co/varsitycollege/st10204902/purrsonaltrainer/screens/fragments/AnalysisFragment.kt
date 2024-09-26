package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.AnalysisBreakdownAdapter

/**
 * Fragment for displaying analysis data using a pie chart.
 */
class AnalysisFragment : Fragment() {
    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("AnalysisFragment", "onCreateView")
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)

        recyclerSetup()

        setupChart(binding.anyChartPie)

        setupDatePicker()

        return binding.root
    }

    /**
     * Sets up the pie chart with data.
     * @param anyChartPie The AnyChartView to display the pie chart.
     */
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

    /**
     * Sets up the date range picker and handles date selection.
     */
    private fun setupDatePicker() {
        val openDatePickerButton = binding.root.findViewById<LinearLayout>(R.id.openDatePickerButton)
        val displayDateRangeSelected = binding.root.findViewById<TextView>(R.id.displayDateRangeSelected)

        openDatePickerButton.setOnClickListener {
            val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("SELECT A DATE RANGE")
                .setTheme(R.style.CalendarTheme)
                .setSelection(
                    androidx.core.util.Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

            dateRangePicker.show(parentFragmentManager, "dateRangePicker")

            dateRangePicker.addOnPositiveButtonClickListener { selection ->
                val startDate = selection.first
                val endDate = selection.second

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedStartDate = sdf.format(Date(startDate))
                val formattedEndDate = sdf.format(Date(endDate))

                val dateRangeText = "$formattedStartDate - $formattedEndDate"
                displayDateRangeSelected.text = dateRangeText

                Log.d("AnalysisFragment", "Selected date range: $dateRangeText")
            }
        }
    }

    /**
     * Calculates the distribution of exercises within a date range.
     * @param user The user whose exercise data is being analyzed.
     * @param startDate The start date of the range in yyyy-MM-dd format.
     * @param endDate The end date of the range in yyyy-MM-dd format.
     * @return A map of exercise categories to their counts.
     */
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

    /**
     * Checks if a date is within a specified range.
     * @param date The date to check.
     * @param start The start date of the range.
     * @param end The end date of the range.
     * @return True if the date is within the range, false otherwise.
     */
    private fun isWithinDateRange(date: Date, start: Date, end: Date): Boolean {
        return date in start..end
    }

    private fun recyclerSetup() {
        // Initialize the adapter with an empty list
        AnalysisBreakdownAdapter = AnalysisBreakdownAdapter(categoryAnalysisList)
        binding.analysisBreakdownRecycler.apply {
            adapter = AnalysisBreakdownAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    /**
     * Called when the view previously created by onCreateView has been detached from the fragment.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
