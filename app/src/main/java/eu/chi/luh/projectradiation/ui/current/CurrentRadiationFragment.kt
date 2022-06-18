package eu.chi.luh.projectradiation.ui.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.card.MaterialCardView
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
        viewModel = ViewModelProvider(this)[CurrentRadiationViewModel::class.java]
        // TODO: Use the ViewModel
    }

    private fun preRun() {
        // Initialise
        db = ProjectRadiationDatabase.invoke(viewOfLayout.context)
        mapData = MapData.invoke()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        dataCollector = DataCollector(
            this.db,
            getString(R.string.OPEN_WEATHER_API),
            getString(R.string.TOMORROW_API)
        )

        // Elements on Display
        val search = viewOfLayout.findViewById<SearchView>(R.id.place_searcher)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                search.clearFocus()

                var tmpString = ""
                var hasContent = false
                if (p0 != null) {
                    tmpString = p0
                    hasContent = true
                }

                val location = mapData.getAddressLocation(requireContext(), tmpString)
                mapData.setPosition(requireContext(), location)
                dataCollector.collect()
                update()

                return hasContent
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        val refresh = viewOfLayout.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        refresh.setOnRefreshListener {
            dataCollector.collect()
            update()
            refresh.isRefreshing = false
        }

        // Run from current location
        mapData.setFromCurrentLocation(requireActivity(), fusedLocationProviderClient)
        dataCollector.collect()
    }

    // TODO Uvi is shown wrong
    private fun update() {
        val cardStack = viewOfLayout.findViewById<ScrollView>(R.id.scroll_cards)
        val cardsAmount = cardStack.size

        for (i in 0 until cardsAmount) {
            val selectCard = cardStack[0] as MaterialCardView // Gets the Material Card
            val selectLayout =
                selectCard[0] as LinearLayout // Gets the components from the Material Card

            val amountVal = selectLayout.size // Amount of Components

            val cityName: TextView = selectLayout[0] as TextView

            val tmpUviNow: RelativeLayout = selectLayout[1] as RelativeLayout
            val uviNow: TextView = tmpUviNow[1] as TextView

            val tmpPollenAverage: RelativeLayout = selectLayout[2] as RelativeLayout
            val pollenNow: TextView = tmpPollenAverage[1] as TextView

            val tmpTime: RelativeLayout = selectLayout[amountVal - 1] as RelativeLayout
            val time: TextView = tmpTime[1] as TextView

            if (db.environmentDao().checkEmpty() == null) return

            // Fill in the components
            val lastEntry = db.environmentDao().getLast()

            val formatter = DateTimeFormatter.ofPattern("HH:mm   dd.MM.yyyy")
            val instant = Instant.ofEpochMilli(lastEntry.time)
            val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

            // TODO round number if numbers are not fractions
            val displayText = "${lastEntry.name}, ${lastEntry.countryName}"
            cityName.text = displayText

            uviNow.text = String.format("%.2f", lastEntry.uvi?.uviCurrent)
            pollenNow.text = String.format("%.2f", lastEntry.pollen?.pollenCurrent)

            time.text = formatter.format(date)
        }
    }

    private fun debug() {
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