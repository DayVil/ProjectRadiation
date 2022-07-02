package eu.chi.luh.projectradiation.ui.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import eu.chi.luh.projectradiation.R
import eu.chi.luh.projectradiation.entities.ProjectRadiationDatabase
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private lateinit var db: ProjectRadiationDatabase

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_summary_data, parent, false)
        db = ProjectRadiationDatabase.invoke(parent.context)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        db.environmentDao().getAll().let { allData ->
            val data = allData[position]

            val title = "${data.cityName}, ${data.countryName}"
            holder.locationName.text = title

            holder.uviValue.text = String.format("%.2f", data.uvi?.uviCurrent)
            holder.pollenValue.text = String.format("%.2f", data.pollen?.pollenCurrent)

            val formatter = DateTimeFormatter.ofPattern("HH:mm   dd.MM.yyyy")
            val instant = Instant.ofEpochMilli(data.time)
            val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

            holder.timeValue.text = formatter.format(date)
        }
    }

    override fun getItemCount(): Int {
        if (::db.isInitialized) {
            val amountDatabase = db.environmentDao().getCount()
            return amountDatabase
        }
        return 1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var locationName: TextView = itemView.findViewById(R.id.location)
        var uviValue: TextView = itemView.findViewById(R.id.uvi_value)
        var pollenValue: TextView = itemView.findViewById(R.id.pollen_value)
        var timeValue: TextView = itemView.findViewById(R.id.update_value)

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition

                val bundle = bundleOf("position" to position)

//                Navigation.findNavController(itemView)
//                    .navigate(R.id.action_currentWeatherFragment_to_maximizeFragement)

                itemView.findNavController().navigate(R.id.action_currentWeatherFragment_to_maximizeFragement, bundle)
            }
        }

    }
}