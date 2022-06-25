import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import eu.chi.luh.projectradiation.R

class SettingsFragment : Fragment() {
    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.settings_fragment, container, false)
        preRun()

        return viewOfLayout
    }

    private fun preRun() {
        (activity as AppCompatActivity).supportActionBar?.title = "Settings"
    }
}