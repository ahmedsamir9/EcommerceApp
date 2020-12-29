package com.example.ecommerce

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce.databinding.ActivityMainBinding
import com.example.ecommerce.utils.UiDisapperAndAppearInActivity
import com.example.ecommerce.utils.isVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),UiDisapperAndAppearInActivity {
private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding =DataBindingUtil.setContentView(this,R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_cart, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()||super.onSupportNavigateUp()
    }
    override fun showToolBar() {
        supportActionBar?.show()
    }

    override fun hideToolBar() {
       supportActionBar?.hide()
    }

    override fun hideNav() {
        activityMainBinding.navView.isVisible(false)
    }

    override fun showNav() {
        activityMainBinding.navView.isVisible(true)
    }
}