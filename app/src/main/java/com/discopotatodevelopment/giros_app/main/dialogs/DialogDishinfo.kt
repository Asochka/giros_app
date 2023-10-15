package com.discopotatodevelopment.giros_app.main.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.discopotatodevelopment.giros_app.databinding.FragmentDishinfoDialogBinding


class dishinfo_dialog: DialogFragment() {

    private var _binding: FragmentDishinfoDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentDishinfoDialogBinding.inflate(LayoutInflater.from(context))

        val args: dishinfo_dialogArgs by navArgs()
        val info = args.infoClass

        binding.infoName = info?.name_toinfo_cl
        binding.calories = info?.calories_toinfo_cl.toString()
        binding.infoProteins = info?.p_toinfo_cl
        binding.infoFats = info?.f_toinfo_cl
        binding.infoCarbohydrates = info?.c_toinfo_cl
        binding.composition = info?.composition_toinfo_cl

        return AlertDialog.Builder(requireActivity()).setView(binding.root).create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
