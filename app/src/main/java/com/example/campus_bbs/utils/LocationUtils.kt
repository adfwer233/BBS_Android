package com.example.campus_bbs.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.provider.Telephony.Mms.Addr
import android.util.Log
import android.webkit.PermissionRequest
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class LocationUtils {
    fun getLocation(context: Context): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var location: Location? = null



        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null
        }
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//        try {
//            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
////
//            if (location == null) {
//                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//            }
//        } catch (e: SecurityException) {
//            Log.e(e.toString(), "Location Utils, getLocation has no permission")
//        }

        return location
    }

    fun getGeoFromLocation(context: Context, location: Location?): List<Address> {
        if (location == null)
            return listOf()

        val geocoder = Geocoder(context)
        var addressList: List<Address> = listOf()
        try {
            addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1) as List<Address>

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressList
    }
}