package eu.chi.luh.projectradiation.ui.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import eu.chi.luh.projectradiation.R
import eu.chi.luh.projectradiation.entities.ProjectRadiationDatabase
import eu.chi.luh.projectradiation.entities.Environment


class MaximizeFragment : Fragment() {
    private lateinit var viewOfLayout: View
    private lateinit var db: ProjectRadiationDatabase

    fun changeTitle(name: String) {
        (activity as AppCompatActivity).supportActionBar?.title = name
    }

    fun populatePage(env: Environment?) {
        val title: String? = "${env?.cityName}, ${env?.countryName}"

        val uviCurrent: Double? = env?.uvi?.uviCurrent
        val uviAverage: Double? = env?.uvi?.uviAverage
        val uviMax: Double? = env?.uvi?.uviMaximum
        val uviMin: Double? = env?.uvi?.uviMinimum

        val genCurrent: Double? = env?.pollen?.pollenCurrent
        val genAverage: Double? = env?.pollen?.pollenAverage
        val genMax: Double? = env?.pollen?.pollenMaximum
        val genMin: Double? = env?.pollen?.pollenMinimum

        val grassCurrent: Int? = env?.pollen?.pollenGrassCurrent
        val grassAverage: Double? = env?.pollen?.pollenGrassAverage
        val grassMax: Int? = env?.pollen?.pollenGrassMaximum
        val grassMin: Int? = env?.pollen?.pollenGrassMinimum

        val treeCurrent: Int? = env?.pollen?.pollenTreeCurrent
        val treeAverage: Double? = env?.pollen?.pollenTreeAverage
        val treeMax: Int? = env?.pollen?.pollenTreeMaximum
        val treeMin: Int? = env?.pollen?.pollenTreeMinimum

        val weedCurrent: Int? = env?.pollen?.pollenWeedCurrent
        val weedAverage: Double? = env?.pollen?.pollenWeedAverage
        val weedMax: Int? = env?.pollen?.pollenWeedMaximum
        val weedMin: Int? = env?.pollen?.pollenWeedMinimum

        if (env == null) {
            changeTitle("UNKNOWN")
        } else {
            changeTitle(title!!)
        }

        viewOfLayout.findViewById<TextView>(R.id.current_uvi_value_maximise).text = String.format("%.2f", uviCurrent)
        viewOfLayout.findViewById<TextView>(R.id.average_uvi_value_maximise).text = String.format("%.2f", uviAverage)
        viewOfLayout.findViewById<TextView>(R.id.maximum_uvi_value_maximise).text = String.format("%.2f", uviMax)
        viewOfLayout.findViewById<TextView>(R.id.minimum_uvi_value_maximise).text = String.format("%.2f", uviMin)

        viewOfLayout.findViewById<TextView>(R.id.current_gen_value_maximise).text = String.format("%.2f", genCurrent)
        viewOfLayout.findViewById<TextView>(R.id.average_gen_value_maximise).text = String.format("%.2f", genAverage)
        viewOfLayout.findViewById<TextView>(R.id.maximum_gen_value_maximise).text = String.format("%.2f", genMax)
        viewOfLayout.findViewById<TextView>(R.id.minimum_gen_value_maximise).text = String.format("%.2f", genMin)

        viewOfLayout.findViewById<TextView>(R.id.current_grass_value_maximise).text = grassCurrent.toString()
        viewOfLayout.findViewById<TextView>(R.id.average_grass_value_maximise).text = String.format("%.2f", grassAverage)
        viewOfLayout.findViewById<TextView>(R.id.maximum_grass_value_maximise).text = grassMax.toString()
        viewOfLayout.findViewById<TextView>(R.id.minimum_grass_value_maximise).text = grassMin.toString()

        viewOfLayout.findViewById<TextView>(R.id.current_tree_value_maximise).text = treeCurrent.toString()
        viewOfLayout.findViewById<TextView>(R.id.average_tree_value_maximise).text = String.format("%.2f", treeAverage)
        viewOfLayout.findViewById<TextView>(R.id.maximum_tree_value_maximise).text = treeMax.toString()
        viewOfLayout.findViewById<TextView>(R.id.minimum_tree_value_maximise).text = treeMin.toString()

        viewOfLayout.findViewById<TextView>(R.id.current_weed_value_maximise).text = weedCurrent.toString()
        viewOfLayout.findViewById<TextView>(R.id.average_weed_value_maximise).text = String.format("%.2f", weedAverage)
        viewOfLayout.findViewById<TextView>(R.id.maximum_weed_value_maximise).text = weedMax.toString()
        viewOfLayout.findViewById<TextView>(R.id.minimum_weed_value_maximise).text = weedMin.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeTitle("City_Name,Country")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.maximize_fragement, container, false)
        db = ProjectRadiationDatabase.invoke(viewOfLayout.context)
        val pos: Int? = arguments?.getInt("position")

        db.environmentDao().getAll().let { ls ->
            if (pos != null) {
                val env = ls[pos]
                populatePage(env)
            } else {
                populatePage(null)
            }
        }

        return viewOfLayout
    }
}