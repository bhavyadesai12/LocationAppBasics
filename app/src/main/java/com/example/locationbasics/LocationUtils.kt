package com.example.locationbasics

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale


/**
 * Utility class to check location permissions in Android.
 * @param context The context in which this utility class operates.
 */

class LocationUtils(val context: Context) {

    private val _fusedLocationClient: FusedLocationProviderClient
    // This creates a client that helps the app find where you are using GPS.
    = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(viewModel: LocationViewModel){
        // This callback is triggered when the app receives updated location information.
        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // If the app receives a new location update, it extracts latitude and longitude.
                locationResult.lastLocation?.let {
                    // Creates a new LocationData object with latitude and longitude.
                    val location = LocationData(latitude = it.latitude, longitude = it.longitude)
                    // Updates the ViewModel with the new location data.
                    viewModel.updateLocation(location)
                }
            }
        }
// Defines parameters for the location request, emphasizing high accuracy and updates every 1000ms.
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,1000).build()
        _fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
    }


    // Check if ACCESS_FINE_LOCATION permission is granted
    fun hasLocationPermission(context: Context): Boolean {

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                // Check if ACCESS_COARSE_LOCATION permission is granted
                &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun reverseGeocodeLocation(location: LocationData) : String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val coordinate = LatLng(location.latitude, location.longitude)
        val addresses:MutableList<Address>? =
            geocoder.getFromLocation(coordinate.latitude, coordinate.longitude, 1)
        return if(addresses?.isNotEmpty()==true){
            addresses[0].getAddressLine(0)
        }else{
            "Address not found"
        }
    }
}