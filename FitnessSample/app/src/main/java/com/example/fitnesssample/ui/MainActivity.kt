package com.example.fitnesssample.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitnesssample.R
import com.example.fitnesssample.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.bottomNavigationView
import kotlinx.android.synthetic.main.activity_main.navHostFragment
import kotlinx.android.synthetic.main.activity_main.toolbar

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigateToTrackingFragmentIfNeeded(intent)

        setSupportActionBar(toolbar)
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        bottomNavigationView.setOnNavigationItemReselectedListener { /* NO-OP */ }

        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->

                when (destination.id) {
                    R.id.settingsFragment,
                    R.id.runFragment,
                    R.id.statisticsFragment -> bottomNavigationView.isVisible = true
                    else -> bottomNavigationView.isVisible = false
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {

        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            navHostFragment.findNavController().navigate(R.id.action_global_trackingFragment)
        }
    }
}