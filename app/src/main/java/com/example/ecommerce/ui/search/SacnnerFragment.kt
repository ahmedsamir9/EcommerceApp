package com.example.ecommerce


import android.Manifest
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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.*
import com.example.ecommerce.utils.CONSTANTS


class ScannerFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var scannerView:CodeScannerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sacnner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCamera(view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Qr Scanner"
    }

    override fun onPause() {
        if (isCameraPermissionGranted()) {codeScanner.releaseResources()}
        super.onPause()
    }
    private fun setupCamera(view: View){
       scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        checkCameraPermission()
        scannerView.setOnClickListener {
            if (isCameraPermissionGranted()){
                setupScanner()
            }
        }

    }
    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun checkCameraPermission(){
        if (!isCameraPermissionGranted()) {
           ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA),CONSTANTS.REQUEST_CAMERA_PERMISSION)
        }else
        {
            setupScanner()
        }
    }
    private fun setupScanner(){
        val activity =requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                codeScanner.stopPreview()
              val action =ScannerFragmentDirections.actionScannerFragmentToSearchFragment(it.text)
                findNavController().navigate(action)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            requireActivity().runOnUiThread {
                Toast.makeText(
                        requireContext(), "Camera initialization error: ${it.message}",
                        Toast.LENGTH_LONG
                ).show()
            }
        }
        codeScanner.startPreview()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONSTANTS.REQUEST_CAMERA_PERMISSION){
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                setupScanner()
            }
        }
    }
}