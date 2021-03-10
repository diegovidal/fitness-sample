package com.example.fitnesssample.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fitnesssample.R
import com.example.fitnesssample.other.CustomMarkerView
import com.example.fitnesssample.other.TrackingUtility
import com.example.fitnesssample.ui.viewmodels.StatisticsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_statistics.barChart
import kotlinx.android.synthetic.main.fragment_statistics.tvAverageSpeed
import kotlinx.android.synthetic.main.fragment_statistics.tvTotalCalories
import kotlinx.android.synthetic.main.fragment_statistics.tvTotalDistance
import kotlinx.android.synthetic.main.fragment_statistics.tvTotalTime
import kotlin.math.round

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()
        setupBarChart()
    }

    private fun subscribeToObservers() {

        viewModel.totalTimeRun.observe(viewLifecycleOwner, Observer {
            it?.let { tvTotalTime.text = TrackingUtility.fetchFormattedStopWatchTime(it) }
        })

        viewModel.totalDistance.observe(viewLifecycleOwner, Observer {

            it?.let {
                val km = it / 1000f
                val totalDistance = round(km * 10f)
                val totalDistanceStr = "${totalDistance}km"

                tvTotalDistance.text = totalDistanceStr
            }
        })

        viewModel.totalAvgSpeed.observe(viewLifecycleOwner, Observer {
            it?.let {
                val avgSpeed = round(it * 10f) / 10f
                val avgSpeedStr = "${avgSpeed}km/h"

                tvAverageSpeed.text = avgSpeedStr
            }
        })

        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner, Observer {
            it?.let {
                val totalCaloriesStr = "${it}kcal"
                tvTotalCalories.text = totalCaloriesStr
            }
        })

        viewModel.runsSortedByDate.observe(viewLifecycleOwner, Observer {
            it?.let {
                val allAvgSpeeds = it.indices.map { i -> BarEntry(i.toFloat(), it[i].avgSpeedInKMH) }
                val bardataSet = BarDataSet(allAvgSpeeds, "Average Speed Over Time").apply {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                }

                barChart.data = BarData(bardataSet)
                barChart.marker = CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                barChart.invalidate()
            }
        })
    }

    private fun setupBarChart() {

        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        barChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        barChart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        barChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }
    }
}