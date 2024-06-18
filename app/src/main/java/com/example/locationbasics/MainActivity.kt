package com.example.locationbasics

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.locationbasics.ui.theme.LocationBasicsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: LocationViewModel = viewModel()
            LocationBasicsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(viewModel)
                }
            }
        }
    }

@Composable
fun MyApp(viewModel: LocationViewModel){
    // Retrieves the current context.
    val context = LocalContext.current
    // Creates an instance of LocationUtils to handle location-related operations.
    val locationUtils = LocationUtils(context)
    // Calls the LocationDisplay composable function to display location-related UI components.
    LocationDisplay(locationUtils = locationUtils, viewModel, context = context )

}}

// Composable function that displays location-related UI components.
@Composable
fun LocationDisplay(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    context: Context
){

    val location = viewModel.location.value

    val address = location?.let {
        locationUtils.reverseGeocodeLocation(location)
    }

    // Creates a launcher for requesting location permissions.
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        // Checks if both ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions are granted.
        contract = ActivityResultContracts.RequestMultiplePermissions() , onResult ={ permissions ->
            if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ){
                // i have access to location
                // Displays a message indicating that location is accessible.
                // (In actual app development, you would update location here.)
                locationUtils.requestLocationUpdates(viewModel = viewModel)
            }else{
                // Displays a message asking the user to grant location permissions.
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                // Checks if a rationale (explanation) for permissions is required.
                if(rationaleRequired){
                    // Shows a toast message explaining why location permission is required.
                    Toast.makeText(context,
                        "Location Permission is required for this feature to work", Toast.LENGTH_LONG)
                        .show()
                }else{
                    Toast.makeText(context,
                        "Location Permission is required. Please Enable it in android Settings", Toast.LENGTH_LONG)
                        .show()
                }
            }
        } )

    // Column layout that centers its children vertically and horizontally.
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        // Text displayed when location is not available.
    ) {

        if(location != null){
            Text(text = "Address: ${location.latitude} ${location.longitude} \n $address")
        }else{
            Text(text = "Location not available")
        }


        Button(onClick = {
            if(locationUtils.hasLocationPermission(context)){
                //permission already granted and want to update the location
                locationUtils.requestLocationUpdates(viewModel)
            }else{
                // request permission of location
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }) {
            Text(text = "Get Location")
        }
    }
}