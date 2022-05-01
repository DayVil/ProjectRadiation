package eu.chi.luh.projectradiation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import eu.chi.luh.projectradiation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapIntent: Intent
    private lateinit var solarRadiation: SolarRadiation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        solarRadiation = SolarRadiation(BuildConfig.OPEN_WEATHER_KEY)

        setContentView(binding.root)

        binding.oMapsButton.setOnClickListener {
            mapIntent = Intent(this, MapsActivity::class.java)
            startActivity(mapIntent)
        }

        binding.hardcodedLocationButton.setOnClickListener {
            solarRadiation.run()
        }
    }
}