package eu.chi.luh.projectradiation.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import eu.chi.luh.projectradiation.BuildConfig
import eu.chi.luh.projectradiation.database.AppDatabase
import eu.chi.luh.projectradiation.database.Environment
import eu.chi.luh.projectradiation.databinding.ActivityMainBinding
import eu.chi.luh.projectradiation.datacollector.SolarRadiation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapIntent: Intent
    private lateinit var solarRadiation: SolarRadiation
    private lateinit var db: AppDatabase

    private val lat = 52.512454
    private val lon = 13.416506

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        solarRadiation = SolarRadiation(BuildConfig.OPEN_WEATHER_KEY, lat, lon)

        setContentView(binding.root)

        preRun()
        uiButtons()
    }

    private fun preRun() {
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "Reads-Database")
            .build()
    }

    private fun uiButtons() {
        binding.oMapsButton.setOnClickListener {
            mapIntent = Intent(this, MapsActivity::class.java)
            startActivity(mapIntent)
        }

        binding.hardcodedLocationButton.setOnClickListener {
            //
            GlobalScope.launch {
                val tmpdt = System.currentTimeMillis()
                val uviData = solarRadiation.run()
//                db.environmentDao().deleteAll()
                db.environmentDao().insertEnvironment(Environment(tmpdt, uviData!!))
                val data = db.environmentDao().getAll()

                data.forEach { a ->
                    Log.d("DATABASE", "${a.dt}:\t ${a.uvi}")
                }
            }
            //
        }
    }
}