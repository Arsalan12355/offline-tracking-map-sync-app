package com.example.compass.smartprintertest.flowNavigation

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

object NavigationFragment {

    fun Fragment.navigation(fragmentId:Int, action: NavDirections) {
        if ( isAdded &&  isVisible) {
            try {
                val navControl= findNavController()
                if (navControl.currentDestination?.id == fragmentId) {
                    navControl.navigate(action)
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




}