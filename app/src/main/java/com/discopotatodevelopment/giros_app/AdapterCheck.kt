package com.discopotatodevelopment.giros_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.discopotatodevelopment.giros_app.databinding.ItemCheckBinding

class AdapterCheck(contextHistory: FragmentHistory): RecyclerView.Adapter<AdapterCheck.CheckHolder>() {

    val checkList = ArrayList<DataPurchases>()
    val context = contextHistory

    class CheckHolder(item: View, contextView: FragmentHistory): RecyclerView.ViewHolder(item) {
        val binding = ItemCheckBinding.bind(item)
        val context = contextView

        fun bind(check: DataPurchases) = with(binding) {
            sum = check.total_amount.toString()
            datePurch = check.date_purch.toString()

            val bundle = Bundle()

            checkInfo.setOnClickListener {
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