package eu.chi.luh.projectradiation.ui.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import eu.chi.luh.projectradiation.R
import eu.chi.luh.projectradiation.entities.Environment

class MaximizeCardFragment(env: Environment) : Fragment() {
    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.maximize_card, container, false)

        preRun()
        return viewOfLayout
    }

    private fun preRun() {
        (activity as AppCompatActivity).supportActionBar?.title = "TEST"
    }
}