package com.example.bachelor_work

    import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationBar)

        // Set up navigation
        val navController = findNavController(R.id.nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)

        // Listen for navigation changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Show/hide bottom navigation based on destination
            when (destination.id) {
                R.id.homeFragment, R.id.historyFragment, R.id.profileFragment, R.id.moreFragment, R.id.timerFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
                else -> {
                    bottomNavigationView.visibility = View.GONE
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
                R.id.navMore -> {
                    navController.navigate(R.id.moreFragment)
                    true
                }
                else -> false
            }
        }
    }
}
