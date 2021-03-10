package com.example.fitnesssample.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitnesssample.R
import com.example.fitnesssample.db.RunDTO
import com.example.fitnesssample.other.TrackingUtility
import kotlinx.android.synthetic.main.item_run.view.ivRunImage
import kotlinx.android.synthetic.main.item_run.view.tvAvgSpeed
import kotlinx.android.synthetic.main.item_run.view.tvCalories
import kotlinx.android.synthetic.main.item_run.view.tvDate
import kotlinx.android.synthetic.main.item_run.view.tvDistance
import kotlinx.android.synthetic.main.item_run.view.tvTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<RunDTO>() {

        override fun areItemsTheSame(oldItem: RunDTO, newItem: RunDTO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RunDTO, newItem: RunDTO): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<RunDTO>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_run,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {

        val run = differ.currentList[position]
        holder.bindContent(run)
    }

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindContent(run: RunDTO) {

            Glide.with(itemView).load(run.img).into(itemView.ivRunImage)

            val calendar = Calendar.getInstance().apply { timeInMillis = run.timestamp }
            val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            itemView.tvDate.text = dateFormat.format(calendar.time)

            val avgSpeed = "${run.avgSpeedInKMH}km/h"
            itemView.tvAvgSpeed.text = avgSpeed

            val distanceInKm = "${run.distanceInMeters / 1000f}km"
            itemView.tvDistance.text = distanceInKm

            itemView.tvTime.text = TrackingUtility.fetchFormattedStopWatchTime(run.timeInMillis)

            val caloriesBurned = "${run.caloriesBurned}kcal"
            itemView.tvCalories.text = caloriesBurned
        }
    }
}