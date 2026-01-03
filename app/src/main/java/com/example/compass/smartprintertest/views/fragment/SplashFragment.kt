package com.example.compass.smartprintertest.views.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.compass.smartprintertest.R
import com.example.compass.smartprintertest.basefragment.BaseViewFragment
import com.example.compass.smartprintertest.databinding.FragmentSplashBinding
import com.example.compass.smartprintertest.flowNavigation.NavigationFragment.navigation
import com.example.compass.smartprintertest.utils.Extensions.isNetworkConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : BaseViewFragment<FragmentSplashBinding>(
    FragmentSplashBinding::inflate
) {

    private var progressStatus = 0
    private var isPaused = false

    override fun onFragmentReady(view: View, savedInstanceState: Bundle?) {

        progressStart()
    }

    private fun progressStart() {
        binding?.apply {
            progressStatus = 0

            lifecycleScope.launch(Dispatchers.IO) {
                while (progressStatus < 100 && !isPaused) {
                    progressStatus += 4

                    // Switch to main thread for UI updates
                    withContext(Dispatchers.Main) {

                        if (progressStatus == 100) {
                            splashShowAdsControl()
                        }
                    }

                    delay(if (activity.isNetworkConnected()) 220L else 100L)
                }
            }
        }
    }

    private fun splashShowAdsControl() {
        val action = SplashFragmentDirections.actionSplashFragmentToDashboardFragment()
        navigation(R.id.splashFragment, action)
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
    }

    override fun onResume() {
        super.onResume()
        if (isPaused && progressStatus <= 100) {
            isPaused = false
            progressStatus = 90
            progressStart()
        }
    }


    override fun onBackPress() {

    }

}