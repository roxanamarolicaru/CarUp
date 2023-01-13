package com.polito.s287503.showprofileactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.polito.s287503.showprofileactivity.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        //val navController = findNavController(R.id.nav_host_fragment)


        NavigationUI.setupWithNavController(binding.navView, navController)
        NavigationUI.setupActionBarWithNavController( this, navController, binding.drawerLayout) */

    }

    /*override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navigation)

        //return NavigationUI.navigateUp(navController, drawerLayout)
    }*/

}