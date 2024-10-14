package com.example.weatherapp.ui

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.repositories.WeatherRepoImp
import com.example.weatherapp.ui.ViewModeFactory.WeatherViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class WeatherFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            WeatherViewModelFactory(WeatherRepoImp, requireActivity().application)
        )[WeatherViewModel::class.java]
    }

    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private var lastCity: String? = null
    private var binding: FragmentWeatherBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding?.root
    }

    //TODO: if given more time, add refresh button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // observe the weather from view model and display it if successful
        viewModel.weatherResult.observe(viewLifecycleOwner) {
            // if successful, display the weather conditons of the searched city
            if (it.isSuccessful) {
                if (it.body()?.weatherData?.isNotEmpty() == true) {
                    lastCity?.let { city -> viewModel.setLastCity(city) }
                    val weather = it.body()?.weatherData?.get(0)
                    binding?.weatherConditionTextView?.text = weather?.main
                    binding?.weatherDescriptionTextView?.text = weather?.description
                    binding?.weatherCityTextView?.text = it.body()?.name
                    binding?.tempTextView?.text = it.body()?.temperatureData?.temp?.split(".")?.get(0) + "F"
                    binding?.tempFeelTextView?.text = it.body()?.temperatureData?.feelsLike?.split(".")?.get(0) + "F"
                    binding?.tempHumidityTextView?.text = it.body()?.temperatureData?.humidity + "%"
                    val iconBaseUrl = requireContext().getString(R.string.weather_icon_base_urlt)
                    val fullIconUrl = iconBaseUrl + "img/wn/" + weather?.icon + "@2x.png"
                    binding?.weatherIconImageView?.let { weatherIconImage ->
                        //using glide to load the weather icon, Glide does image caching by default
                        Glide.with(requireContext())
                            .load(fullIconUrl)
                            .into(weatherIconImage)
                    }
                }
            } else {
                Toast.makeText(context, it.message(), Toast.LENGTH_SHORT).show()
            }
        }

        binding?.weatherSearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //Given more time,will add listview to show available cities that can be queried
            override fun onQueryTextSubmit(query: String): Boolean {
                query.trim()
                binding?.weatherSearchView?.clearFocus()
                if ( !isNumeric(query) ) {
                    viewModel.getWeather(query)
                }
                else {
                    Toast.makeText(context,"please enter city only",Toast.LENGTH_SHORT).show()
                }
                lastCity = query
                return false
            }

            //Given more time,will add listview to show available cities
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        checkPermission()

    }

    override fun onResume() {
        super.onResume()
        //get the location of user and get the weather of his/her current location
        fetchLocationAndGetWeather()

        //Get the last city from share preference that was last queried
        val city = viewModel.getLastCity()
        //get weather of last queried
        city?.let {
            viewModel.getWeather(city)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
    }

    private fun fetchLocationAndGetWeather() {
        val task : Task<Location> = fusedLocationProviderClient.lastLocation
        checkPermission()
        task.addOnSuccessListener {
            if ( viewModel.getLastCity() == null) {
                viewModel.getWeather(it.latitude, it.longitude)
            }
        }
    }

}