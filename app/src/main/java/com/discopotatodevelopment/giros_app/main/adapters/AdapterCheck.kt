package com.discopotatodevelopment.giros_app.main.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.discopotatodevelopment.giros_app.DataPurchases
import com.discopotatodevelopment.giros_app.MAIN
import com.discopotatodevelopment.giros_app.R
import com.discopotatodevelopment.giros_app.databinding.ItemCheckBinding
import com.discopotatodevelopment.giros_app.main.fragments.FragmentHistory
import java.text.DecimalFormat
import java.util.*
import kotlin.math.roundToInt

class AdapterCheck(contextHistory: FragmentHistory): RecyclerView.Adapter<AdapterCheck.CheckHolder>() {

    val checkList = ArrayList<DataPurchases>()
    val context = contextHistory

    class CheckHolder(item: View, contextView: FragmentHistory): RecyclerView.ViewHolder(item) {
        val binding = ItemCheckBinding.bind(item)
        val context = contextView

        fun bind(check: DataPurchases) = with(binding) {
            val days = arrayOf("Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
            val cal = Calendar.getInstance()
            cal.time = check.date_purch_check
            Log.d("check_day", "${days[cal[Calendar.DAY_OF_WEEK]-1]}")
            Log.d("check_day", "${cal[Calendar.DAY_OF_WEEK]}")
            datePurch = "${days[cal[Calendar.DAY_OF_WEEK]-1]}, ${cal[Calendar.DAY_OF_MONTH]}." +
                    "${cal[Calendar.MONTH]+1}"
            timePurch = "в ${cal[Calendar.HOUR_OF_DAY]}:${cal[Calendar.MINUTE]}"

            calories = check.calories_check.roundToInt().toString() + " kkal"
            sum = DecimalFormat("#.##").format(check.total_amount_check)

            val list_bind: Array<TextView> by lazy {
                arrayOf(binding.firstDishInCheck, binding.secondDishInCheck, binding.thirdDishInCheck)
            }

            val parse_dishes = check.dishes_in_check.split("!", limit = 3)

            Log.d("check", parse_dishes.toString())
            Log.d("check", parse_dishes.lastIndex.toString())

            for (i in parse_dishes.indices) {
                list_bind[i].text = "${i+1}. ${parse_dishes[i].removeSuffix("!")}"
                if (parse_dishes.size < list_bind.size) {
                    list_bind[i+1].visibility = View.GONE
                }
            }

            val bundle = Bundle()
            dishCardCheck.setOnClickListener {
                bundle.putString("requestKey_idCheck", check.id_check.toString())
                //findNavController().navigate(R.id.action_history_screen_to_fragmentCheckInfo, bundle)
                MAIN.navController.navigate(R.id.action_history_screen_to_fragmentCheckInfo, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_check, parent, false)
        return CheckHolder(view, context)
    }

    override fun onBindViewHolder(holder: CheckHolder, position: Int) {
        holder.bind(checkList[position])
    }

    override fun getItemCount(): Int {
        return checkList.size
    }

    fun addCheck(check: DataPurchases) {
        checkList.add(check)
        notifyDataSetChanged()
    }

    fun clearCheckList() {
        checkList.clear()
    }
}