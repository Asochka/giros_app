package com.discopotatodevelopment.giros_app.account.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.discopotatodevelopment.giros_app.ActivityMain
import com.discopotatodevelopment.giros_app.R
import com.discopotatodevelopment.giros_app.databinding.FragmentAskQueBinding

class FragmentAskQuest: Fragment() {
    private var _binding: FragmentAskQueBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        val items = listOf("Персональные данные", "Ошибки", "Предложения", "Другое")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_supp, items)
        (binding.spinnerMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ask_que, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as ActivityMain).bottomNav.visibility = View.GONE
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Свяжитесь с нами"

        view.findViewById<Toolbar>(R.id.toolbar).apply {
            setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)

            setNavigationOnClickListener {
                findNavController().navigate(R.id.action_fragmentAskQuest_to_account_screen, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.account_screen, true)
                        .build())
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAskQueBinding.bind(view)
        _binding = binding

        binding.apply {
            //buttonSend.isEnabled = false
            buttonSend.alpha = 0.5F

            editTextSupp1.addTextChangedListener {
                if (editTextSupp1.text.isNotEmpty()) {
                    //buttonSend.isEnabled = true
                    buttonSend.alpha = 1F
                } else {
                    //buttonSend.isEnabled = false
                    buttonSend.alpha = 0.5F
                }
            }
        }
        binding.buttonSend.setOnClickListener {
            val selected_subject = binding.spinner.editableText.toString()
            val body = binding.editTextSupp1.text.toString()

            val intent = Intent(Intent.ACTION_SEND)
            val emailAddress = arrayOf("discopotato.development@yandex.ru")
            intent.putExtra(Intent.EXTRA_EMAIL, emailAddress)
            intent.putExtra(Intent.EXTRA_SUBJECT, selected_subject)
            intent.putExtra(Intent.EXTRA_TEXT, body)

            intent.type = "message/rfc822"

            try {
                startActivity(Intent.createChooser(intent, "Choose Email Provider."))
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "No Email Provider", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as ActivityMain).bottomNav.visibility = View.VISIBLE
    }
}