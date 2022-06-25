package eu.chi.luh.projectradiation.ui.current

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import eu.chi.luh.projectradiation.R

class MaximizeCard(env: Environment): Fragment() {
    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.current_radiation_fragment, container, false)

        preRun()
        return viewOfLayout
    }

    private fun preRun() {
        (activity as AppCompatActivity).supportActionBar?.title = "Current"
    }
}