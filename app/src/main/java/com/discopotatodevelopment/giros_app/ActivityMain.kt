package com.discopotatodevelopment.giros_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.discopotatodevelopment.giros_app.account.fragments.FragmentAccount
import com.discopotatodevelopment.giros_app.dataCoordinator.DataCoordinator
import com.discopotatodevelopment.giros_app.dataCoordinator.getisLogin
import com.discopotatodevelopment.giros_app.databinding.ActivityMainBinding
import com.discopotatodevelopment.giros_app.main.fragments.FragmentHistory
import com.discopotatodevelopment.giros_app.main.fragments.FragmentMain
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ActivityMain : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            DataCoordinator.shared.initialize(
                context = baseContext,
                onLoad = {})

            checkLogin()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        //windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())

        MAIN = this
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.main_screen -> {
                    loadFragment(FragmentMain())
                    true
                }
                R.id.history_screen -> {
                    loadFragment(FragmentHistory())
                    true
                }
                R.id.account_screen -> {
                    loadFragment(FragmentAccount())
                    true
                }
                else -> {throw AssertionError()}
            }
        }

        bottomNav.setupWithNavController(Navigation.findNavController(this, R.id.nav_host_fragment))
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

    private suspend fun checkLogin() {
        if (DataCoordinator.shared.getisLogin()) {
            supportFragmentManager.beginTransaction().replace(R.id.main_screen, FragmentMain())
        }
    }
}