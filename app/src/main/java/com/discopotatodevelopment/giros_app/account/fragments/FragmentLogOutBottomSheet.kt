package com.discopotatodevelopment.giros_app.account.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.discopotatodevelopment.giros_app.R
import com.discopotatodevelopment.giros_app.dataCoordinator.DataCoordinator
import com.discopotatodevelopment.giros_app.dataCoordinator.updateisLogin
import com.discopotatodevelopment.giros_app.dataCoordinator.updateisPolicyAccepted
import com.discopotatodevelopment.giros_app.dataCoordinator.updateuserID
import com.discopotatodevelopment.giros_app.databinding.FragmentLogOutBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentLogOutBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentLogOutBottomSheetBinding? = null
    private val binding get() = _binding!!

    companion object { const val TAG = "FragmentLogOutBottomSheet" }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_log_out_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLogOutBottomSheetBinding.bind(view)
        _binding = binding

        binding.buttonYes.setOnClickListener {
            DataCoordinator.shared.updateuserID(0)
            DataCoordinator.shared.updateisLogin(false)
            DataCoordinator.shared.updateisPolicyAccepted(false)
            dismiss()
            findNavController().navigate(R.id.action_account_screen_to_fragmentFirstEntrance)
        }

        binding.buttonNo.setOnClickListener {
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
