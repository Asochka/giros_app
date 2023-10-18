package com.discopotatodevelopment.giros_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class qrpay_dialog: BottomSheetDialogFragment(R.layout.fragment_qrpay_bottom_sheet), ZBarScannerView.ResultHandler {

    var zbView: ZBarScannerView? = null
    companion object { const val TAG = "FragmentQrpayBottomSheet" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        zbView = ZBarScannerView(activity)
        (activity as ActivityMain).bottomNav.visibility = View.GONE
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
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("$result"))
        startActivity(browserIntent)

        Log.d("tag", "qr result: ${p0?.contents}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as ActivityMain).bottomNav.visibility = View.VISIBLE
        dismiss()
    }
}