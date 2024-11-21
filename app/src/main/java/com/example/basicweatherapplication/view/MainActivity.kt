package com.example.basicweatherapplication.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.basicweatherapplication.R
import com.example.basicweatherapplication.databinding.ActivityMainBinding
import com.example.basicweatherapplication.model.weather.WeatherResponseModel
import com.example.basicweatherapplication.viewModel.LocationViewModel
import com.example.basicweatherapplication.viewModel.WeatherViewModel
import kotlinx.coroutines.launch

open class MainActivity : AppCompatActivity() {
    private var icon = "02d"
    var latitude: Double? = null
    var longitude: Double? = null
    private var currentLocation: Location? = null
    private var locationByGps: Location? = null
    private var locationByNetwork: Location? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeLiveData()
        binding.getDataButton.setOnClickListener {
            fetchWeatherFromApi(binding.inputText.text.toString())
        }

        val sharedPreferences = getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
        val city = sharedPreferences.getString(CITY_NAME, null)
        if (city != null) {
            retainUI(city, sharedPreferences)
        } else {
            getLocation()
        }
    }

    override fun onDestroy() {
        saveData()
        super.onDestroy()
    }

    @SuppressLint("MissingPermission") // This method is only called after permissions are granted.
    @VisibleForTesting(otherwise = MODE_PRIVATE)
    internal fun getLatLong() {
        val lastKnownLocationByGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnownLocationByGps?.let {
            locationByGps = lastKnownLocationByGps
        }
        val lastKnownLocationByNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        lastKnownLocationByNetwork?.let {
            locationByNetwork = lastKnownLocationByNetwork
        }
        if (locationByGps != null && locationByNetwork != null) {
            if (locationByGps?.accuracy!! > locationByNetwork!!.accuracy)
                setLatLong(locationByGps)
            else
                setLatLong(locationByNetwork)
        } else if (locationByGps != null) {
            setLatLong(locationByGps)
        } else if (locationByNetwork != null) {
            setLatLong(locationByNetwork)
        }
        if (latitude != null && longitude != null)
            fetchLocationFromApi(latitude!!, longitude!!)
    }

    @VisibleForTesting(otherwise = MODE_PRIVATE)
    internal fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        // Precise location access granted.
                        getLatLong()
                    }
                    else -> {
                        // No location access granted.
                        showPermissionToast()
                    }
                }
            }
            locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        } else {
            getLatLong()
        }
    }

    @VisibleForTesting(otherwise = MODE_PRIVATE)
    internal fun fetchLocationFromApi(latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            locationViewModel.fetchLocation(latitude, longitude, 5)
        }
    }

    @VisibleForTesting(otherwise = MODE_PRIVATE)
    internal fun fetchWeatherFromApi(city: String) {
        lifecycleScope.launch {
            weatherViewModel.fetchWeather(city)
        }
    }

    private fun observeLiveData() {
        weatherViewModel.weatherData.observe(this) { weatherResponseModel ->
            weatherResponseModel?.let { updateUI(it) }
                ?: run {
                    showInvalidCityUI()
                }
        }
        locationViewModel.locationData.observe(this) { locationModel ->
            locationModel?.let {
                binding.inputText.setText(it.name)
                fetchWeatherFromApi(it.name)
            }
        }
    }

    private fun showPermissionToast() = Toast.makeText(this, "Please grant Location Permission from Settings", Toast.LENGTH_LONG).show()

    private fun retainUI(city: String, sharedPreferences: SharedPreferences) {
        binding.inputText.setText(city)
        val iconUrl = ICON_URL_PREFIX + "${sharedPreferences.getString(WEATHER_ICON, "02d")}@4x.png"
        Glide.with(binding.weatherIcon.context).load(iconUrl).into(binding.weatherIcon)
        binding.temperatureText.text = sharedPreferences.getString(TEMPERATURE, null) ?: EMPTY_STRING
        binding.descriptionText.text = sharedPreferences.getString(TEMPERATURE_DESC, null) ?: EMPTY_STRING
    }

    private fun saveData() {
        val editor = getSharedPreferences().edit()
        editor.putString(CITY_NAME, binding.inputText.text.toString())
        editor.putString(WEATHER_ICON, icon)
        editor.putString(TEMPERATURE, binding.temperatureText.text.toString())
        editor.putString(TEMPERATURE_DESC, binding.descriptionText.text.toString())
        editor.apply()
    }

    private fun setLatLong(location: Location?) {
        currentLocation = location
        latitude = currentLocation?.latitude ?: ZERO_DOUBLE
        longitude = currentLocation?.longitude ?: ZERO_DOUBLE
    }

    @VisibleForTesting(otherwise = MODE_PRIVATE)
    internal fun updateUI(weatherResponseModel: WeatherResponseModel) {
        binding.errorText.visibility = View.GONE
        binding.temperatureText.text = "${weatherResponseModel.temperatureDetailsModel.temperature}"
        binding.descriptionText.text = weatherResponseModel.weatherModelList[0].description
        icon = weatherResponseModel.weatherModelList[0].icon
        val iconUrl = ICON_URL_PREFIX + "${icon}@4x.png"
        Glide.with(binding.weatherIcon.context).load(iconUrl).into(binding.weatherIcon)
    }

    private fun getSharedPreferences()  = getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)

    private fun showInvalidCityUI() {
        binding.errorText.visibility = View.VISIBLE
        binding.temperatureText.text = ""
        binding.descriptionText.text = ""
        icon = "02d"
        binding.weatherIcon.setImageResource(R.drawable.weather_icon)
    }
    companion object {
        private const val CITY_NAME = "CITY_NAME"
        private const val WEATHER_ICON = "WEATHER_ICON"
        private const val TEMPERATURE = "TEMPERATURE"
        private const val TEMPERATURE_DESC = "TEMPERATURE_DESC"
        private const val ICON_URL_PREFIX = "https://openweathermap.org/img/wn/"
        private const val EMPTY_STRING = ""
        private const val ZERO_DOUBLE = 0.0
    }
}