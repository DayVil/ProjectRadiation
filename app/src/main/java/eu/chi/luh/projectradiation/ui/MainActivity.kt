package eu.chi.luh.projectradiation.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import eu.chi.luh.projectradiation.R
import eu.chi.luh.projectradiation.entities.AppDatabase
import eu.chi.luh.projectradiation.databinding.ActivityMainBinding
import eu.chi.luh.projectradiation.datacollector.DataCollector
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapIntent: Intent
    private lateinit var db: AppDatabase
    private lateinit var dataCollector: DataCollector

    private val _lat = 52.512454
    private val _lon = 13.416506

    private var lastButtonReq: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preRun()
        uiButtons()
    }

    private fun preRun() {
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "environment-data")
            .fallbackToDestructiveMigration().build()
//        db.environmentDao().deleteAll()

        dataCollector = DataCollector(
            db,
            getString(R.string.OPEN_WEATHER_API),
            getString(R.string.AMBEE_API)
        )
        dataCollector.setPosition(_lat, _lon)
    }

    private fun uiButtons() {
        binding.oMapsButton.setOnClickListener {
            mapIntent = Intent(this, MapsActivity::class.java)
            startActivity(mapIntent)
        }

        binding.hardcodedLocationButton.setOnClickListener {
            if (System.currentTimeMillis() - lastButtonReq > 10000) {
                GlobalScope.launch {
                    dataCollector.collect()
                }
                lastButtonReq = System.currentTimeMillis()
            }
        }

        binding.delData.setOnClickListener {
            GlobalScope.launch {
                db.environmentDao().deleteAll()
            }
        }
    }
}