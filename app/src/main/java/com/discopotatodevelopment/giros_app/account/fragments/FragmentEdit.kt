package com.discopotatodevelopment.giros_app.account.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.discopotatodevelopment.giros_app.ActivityMain
import com.discopotatodevelopment.giros_app.R

class FragmentEdit : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_edit, container, false)

        val view = inflater.inflate(R.layout.fragment_edit, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as ActivityMain).bottomNav.visibility = View.GONE

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Edit profile"

        view.findViewById<Toolbar>(R.id.toolbar).apply {
            setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)

            setNavigationOnClickListener {
                findNavController().navigate(R.id.action_fragmentEdit_to_account_screen, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.account_screen, true)
                        .build())
            }
        }

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        (activity as ActivityMain).bottomNav.visibility = View.VISIBLE
    }
}