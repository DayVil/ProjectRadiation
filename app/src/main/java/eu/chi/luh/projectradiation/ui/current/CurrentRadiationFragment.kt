package eu.chi.luh.projectradiation.ui.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import eu.chi.luh.projectradiation.R
import eu.chi.luh.projectradiation.datacollector.DataCollector
import eu.chi.luh.projectradiation.entities.ProjectRadiationDatabase
import eu.chi.luh.projectradiation.map.MapData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CurrentRadiationFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var db: ProjectRadiationDatabase
    private lateinit var dataCollector: DataCollector
    private lateinit var mapData: MapData

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(R.layout.current_radiation_fragment, container, false)

        preRun()
        update()

        return viewOfLayout
    }

    private fun preRun() {
        // Inits
        (activity as AppCompatActivity).supportActionBar?.title = "Current"
        db = ProjectRadiationDatabase.invoke(viewOfLayout.context)
        mapData = MapData.invoke()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        dataCollector = DataCollector(
            this.db,
            getString(R.string.OPEN_WEATHER_API),
            getString(R.string.TOMORROW_API)
        )

        // Recycler view init
        val recView = viewOfLayout.findViewById<RecyclerView>(R.id.recyclerView)
        layoutManager = LinearLayoutManager(requireContext())
        recView.layoutManager = layoutManager
        adapter = RecyclerAdapter()
        recView.adapter = adapter


        // Elements on Display
        // Search Bar
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

        // Refresher
        val refresh = viewOfLayout.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        refresh.setColorSchemeColors(viewOfLayout.resources.getColor(R.color.orange_l))
        refresh.setOnRefreshListener {
            dataCollector.collect()
            update()
            refresh.isRefreshing = false
        }

        // Run and set from current location
        mapData.setFromCurrentLocation(requireActivity(), fusedLocationProviderClient)
        dataCollector.collect()
    }

    private fun update() {
        try {
            lifecycleScope.launch {
                delay(1L)
                adapter?.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            lifecycleScope.launch {
                delay(3L)
                adapter?.notifyDataSetChanged()
            }
        }

        lifecycleScope.launch {
            var amountInStorage = db.environmentDao().getCount()
            while (amountInStorage > 12) {
                val oldestMember = db.environmentDao().getFirst()
                db.environmentDao().delete(oldestMember)
                amountInStorage = db.environmentDao().getCount()
            }
        }
    }
}