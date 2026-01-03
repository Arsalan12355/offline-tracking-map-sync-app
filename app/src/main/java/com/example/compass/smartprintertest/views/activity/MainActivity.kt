package com.example.compass.smartprintertest.views.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.compass.smartprintertest.R
import com.example.compass.smartprintertest.databinding.ActivityMainBinding
import com.example.compass.smartprintertest.utils.Extensions.hideNavigationBar
import com.example.compass.smartprintertest.utils.Extensions.showNavigationBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var navHost: NavHostFragment? = null
    private var navController: NavController? = null
    private var lastVisibleInsets: WindowInsetsCompat? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            enableEdgeToEdge()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initNavigation()


    }


    private fun initNavigation() {

        navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment?
        navHost?.let {
            navController = it.navController

        } ?: throw IllegalStateException("NavHostFragment not found")

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> hideNavigationBar()

                else -> showNavigationBar()
            }
        }


    }


    private fun init() {
        binding.root.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { view, insets ->
                val statusVisible = insets.isVisible(WindowInsetsCompat.Type.statusBars())
                val navVisible = insets.isVisible(WindowInsetsCompat.Type.navigationBars())

                if (statusVisible || navVisible) {
                    lastVisibleInsets = insets // Save when bars are visible
                }

                val insetToUse = lastVisibleInsets ?: insets
                val systemBars = insetToUse.getInsets(WindowInsetsCompat.Type.systemBars())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
                    view.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.app_bg
                        )
                    )

                    view.setPadding(
                        systemBars.left,
                        if (statusVisible) systemBars.top else systemBars.top, // Always apply top
                        systemBars.right,
                        if (navVisible) systemBars.bottom else 0
                    )
                }
                insets
            }
        }
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

    }


}