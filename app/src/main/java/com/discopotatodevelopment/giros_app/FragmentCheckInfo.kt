package com.discopotatodevelopment.giros_app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.discopotatodevelopment.giros_app.databinding.FragmentCheckInfoBinding
import java.sql.DriverManager

class FragmentCheckInfo: Fragment() {

    private var _binding: FragmentCheckInfoBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    val adapter = AdapterDishInCheck(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        val binding = FragmentCheckInfoBinding.bind(view)
        _binding = binding

        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        //val jdbcUrl = "jdbc:postgresql://192.168.222.247:5432/ebalovojest"
        val connection = DriverManager.getConnection(jdbcUrl, "giros_app", "291039")
        val id_check = arguments?.getString("requestKey_idCheck")
        Log.d("id_check", "${id_check}")
        //изменить запрос, чтоб вытягивало чек по пользователю
        val query = connection.prepareStatement(
            "select dish_name, calories, composition, " +
                    "price_per_100 from history_purch, history_place, dish_name where " +
                    "history_place.id_place = history_purch.id_place and " +
                    "history_place.id_dish = dish_name.id_dish " +
                    "and history_purch.id_check = ${id_check}")
        val result = query.executeQuery()

        while (result.next()) {
            val str1 = result.getString("dish_name").toString()
            val str2 = result.getFloat("price_per_100")
            val str3 = 100f
            val str4 = "${result.getFloat("calories").toInt()}"
            val str5 = 1f         //"${result_4.getFloat("calories")}"
            val str6 = 2f         //"${result_4.getFloat("calories")}"
            val str7 = 3f         //"${result_4.getFloat("calories")}"
            val str8 = result.getString("composition")
            val dish = DishInfo(str1, str2, str3, str4, str5, str6, str7, str8)
            adapter.addDishInCheck(dish)
        }
    }
}