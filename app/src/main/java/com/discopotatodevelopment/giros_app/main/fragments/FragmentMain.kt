package com.discopotatodevelopment.giros_app.main.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.discopotatodevelopment.giros_app.*
import com.discopotatodevelopment.giros_app.dataCoordinator.DataCoordinator
import com.discopotatodevelopment.giros_app.dataCoordinator.getuserID
import com.discopotatodevelopment.giros_app.databinding.FragmentMainScreenBinding
import com.discopotatodevelopment.giros_app.main.adapters.AdapterDishInfo
import kotlinx.coroutines.launch
import java.sql.DriverManager


class FragmentMain: Fragment(R.layout.fragment_main_screen) {

    val CAMERA_REQ_CODE = 1
    var final_amount: Float = 0.0f

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    val adapter = AdapterDishInfo(this)

    override fun onStart() {
        super.onStart()
        final_amount = 0.0f
        lifecycleScope.launch {
            init()
            adapter.clearList()
            getDatafromBase()}
        binding.amount = final_amount.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as ActivityMain).bottomNav.visibility = View.VISIBLE

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        setFragmentResultListener("requestKey_qrScan") { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported.
            val result = bundle.getString("bundleKey_qrScan")
            // Do something with the result.
            lifecycleScope.launch {insertDataintoBase(result?.toInt() ?: 0)}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMainScreenBinding.bind(view)
        _binding = binding

        binding.toPay.setOnClickListener {
//            val str1 = "account_id"
//            val str2 = "id"
//            val str3 = binding.amount
//            val data_to_qr = qrCreateInfo(str1, str2, str3)
//            val set_data = FragmentMainDirections.actionMainScreenToPayDialog(data_to_qr)
//            findNavController().navigate(set_data)

            val modalbottomSheetFragment = qrpay_dialog()
            modalbottomSheetFragment.show(childFragmentManager, modalbottomSheetFragment.tag)

        }

        binding.qrButton.setOnClickListener {
            val status = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            // работа с разрешением на использование камеры
            if (status == PackageManager.PERMISSION_GRANTED) {
                MAIN.navController.navigate(R.id.action_main_screen_to_qrScan_screen)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQ_CODE
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init() {
        binding.dishRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.dishRecyclerView.adapter = adapter
    }

    private suspend fun getDatafromBase(): Float {
        val id_user = DataCoordinator.shared.getuserID()
        val connection = DriverManager.getConnection(jdbcUrl, user, password)

        // вытягиваем всю инфу о том, что в отсканированном боксе
        val query = connection.prepareStatement(
            "select dish_name, calories, composition, mass_prev, mass_hbc, proteins, fats, " +
                    "price_per_100, carbohydrates, photo_url from history_purch, history_place, dish_name where " +
                    "history_place.id_place = history_purch.id_place and " +
                    "history_place.id_dish = dish_name.id_dish " +
                    "and history_purch.id_user = ${id_user} and history_purch.time_close IS NULL")
        val result = query.executeQuery()

        while (result.next()) {
            binding.startInfo.visibility = View.GONE    // убрать в другое место, тк видна пользователю

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
            adapter.addDish(dish)

            this.final_amount += result.getFloat("price_per_100")
        }
        return final_amount
    }

    private suspend fun insertDataintoBase(id_box_now: Int) {
        val id_user = DataCoordinator.shared.getuserID()
        var id_check: String = ""
        val connection = DriverManager.getConnection(jdbcUrl, user, password)
        val query = connection.prepareStatement("select time_close from public.history_purch where " +
                "time_close IS NULL and id_user = ${id_user}").executeQuery()

        if(query.fetchSize == 0) {
            query.close()
            connection.prepareStatement("INSERT INTO public.check_table default values").executeUpdate()

            val query_2 = connection.prepareStatement("select max(id_check) from check_table")
            val result_2 = query_2.executeQuery()

            while (result_2.next()) {
                id_check = result_2.getInt("max").toString()
            }
            query_2.close()

            connection.prepareStatement("INSERT INTO public.history_purch(id_purchase, " +
                        "id_check, id_user, id_place, time_open) VALUES " +
                        "((select max(id_purchase) from history_purch)+1, ${id_check}, ${id_user}, " +
                        "(Select id_place from history_place where id_box = ${id_box_now} " +
                        "and data_time_stop is NULL), (select now()))"
            ).executeUpdate()
        }
    }
}