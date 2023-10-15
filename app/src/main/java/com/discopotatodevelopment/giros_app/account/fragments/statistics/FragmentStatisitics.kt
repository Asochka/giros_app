package com.discopotatodevelopment.giros_app.account.fragments.statistics

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
import com.discopotatodevelopment.giros_app.databinding.FragmentStatisiticsBinding

class FragmentStatisitics : Fragment() {

    private var _binding: FragmentStatisiticsBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        binding.analyticalPieChart1.setDataChart(
            listOf(Pair(4, "Protein"), Pair(6, "Fats"), Pair(6, "Carbohydrates")))
        binding.analyticalPieChart1.startAnimation()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_statisitics, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as ActivityMain).bottomNav.visibility = View.GONE

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Ur stats"

        view.findViewById<Toolbar>(R.id.toolbar).apply {
            setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)

            setNavigationOnClickListener {
                findNavController().navigate(R.id.action_fragmentStatisitics_to_account_screen, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.account_screen, true)
                        .build())
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentStatisiticsBinding.bind(view)
        _binding = binding
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as ActivityMain).bottomNav.visibility = View.VISIBLE
    }
}