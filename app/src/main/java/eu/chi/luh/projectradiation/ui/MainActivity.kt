package eu.chi.luh.projectradiation.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import eu.chi.luh.projectradiation.R
import eu.chi.luh.projectradiation.datacollector.DataCollector
import eu.chi.luh.projectradiation.entities.AppDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    private lateinit var mapIntent: Intent
    private lateinit var db: AppDatabase
    private lateinit var dataCollector: DataCollector

    private val _lat = 52.512454
    private val _lon = 13.416506

    private var lastButtonReq: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        preRun()
        uiButtons()
    }

    private fun preRun() {
        val navFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "environment-data")
            .fallbackToDestructiveMigration().build()

        dataCollector = DataCollector(
            db,
            getString(R.string.OPEN_WEATHER_API),
            getString(R.string.AMBEE_API)
        )
        dataCollector.setPosition(_lat, _lon)
    }

    private fun uiButtons() {
//        binding.oMapsButton.setOnClickListener {
//            mapIntent = Intent(this, MapsActivity::class.java)
//            startActivity(mapIntent)
//        }
//
//        binding.hardcodedLocationButton.setOnClickListener {
//            if (System.currentTimeMillis() - lastButtonReq > 10000) {
//                GlobalScope.launch {
//                    dataCollector.collect()
//                }
//                lastButtonReq = System.currentTimeMillis()
//            }
//        }
//
//        binding.delData.setOnClickListener {
//            GlobalScope.launch {
//                db.environmentDao().deleteAll()
//            }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}