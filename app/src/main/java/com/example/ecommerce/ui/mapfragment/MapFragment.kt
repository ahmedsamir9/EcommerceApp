package com.example.ecommerce.ui.mapfragment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ecommerce.R
import com.example.ecommerce.databinding.MapFragmentBinding
import com.example.ecommerce.utils.CONSTANTS
import com.example.ecommerce.utils.CONSTANTS.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import com.example.ecommerce.utils.UiDisapperAndAppearInActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private val viewModel: MapViewModel by viewModels()
    private lateinit var mapFragmentBinding:MapFragmentBinding
    private lateinit var googleMap: GoogleMap
    private var marker: Marker? =null
    private val latitude = -34.0
    private val langtude = 151.0
    private lateinit var uiDisapperAndAppearInActivity: UiDisapperAndAppearInActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiDisapperAndAppearInActivity =context as UiDisapperAndAppearInActivity
        uiDisapperAndAppearInActivity.showToolBar()
        uiDisapperAndAppearInActivity.hideNav()
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Dropping Location "
    }
private val args:MapFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapFragmentBinding =DataBindingUtil.inflate(
            inflater,
            R.layout.map_fragment,
            container,
            false
        )
        mapFragmentBinding.mapView.onCreate(savedInstanceState)
        mapFragmentBinding.mapView.getMapAsync(this)
        mapFragmentBinding.checkOut.setOnClickListener {
            val map = mutableMapOf<String,Any>()
            map["addressOfCustomerLongitude"]=langtude
            map["addressOfCustomerLatitude"]=latitude
            map["orderState"]=CONSTANTS.OREDER_STATE_COMPLETE
            val orderId =args.orderId
            viewModel.checkOutorder(orderId,map)
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToNavigationHome())
        }
        return mapFragmentBinding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap =map
        var sydney = LatLng(latitude, langtude)
        val mark = MarkerOptions().position(sydney).title("Droping Place").icon(
            BitmapDescriptorFactory.defaultMarker()
        )
        googleMap.addMarker(mark)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f))
    }



 override fun onStart() {
        super.onStart()
     if (!isLocationAllowed()){
         //request permission
         requestPermission()
     }
     mapFragmentBinding.mapView.onStart()
    }

 override fun onResume() {
        super.onResume()
     mapFragmentBinding.mapView.onResume()
    }

override fun onPause() {
        super.onPause()
    mapFragmentBinding.mapView.onPause()
    }

 override fun onStop() {
        super.onStop()
     mapFragmentBinding.mapView.onStop()
    }

 override fun onDestroy() {
        super.onDestroy()
     mapFragmentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapFragmentBinding.mapView.onLowMemory()
    }

    private fun isLocationAllowed():Boolean{
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }
    private fun requestPermission(){
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )) {
                Toast.makeText(context, "gps is dined", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    CONSTANTS.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )

            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(context, "gps is allowed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "gps is dined", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }
}