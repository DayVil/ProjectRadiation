package eu.chi.luh.projectradiation.ui.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import eu.chi.luh.projectradiation.R
import eu.chi.luh.projectradiation.datacollector.DataCollector
import eu.chi.luh.projectradiation.entities.ProjectRadiationDatabase
import eu.chi.luh.projectradiation.map.MapData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CurrentRadiationFragment : Fragment() {

    private lateinit var viewModel: CurrentRadiationViewModel
    private lateinit var viewOfLayout: View

    private lateinit var db: ProjectRadiationDatabase
    private lateinit var dataCollector: DataCollector
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var mapData: MapData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.current_radiation_fragment, container, false)

        preRun()
        update()
        debug()

        return viewOfLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentRadiationViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun preRun() {
        db = ProjectRadiationDatabase.invoke(viewOfLayout.context)
        mapData = MapData.invoke()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        dataCollector = DataCollector(
            this.db,
            getString(R.string.OPEN_WEATHER_API),
            getString(R.string.TOMORROW_API)
        )
        // TODO Make this work
//        val tmpLocation = mapData.getCurrentLocation(requireActivity(), fusedLocationProviderClient)
//        mapData.setPosition(requireContext(), tmpLocation)
        dataCollector.collect()
    }

    // TODO make it async
    fun update() {
        val pCard = viewOfLayout.findViewById<LinearLayout>(R.id.primary_linear)
        val elemLen = pCard.size

        val cityName: TextView = pCard[0] as TextView

        val tmpUviNow: RelativeLayout = pCard[1] as RelativeLayout
        val uviNow: TextView = tmpUviNow[1] as TextView

        val tmpPollenAverage: RelativeLayout = pCard[2] as RelativeLayout
        val pollenAverage: TextView = tmpPollenAverage[1] as TextView

        val tmpTime: RelativeLayout = pCard[elemLen - 1] as RelativeLayout
        val time: TextView = tmpTime[1] as TextView

        if (db.environmentDao().checkEmpty() == null) return

        val lastEntry = db.environmentDao().getLast()

        val formatter = DateTimeFormatter.ofPattern("HH:mm   dd.MM.yyyy")
        val instant = Instant.ofEpochMilli(lastEntry.time)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

        cityName.text = lastEntry.name
        time.text = formatter.format(date)
        uviNow.text = String.format("%.2f", lastEntry.uvi?.uviAverage)
        pollenAverage.text = String.format("%.2f", lastEntry.pollen?.pollenAverage)
    }

    fun debug() {
        val btn = viewOfLayout.findViewById<Button>(R.id.del)
        val upt = viewOfLayout.findViewById<Button>(R.id.update)

        btn.setOnClickListener {
            GlobalScope.launch {
                db.environmentDao().deleteAll()
            }
        }

        upt.setOnClickListener {
            mapData.setPosition(requireContext(), 52.455319, 10.203917)
            dataCollector.collect()
            update()
        }
    }
}