package com.example.pokefight.tools

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class QRCodeTool(private val fragment : Fragment, private val context: Context) {
    private val requestPermissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted: Boolean ->
            if(isGranted){
                showCamera()
            }
        }

    private val scanLauncher =
        fragment.registerForActivityResult(ScanContract()){ result: ScanIntentResult ->
            run {
                if (result.contents == null){
                    Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                }else{
                    setResult(result.contents)
                }
            }
        }

    val result = MutableLiveData<String>()

    private fun setResult(contents: String) {
        result.postValue(contents)
    }

    private fun showCamera() {
        val option = ScanOptions()
        option.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        option.setPrompt("Scan QR code")
        option.setCameraId(0)
        option.setBeepEnabled(false)
        option.setBarcodeImageEnabled(true)
        option.setOrientationLocked(false)

        scanLauncher.launch(option)

    }
    private fun checkCameraPermission(){
        if (context != null) {
            if (ContextCompat.checkSelfPermission(context,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                showCamera()
            }
            else if(fragment.shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                Toast.makeText(context, "CAMERA permission Required", Toast.LENGTH_SHORT).show()
            }
            else{
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    fun scan() {
        checkCameraPermission()
    }
}