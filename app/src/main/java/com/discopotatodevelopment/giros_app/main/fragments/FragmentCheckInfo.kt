package com.discopotatodevelopment.giros_app.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.discopotatodevelopment.giros_app.*
import com.discopotatodevelopment.giros_app.databinding.FragmentCheckInfoBinding
import com.discopotatodevelopment.giros_app.main.adapters.AdapterDishInCheck
import java.sql.DriverManager

class FragmentCheckInfo: Fragment() {

    private var _binding: FragmentCheckInfoBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    val adapter = AdapterDishInCheck(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_check_info, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as ActivityMain).bottomNav.visibility = View.GONE

        val id_check = arguments?.getString("requestKey_idCheck")
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Чек № $id_check"

        view.findViewById<Toolbar>(R.id.toolbar).apply {
            setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)

            setNavigationOnClickListener {
                findNavController().navigate(R.id.action_fragmentCheckInfo_to_history_screen, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.history_screen, true)
                        .build())
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(requireActivity() as AppCompatActivity).supportActionBar?.show()

        val binding = FragmentCheckInfoBinding.bind(view)
        _binding = binding

        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as ActivityMain).bottomNav.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        adapter.clearDishInCheckList()
        getDatafromBase()
    }

    private fun init() {
        binding.dishInCheckRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.dishInCheckRecyclerView.adapter = adapter
    }

    private fun getDatafromBase() {
        val connection = DriverManager.getConnection(jdbcUrl, user, password)
        val id_check = arguments?.getString("requestKey_idCheck")
        //изменить запрос, чтоб вытягивало чек по пользователю
        val query = connection.prepareStatement(
            "select dish_name, calories, composition, mass_prev, mass_hbc,proteins, fats, " +
                "price_per_100, carbohydrates, photo_url from history_purch, history_place, dish_name where " +
                "history_place.id_place = history_purch.id_place and " +
                "history_place.id_dish = dish_name.id_dish and history_purch.id_check = ${id_check}")
        val result = query.executeQuery()

        while (result.next()) {
            val str1 = result.getString("dish_name").toString()
            val str2 = result.getFloat("price_per_100")
            val str3 = result.getFloat("mass_prev") - result.getFloat("mass_hbc")
            val str4 = result.getFloat("calories")
            val str5 = result.getFloat("proteins")
            val str6 = result.getFloat("fats")
            val str7 = result.getFloat("carbohydrates")
            val str8 = result.getString("composition")
            val str9 = result.getString("photo_url")
            val dish = DishInfo(str1, str2, str3, str4, str5, str6, str7, str8, str9)
            adapter.addDishInCheck(dish)
        }
    }
}