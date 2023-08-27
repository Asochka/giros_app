package com.discopotatodevelopment.giros_app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.discopotatodevelopment.giros_app.databinding.FragmentMainScreenBinding
import java.sql.DriverManager

class FragmentMain: Fragment(R.layout.fragment_main_screen) {

    val CAMERA_REQ_CODE = 1
    var final_amount: Float = 0.0f

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    val adapter = AdapterDishInfo(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        setFragmentResultListener("requestKey_qrScan") { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported.
            val result = bundle.getString("bundleKey_qrScan")
            // Do something with the result.
            insertDataintoBase(result?.toInt() ?: 0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        val binding = FragmentMainScreenBinding.bind(view)
        _binding = binding

        init()

        binding.toPay.setOnClickListener {
            Log.d("tag", "i`m a pay button")

            val str1 = "account_id"
            val str2 = "id"
            val str3 = binding.amount
            val data_to_qr = qrCreateInfo(str1, str2, str3)
            val set_data = FragmentMainDirections.actionMainScreenToPayDialog(data_to_qr)
            findNavController().navigate(set_data)

        }

        binding.qrButton.setOnClickListener {
            val status = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            // работа с разрешением на использование камеры
            if (status == PackageManager.PERMISSION_GRANTED) {
                MAIN.navController.navigate(R.id.action_main_screen_to_qrScan_screen)
                Log.d("tag", "camera was activated")
            } else {
                Log.d("tag", "before per")
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQ_CODE
                )
            }

            val toast = Toast.makeText(context, "после сканирования?", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        final_amount = 0.0f
        adapter.clearList()
        getDatafromBase()
        binding.amount =final_amount.toString()
    }

    private fun init() {
        binding.dishRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.dishRecyclerView.adapter = adapter
    }

    private fun getDatafromBase(): Float {
        val id_user: String = "1"
        //val jdbcUrl = "jdbc:postgresql://192.168.169.247:5432/ebalovojest"
        val connection = DriverManager.getConnection(jdbcUrl, "giros_app", "291039")

        val query = connection.prepareStatement(
            "select dish_name, calories, composition, " +
                    "price_per_100 from history_purch, history_place, dish_name where " +
                    "history_place.id_place = history_purch.id_place and " +
                    "history_place.id_dish = dish_name.id_dish " +
                    "and history_purch.id_user = ${id_user} and history_purch.payment = false")
        val result = query.executeQuery()

        while (result.next()) {
            binding.startInfo.visibility = View.GONE    // убрать в другое место, тк видна пользователю

            val str1 = result.getString("dish_name").toString()
            val str2 = result.getFloat("price_per_100")
            val str3 = 100f
            val str4 = "${result.getFloat("calories").toInt()}"
            val str5 = 1f         //"${result_4.getFloat("calories")}"
            val str6 = 2f         //"${result_4.getFloat("calories")}"
            val str7 = 3f         //"${result_4.getFloat("calories")}"
            val str8 = result.getString("composition")
            val dish = DishInfo(str1, str2, str3, str4, str5, str6, str7, str8)
            adapter.addDish(dish)

            this.final_amount += result.getFloat("price_per_100")
        }
        return final_amount
    }

    private fun insertDataintoBase(id_box_now: Int) {
        val id_user: String = "1"
        var id_check: String = ""

        //val jdbcUrl = "jdbc:postgresql://192.168.222.247:5432/ebalovojest"
        val connection = DriverManager.getConnection(jdbcUrl, "giros_app", "291039")
        val query = connection.prepareStatement("select payment from public.history_purch where " +
                "payment = false and id_user = ${id_user}").executeQuery()

        if(query.fetchSize == 0) {
            query.close()
            val query_1 =
                connection.prepareStatement("INSERT INTO public.check_table default values")
                    .executeUpdate()

            val query_2 = connection.prepareStatement("select max(id_check) from check_table")
            val result_2 = query_2.executeQuery()

            while (result_2.next()) {
                id_check = result_2.getInt("max").toString()
            }
            query_2.close()

            val query_3 = connection.prepareStatement(
                "INSERT INTO public.history_purch(id_purchase, " +
                        "id_check, id_user, id_place, time_open) VALUES " +
                        "((select max(id_purchase) from history_purch)+1, ${id_check}, ${id_user}, " +
                        "(Select id_place from history_place where id_box = ${id_box_now} " +
                        "and data_time_stop is NULL), (select now()))"
            ).executeUpdate()
        }
    }
}