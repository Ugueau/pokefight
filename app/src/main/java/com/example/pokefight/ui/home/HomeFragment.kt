package com.example.pokefight.ui.home

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.R
import com.example.pokefight.databinding.FragmentHomeBinding
import com.example.pokefight.tools.QRCodeTool
import com.example.pokefight.ui.MainViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    val mainViewModel by activityViewModels<MainViewModel>()
    lateinit var vm : MainViewModel
    private lateinit var TextViewTrophy : TextView
    private lateinit var FightButton : Button
    private lateinit var QRCodeImage : ImageView

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

        val qrcodeReader = QRCodeTool(this, requireContext())

        FightButton = view.findViewById<Button>(R.id.FightButton)
        FightButton.setOnClickListener {
            qrcodeReader.scan()
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
}