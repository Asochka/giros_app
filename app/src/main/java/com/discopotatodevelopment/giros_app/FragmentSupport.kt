package com.discopotatodevelopment.giros_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.discopotatodevelopment.giros_app.databinding.FragmentSupportScreenBinding

class FragmentSupport: Fragment() {

    private var _binding: FragmentSupportScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSupportScreenBinding.bind(view)
        _binding = binding

        binding.apply {
            buttonSend.isEnabled = false
            buttonSend.alpha = 0.5F

            editTextSupp1.addTextChangedListener {
                if(editTextSupp1.text.isNotEmpty()) {
                    buttonSend.isEnabled = true
                    buttonSend.alpha = 1F
                } else {
                    buttonSend.isEnabled = false
                    buttonSend.alpha = 0.5F
                }
            }
        }
    }
}