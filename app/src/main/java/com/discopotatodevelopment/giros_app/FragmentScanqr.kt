package com.discopotatodevelopment.giros_app

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class FragmentScanqr : Fragment(R.layout.fragment_qr_scan_screen), ZBarScannerView.ResultHandler {
    var zbView: ZBarScannerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        zbView = ZBarScannerView(activity)
        return zbView
    }

    override fun onResume() {
        super.onResume()
        zbView?.setResultHandler(this)
        zbView?.startCamera()
    }

    override fun onPause() {
        super.onPause()
        zbView?.stopCamera()
    }

    override fun handleResult(p0: Result?) {
        val result = p0?.contents
        // Use the Kotlin extension in the fragment-ktx artifact.
        setFragmentResult("requestKey_qrScan", bundleOf("bundleKey_qrScan" to result))

        Log.d("tag", "qr result: ${p0?.contents}")
        findNavController().navigate(R.id.action_qrScan_screen_to_main_screen)

        val toast = Toast.makeText(context, "QR просканен: ${p0?.contents}", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}