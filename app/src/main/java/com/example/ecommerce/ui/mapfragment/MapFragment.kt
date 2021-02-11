package com.example.ecommerce.ui.mapfragment

import android.Manifest
import android.annotation.SuppressLint
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
import com.example.ecommerce.utils.CONSTANTS.REQUEST_LOCATION_PERMISSION
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
    private lateinit var mapFragmentBinding: MapFragmentBinding
    private lateinit var googleMap: GoogleMap
    private var marker: Marker? = null
    private var latitude = 0.0
    private var langtude = 0.0
    private lateinit var uiDisapperAndAppearInActivity: UiDisapperAndAppearInActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiDisapperAndAppearInActivity = context as UiDisapperAndAppearInActivity
        uiDisapperAndAppearInActivity.showToolBar()
        uiDisapperAndAppearInActivity.hideNav()
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Dropping Location "
    }

    private val args: MapFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.map_fragment,
            container,
            false
        )
        mapFragmentBinding.mapView.onCreate(savedInstanceState)
        mapFragmentBinding.mapView.getMapAsync(this)
        mapFragmentBinding.checkOut.setOnClickListener {
            val map = mutableMapOf<String, Any>()
            map["addressOfCustomerLongitude"] = langtude
            map["addressOfCustomerLatitude"] = latitude
            map["orderState"] = CONSTANTS.OREDER_STATE_COMPLETE
            val orderId = args.orderId
            viewModel.checkOutorder(orderId, map)
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToNavigationHome())
        }
        return mapFragmentBinding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setOnMapLongClickListener {
            if (marker == null){
                val mark = MarkerOptions().position(it).title("Droping Place").icon(
                    BitmapDescriptorFactory.defaultMarker()
                )
                marker =googleMap.addMarker(mark)
            }
            else
            {
                marker!!.position = it
            }
            langtude =it.longitude
            latitude= it.latitude
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 15.0f))
        }
        enableMyLocation()
    }


    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            googleMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                googleMap.isMyLocationEnabled = true
            }
        }

    }
    override fun onStart() {
        super.onStart()

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


}