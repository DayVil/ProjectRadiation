package eu.chi.luh.projectradiation.ui.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import eu.chi.luh.projectradiation.R
import eu.chi.luh.projectradiation.datacollector.DataCollector
import eu.chi.luh.projectradiation.entities.tmp.TemporaryData.Companion.db
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrentRadiationFragment : Fragment() {

    companion object {
        fun newInstance() = CurrentRadiationFragment()
    }

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
        val btn = viewOfLayout.findViewById<Button>(R.id.collect_tmp)
        val btnDel = viewOfLayout.findViewById<Button>(R.id.remove_data)

        val dataCollector =
            DataCollector(
                db,
                getString(R.string.OPEN_WEATHER_API),
                getString(R.string.TOMORROW_API)
            )
        dataCollector.setPosition(52.3759, 9.7320)

        btn.setOnClickListener {
            GlobalScope.launch {
                dataCollector.collect(60)
            }
        }

        btnDel.setOnClickListener {
            GlobalScope.launch {
                db.environmentDao().deleteAll()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentRadiationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}