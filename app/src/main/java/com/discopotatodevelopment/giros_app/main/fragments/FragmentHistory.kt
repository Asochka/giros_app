package com.discopotatodevelopment.giros_app.main.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.discopotatodevelopment.giros_app.*
import com.discopotatodevelopment.giros_app.dataCoordinator.DataCoordinator
import com.discopotatodevelopment.giros_app.dataCoordinator.getuserID
import com.discopotatodevelopment.giros_app.databinding.FragmentHistoryScreenBinding
import com.discopotatodevelopment.giros_app.main.adapters.AdapterCheck
import kotlinx.coroutines.launch
import java.sql.DriverManager
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class FragmentHistory: Fragment() {

    private var _binding: FragmentHistoryScreenBinding? = null
    private val binding get() = _binding!!

    var flag = false

    val adapter = AdapterCheck(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        val binding = FragmentHistoryScreenBinding.bind(view)
        _binding = binding

        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()

        val executor: Executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            try {
                binding.loadBar.visibility = View.VISIBLE
                adapter.clearCheckList()
                lifecycleScope.launch {flag = getDatafromBaseCheck()}

                handler.post {
                    binding.loadBar.visibility = View.GONE

                    if (!flag) {
                        binding.info.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post {} // update error to UI thread or handle }
            }
        }
    }

    private fun init() {
        binding.checkRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.checkRecyclerView.adapter = adapter
    }

    private suspend fun getDatafromBaseCheck(): Boolean {
        var flag_ = false
        val id_user = DataCoordinator.shared.getuserID()
        val connection = DriverManager.getConnection(jdbcUrl, user, password)

        val query1 = connection.prepareStatement(
            "select check_table.id_check, check_table.date_purch, check_table.payment, " +
                    "check_table.sum_purch from check_table " +
                    "where check_table.id_user = ${id_user} and check_table.payment = true")
        val result1 = query1.executeQuery()


        while (result1.next()) {
            flag_ = true

            val str1 = result1.getInt("id_check")
            val str2 = result1.getTimestamp("date_purch")
            val str3 = result1.getFloat("sum_purch")

            val query2 = connection.prepareStatement(
                "Select check_table.id_check, calories, mass_prev, mass_hbc, dish_name " +
                    "FROM check_table " +
                    "inner join history_purch on check_table.id_check = history_purch.id_check " +
                    "inner join history_place on history_purch.id_place = history_place.id_place " +
                    "inner join dish_name on history_place.id_dish = dish_name.id_dish " +
                    "where check_table.id_user = ${id_user} and check_table.payment = true and " +
                    "check_table.id_check = ${str1}"
            )
            val result2 = query2.executeQuery()

            var sum_cal = 0F
            var all_dishes = ""
            while (result2.next()) {
                val mass = result2.getFloat("mass_prev") - result2.getFloat("mass_hbc")
                sum_cal += mass / 100 * result2.getFloat("calories")
                val dish = result2.getString("dish_name")
                all_dishes += "${dish}!"
            }

            val str4 = sum_cal
            val str5 = all_dishes

            val check = DataPurchases(str1, str2, str3, str4, str5)
            adapter.addCheck(check)
        }
        return flag_
    }
}