package eu.chi.luh.projectradiation.ui.current

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import eu.chi.luh.projectradiation.R
import eu.chi.luh.projectradiation.datacollector.DataCollector
import eu.chi.luh.projectradiation.entities.ProjectRadiationDatabase
import eu.chi.luh.projectradiation.map.MapData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrentRadiationFragment : Fragment() {

    private lateinit var viewModel: CurrentRadiationViewModel
    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.current_radiation_fragment, container, false)

        debugFun()

        return viewOfLayout
    }

    /**
     * For debugging purposes
     */
    fun debugFun() {
        val db: ProjectRadiationDatabase = ProjectRadiationDatabase.invoke(viewOfLayout.context)
        val mapData = MapData.invoke()

        val btn = viewOfLayout.findViewById<Button>(R.id.collect_tmp)
        val btnDel = viewOfLayout.findViewById<Button>(R.id.remove_data)
        val txtView = viewOfLayout.findViewById<TextView>(R.id.textView)

        val dataCollector =
            DataCollector(
                db,
                getString(R.string.OPEN_WEATHER_API),
                getString(R.string.TOMORROW_API)
            )

        Log.d("MapData", mapData.getCityName())
        btn.setOnClickListener {
            GlobalScope.launch {
                dataCollector.collect(60)
                txtView.text = db.environmentDao().getLast().pollen?.pollenTreeCurrent.toString()
            }
        }

        btnDel.setOnClickListener {
            GlobalScope.launch {
                mapData.setPosition(requireContext(), 51.030441, -1.294777)
                db.environmentDao().deleteAll()
                txtView.text = "EMPTY"
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentRadiationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}