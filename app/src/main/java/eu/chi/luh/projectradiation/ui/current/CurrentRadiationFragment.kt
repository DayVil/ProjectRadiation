package eu.chi.luh.projectradiation.ui.current

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.chi.luh.projectradiation.R

class CurrentRadiationFragment : Fragment() {

    companion object {
        fun newInstance() = CurrentRadiationFragment()
    }

    private lateinit var viewModel: CurrentRadiationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_radiation_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentRadiationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}