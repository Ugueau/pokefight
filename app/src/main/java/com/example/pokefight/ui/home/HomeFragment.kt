package com.example.pokefight.ui.home

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.R
import com.example.pokefight.databinding.FragmentHomeBinding
import com.example.pokefight.ui.MainViewModel
import com.example.pokefight.ui.fight.PopupWaitingFight
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    val mainViewModel by activityViewModels<MainViewModel>()
    lateinit var vm : MainViewModel
    private lateinit var TextViewTrophy : TextView
    private lateinit var FightButton : Button
    private lateinit var QRCodeImage : ImageView

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted: Boolean ->
            if(isGranted){
                showCamera()
            }
        }

    private val scanLauncher =
        registerForActivityResult(ScanContract()){ result: ScanIntentResult ->
            run {
                if (result.contents == null){
                    Toast.makeText(this.context, "Cancelled", Toast.LENGTH_SHORT).show()
                }else{
                    setResult(result.contents)
                }
            }
        }

    private fun setResult(contents: String) {

        vm.askFight(contents)

        val popupWaitingFight = PopupWaitingFight(contents)

        popupWaitingFight.show((activity as AppCompatActivity).supportFragmentManager, "popupWaitingFight")
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        vm = ViewModelProvider(this).get(MainViewModel::class.java)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TextViewTrophy = binding.TextViewTrophy
        TextViewTrophy.text = mainViewModel.getConnectedUser().Trophy.toString()

        FightButton = view.findViewById<Button>(R.id.FightButton)
        FightButton.setOnClickListener {
            checkCameraPermission(this.context)
        }

        val connectedUser = vm.getConnectedUser()

        val QRCodeWriter = QRCodeWriter()
        try {

            val matrix = QRCodeWriter.encode(connectedUser.UserToken, BarcodeFormat.QR_CODE, 256, 256)
            val width = matrix.width
            val height = matrix.height
            val QRCode = Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565)

            for (x in 0 until width){
                for (y in 0 until height){
                    if (matrix[x, y]){
                        QRCode.setPixel(x, y, Color.BLACK)
                    }
                    else {
                        QRCode.setPixel(x, y, Color.WHITE)
                    }
                }
            }
            QRCodeImage = view.findViewById(R.id.QRCodeImage)
            QRCodeImage.setImageBitmap(QRCode)

        }catch (e: WriterException){
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkCameraPermission(context: Context?){
        if (context != null) {
            if (ContextCompat.checkSelfPermission(context,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                showCamera()
            }
            else if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                Toast.makeText(context, "CAMERA permission Riquiered", Toast.LENGTH_SHORT).show()
            }
            else{
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }
}