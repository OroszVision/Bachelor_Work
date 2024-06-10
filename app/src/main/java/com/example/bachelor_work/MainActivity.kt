package com.example.bachelor_work

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationBar)
        toolbar = findViewById(R.id.topToolbar)
        navController = findNavController(R.id.nav_host_fragment)

        setSupportActionBar(toolbar)

        // Set up navigation
        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController)

        // Listen for navigation changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Show/hide bottom navigation based on destination
            when (destination.id) {
                R.id.homeFragment, R.id.historyFragment, R.id.profileFragment, R.id.statisticsFragment, R.id.timerFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                else -> {
                    bottomNavigationView.visibility = View.GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
            }
        }

        // Handle bottom navigation item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navHome -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.navHistory -> {
                    navController.navigate(R.id.historyFragment)
                    true
                }
                R.id.navTimer -> {
                    navController.navigate(R.id.timerFragment)
                    true
                }
                R.id.navProfile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.navStatistics -> {
                    navController.navigate(R.id.statisticsFragment)
                    true
                }
                else -> false
            }
        }

        // Handle click on menu icon
        toolbar.findViewById<View>(R.id.menuIcon).setOnClickListener {
            showPopupMenu(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.dropdown_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    // Handle settings item click
                    true
                }
                R.id.action_guide -> {
                    // Handle guide item click
                    true
                }
                // Add more menu items here if needed
                else -> false
            }
        }
        popupMenu.show()
    }
}
