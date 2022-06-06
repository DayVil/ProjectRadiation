package eu.chi.luh.projectradiation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import eu.chi.luh.projectradiation.R
import eu.chi.luh.projectradiation.datacollector.DataCollector
import eu.chi.luh.projectradiation.entities.ProjectRadiationDatabase
import eu.chi.luh.projectradiation.map.MapData

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var dataCollector: DataCollector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        preRun()
    }

    /**
     * Runs the necessary steps before final launch
     */
    private fun preRun() {
        val navFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)

        val db: ProjectRadiationDatabase = ProjectRadiationDatabase.invoke(applicationContext)
        val mapData = MapData.invoke()

        mapData.setPosition(52.5200, 13.4050)

        dataCollector = DataCollector(
            db,
            getString(R.string.OPEN_WEATHER_API),
            getString(R.string.TOMORROW_API)
        )
        mapData.printPos("MainActivity")
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}