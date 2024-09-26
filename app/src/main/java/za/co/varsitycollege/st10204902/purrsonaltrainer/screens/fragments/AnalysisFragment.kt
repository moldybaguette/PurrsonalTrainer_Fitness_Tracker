package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.charts.Pie
import com.google.android.material.datepicker.MaterialDatePicker
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.AnalysisBreakdownAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.FragmentAnalysisBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.CategoryAnalysis
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Fragment for displaying analysis data using a pie chart.
 */
class AnalysisFragment : Fragment() {
    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!

    private lateinit var pie: Pie
    private lateinit var analysisBreakdownAdapter: AnalysisBreakdownAdapter
    private var categoryAnalysisList: List<CategoryAnalysis> = emptyList()

    private var explodedSlice: Int = -1 // Initialize to -1 indicating no slice is exploded

    /**
     * Called to have the fragment instantiate its user interface view.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Log.d("AnalysisFragment", "onCreateView")
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)

        // Initialize RecyclerView
        setupRecyclerView()

        // Initialize Pie Chart
        setupChart()

        // Set default date range
        val defaultStartDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2024-01-01") ?: Date(0)
        val defaultEndDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2024-12-31") ?: Date()

        val sdfDisplay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val myText = "${sdfDisplay.format(defaultStartDate)} - ${sdfDisplay.format(defaultEndDate)}"
        binding.displayDateRangeSelected.text = myText
        
        // Initial chart and adapter setup with default date range
        UserManager.user?.let { user ->
            updateAnalysis(user, defaultStartDate, defaultEndDate)
        }

        // Setup Date Picker
        setupDatePicker()
        return binding.root
    }

    /**
     * Sets up the RecyclerView with its adapter and layout manager.
     */
    private fun setupRecyclerView() {
        analysisBreakdownAdapter = AnalysisBreakdownAdapter(categoryAnalysisList)
        binding.analysisBreakdownRecycler.apply {
            adapter = analysisBreakdownAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    /**
     * Configures the pie chart's appearance, palette, and click listeners.
     */
    private fun setupChart() {
        pie = AnyChart.pie()

        // Pie chart setup
        pie.title("Set Distribution")

        // Enable animation
        pie.animation(true)
        pie.legend().enabled(false)
        pie.labels().enabled(true)
        pie.tooltip().enabled(false)
        pie.animation().duration(1500)

        // Configure title
        configureTitle(pie)

        // Configure labels
        configureLabels(pie)

        // Configure tooltips
        configureToolTips(pie)

        // Set custom colors
        val customColors = listOf(
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

        pie.palette(customColors.map {
            String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(requireContext(), it))
        }.toTypedArray())

        // Set the chart to the AnyChartView
        binding.anyChartPie.setChart(pie)

        // Set up slice click listener
        setupSliceClickListener()
    }

    /**
     * Sets up the slice click listener for the pie chart to handle slice exploding.
     */
    private fun setupSliceClickListener() {
        pie.setOnClickListener(object :
            ListenersInterface.OnClickListener(arrayOf<String>("x", "value")) {
            override fun onClick(event: Event) {
                val categoryName = event.data["x"] ?: return

                // Find the index of the clicked category
                val index = categoryAnalysisList.indexOfFirst { it.categoryName == categoryName }

                // Handle "No Data Available" case
                if (categoryAnalysisList.isEmpty()) {
                    // No action needed since there's only one slice
                    return
                }

                if (index == -1) {
                    // Slice not found in the list
                    return
                }

                // Toggle explosion state
                if (explodedSlice == index) {
                    pie.explodeSlice(index, false)
                    explodedSlice = -1
                } else {
                    // Reset previously exploded slice
                    if (explodedSlice != -1) {
                        pie.explodeSlice(explodedSlice, false)
                    }
                    pie.explodeSlice(index, true)
                    explodedSlice = index
                }
            }
        })
    }

    /**
     * Updates the analysis data, RecyclerView, and pie chart based on the selected date range.
     * @param user The user whose exercise data is being analyzed.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     */
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

        // Update the pie chart data
        updateChartData()
    }

    /**
     * Updates the pie chart data based on the current categoryAnalysisList.
     * Handles the "No Data Available" case by displaying a single slice.
     */
    private fun updateChartData() {
        val dataEntries: MutableList<DataEntry> = if (categoryAnalysisList.isEmpty()) {
            mutableListOf(
                object : DataEntry() {
                    init {
                        setValue("x", "No Data Available")
                        setValue("value", 1)
                    }
                }
            )
        } else {
            categoryAnalysisList.map { analysis ->
                object : DataEntry() {
                    init {
                        setValue("x", analysis.categoryName)
                        setValue("value", analysis.setCount)
                    }
                }
            }.toMutableList()
        }

        pie.data(dataEntries)
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

    /**
     * Calculates the distribution of exercises within a date range.
     * @param user The user whose exercise data is being analyzed.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
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

    /**
     * Retrieves the color resource ID for a given category position.
     * @param position The position of the category.
     * @return The color resource ID.
     */
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
     * Configures the pie chart's title.
     * @param pie The pie chart instance.
     */
    private fun configureTitle(pie: Pie) {
        pie.title().enabled(true)
        pie.title().text("Set Distribution")
        pie.title().fontColor("black")
        pie.title().fontSize(24)
        pie.title().fontWeight("bold")
        pie.title().fontFamily("")
    }

    /**
     * Configures the pie chart's labels.
     * @param pie The pie chart instance.
     */
    private fun configureLabels(pie: Pie) {
        val labels = pie.labels()
        labels.position("outside")
        labels.format("{%x}: {%value}")
        labels.fontColor("black")
        labels.fontSize(12)
        labels.fontWeight("bold")
    }

    /**
     * Configures the pie chart's tooltips.
     * @param pie The pie chart instance.
     */
    private fun configureToolTips(pie: Pie) {
        val toolTips = pie.tooltip()
        toolTips.titleFormat("{%x}")
        toolTips.format("{%value}")
        toolTips.fontColor("black")
        toolTips.fontSize(18)
        toolTips.fontWeight("bold")
    }

    /**
     * Called when the view previously created by onCreateView has been detached from the fragment.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
