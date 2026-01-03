package com.example.compass.smartprintertest.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.compass.smartprintertest.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class PrefHelper private constructor(activity: Context?) {

    private var sharedPref: SharedPreferences? = null

    init {
        if (activity != null) {
            sharedPref = activity.getSharedPreferences(
                activity.getString(R.string.app_name), Context.MODE_PRIVATE
            )
        }
    }

    companion object {
        @Volatile
        private var prefHelper: PrefHelper? = null

        fun getPrefInstance(activity: Context?): PrefHelper {
            return prefHelper ?: synchronized(this) {
                prefHelper ?: PrefHelper(activity).also { prefHelper = it }
            }
        }
    }


    var locationDenyCount: Int
        get() = sharedPref!!.getInt("locationDenyCount", 0)
        set(value) {
            sharedPref!!.edit().putInt("locationDenyCount", value).apply()
        }


}
