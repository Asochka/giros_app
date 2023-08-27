package com.discopotatodevelopment.giros_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.discopotatodevelopment.giros_app.databinding.FragmentHistoryScreenBinding
import java.sql.DriverManager

class FragmentHistory: Fragment() {

    private var _binding: FragmentHistoryScreenBinding? = null
    private val binding get() = _binding!!

    val adapter = AdapterCheck(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

    override fun onResume() {
        super.onResume()
        adapter.clearCheckList()
        getDatafromBase()
    }

    private fun init() {
        binding.checkRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.checkRecyclerView.adapter = adapter
    }

    private fun getDatafromBase() {
        val id_user: String = "1"
        //val jdbcUrl = "jdbc:postgresql://192.168.222.247:5432/ebalovojest"
        val connection = DriverManager.getConnection(jdbcUrl, "giros_app", "291039")

        //изменить запрос, чтоб вытягивало чек по пользователю
        val query = connection.prepareStatement(
            "select check_table.id_check, check_table.date_purch, check_table.sum from check_table, history_purch " +
                    "where check_table.id_check = history_purch.id_check and " +
                    "history_purch.id_user = ${id_user} and check_table.date_purch IS NOT NULL")
        val result = query.executeQuery()

        while (result.next()) {
            binding.info.visibility = View.GONE    // убрать в другое место, тк видна пользователю

            val str1 = result.getInt("id_check")
            val str2 = result.getDate("date_purch")
            val str3 = result.getFloat("sum")
            val check = DataPurchases(str1, str2, str3)
            adapter.addCheck(check)
        }
    }
}