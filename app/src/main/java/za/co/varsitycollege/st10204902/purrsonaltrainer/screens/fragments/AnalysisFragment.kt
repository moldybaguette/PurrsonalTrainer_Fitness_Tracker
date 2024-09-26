package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.charts.Pie
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.FragmentAnalysisBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.CategoryAnalysis
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.User
import com.google.android.material.datepicker.MaterialDatePicker
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.AnalysisBreakdownAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Fragment for displaying analysis data using a pie chart.
 */
class AnalysisFragment : Fragment() {
    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!

    private lateinit var analysisBreakdownAdapter: AnalysisBreakdownAdapter
    private var categoryAnalysisList: List<CategoryAnalysis> = emptyList()

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Log.d("AnalysisFragment", "onCreateView")
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)

        // Initialize RecyclerView
        setupRecyclerView()

        val defaultStartDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2024-01-01")?: Date(0)
        val defaultEndDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2024-12-31") ?: Date()

        val sdfDisplay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.displayDateRangeSelected.text = "${sdfDisplay.format(defaultStartDate)} - ${sdfDisplay.format(defaultEndDate)}"

        // Initial chart and adapter setup with default date range
        UserManager.user?.let { user ->
            updateAnalysis(user, defaultStartDate, defaultEndDate)
        }

        setupDatePicker()
        return binding.root
    }

    private fun setupRecyclerView() {
        analysisBreakdownAdapter = AnalysisBreakdownAdapter(categoryAnalysisList)
        binding.analysisBreakdownRecycler.apply {
            adapter = analysisBreakdownAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    /**
     * Sets up the pie chart with data.
     * @param anyChartPie The AnyChartView to display the pie chart.
     */
    private fun setupChart(anyChartPie: AnyChartView) {
        val pie: Pie = AnyChart.pie()

        // Pie chart setup
        pie.title("Set Distribution")
        pie.labels().position("outside")

        anyChartPie.setChart(pie)

        UserManager.user?.let { user ->
            // Use selected date range
            val selectedDateRange = binding.displayDateRangeSelected.text.toString().split(" - ")
            if (selectedDateRange.size != 2) {
                Log.w("AnalysisFragment", "Invalid date range format")
                return
            }

            val sdfInput = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDate = sdfInput.parse(selectedDateRange[0]) ?: Date(0)
            val endDate = sdfInput.parse(selectedDateRange[1]) ?: Date()

            val data = calculateExerciseDistribution(user, startDate, endDate)
            Log.d("AnalysisFragment", "Data: $data")

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
        val openDatePickerButton = binding.openDatePickerButton
        val displayDateRangeSelected = binding.displayDateRangeSelected

        openDatePickerButton.setOnClickListener {
            val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("SELECT A DATE RANGE")
                .setTheme(R.style.CalendarTheme)
                .build()

            dateRangePicker.show(parentFragmentManager, "dateRangePicker")

            dateRangePicker.addOnPositiveButtonClickListener { selection ->
                val startDateMillis = selection.first
                val endDateMillis = selection.second

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedStartDate = sdf.format(Date(startDateMillis))
                val formattedEndDate = sdf.format(Date(endDateMillis))

                val dateRangeText = "$formattedStartDate - $formattedEndDate"
                displayDateRangeSelected.text = dateRangeText

                Log.d("AnalysisFragment", "Selected date range: $dateRangeText")

                // Update chart and adapter based on the new date range
                UserManager.user?.let { user ->
                    updateAnalysis(user, Date(startDateMillis), Date(endDateMillis))
                }
            }
        }
    }

    private fun updateAnalysis(user: User, startDate: Date, endDate: Date) {
        val categoryCounts = calculateExerciseDistribution(user, startDate, endDate)
        val totalSets = categoryCounts.values.sum().toFloat()

        categoryAnalysisList = categoryCounts.entries.mapIndexed { index, entry ->
            val category = entry.key
            val count = entry.value
            CategoryAnalysis(
                categoryName = category,
                setCount = count,
                percentage = if (totalSets > 0) (count / totalSets) * 100 else 0f,
                color = getColorForCategory(index) // Pass the index directly
            )
        }

        if (categoryAnalysisList.isEmpty()) {
            binding.noDataTextView.visibility = View.VISIBLE
        } else {
            binding.noDataTextView.visibility = View.GONE
        }

        // Update the RecyclerView adapter
        analysisBreakdownAdapter.updateData(categoryAnalysisList)

        // Update the chart
        setupChart(binding.anyChartPie)
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
        startDate: Date,
        endDate: Date
    ): Map<String, Int> {
        val categoryCount = mutableMapOf<String, Int>()

        user.userWorkouts.values.forEach { workout ->
            if (isWithinDateRange(workout.date, startDate, endDate)) {
                workout.workoutExercises.values.forEach { exercise ->
                    val sets = exercise.sets.values.size
                    categoryCount[exercise.category] =
                        categoryCount.getOrDefault(exercise.category, 0) + sets
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

    private fun getColorForCategory(position: Int): Int {
        val colors = listOf(
            R.color.categoryPink,
            R.color.categoryLightYellow,
            R.color.categoryLightBlue,
            R.color.categoryRed,
            R.color.categoryGreen2,
            R.color.categoryOrange1,
            R.color.categoryOrange2,
            R.color.categoryYellow,
            R.color.categoryBlue,
            R.color.categoryDarkBlue,
            R.color.categoryPurple,
            R.color.categoryGreen1,
        )
        return colors[position % colors.size] // Ensure position is within bounds
    }

    /**
     * Called when the view previously created by onCreateView has been detached from the fragment.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
