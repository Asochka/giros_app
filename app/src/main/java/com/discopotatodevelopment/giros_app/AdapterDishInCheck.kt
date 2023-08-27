package com.discopotatodevelopment.giros_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.discopotatodevelopment.giros_app.databinding.ItemDishBinding

class AdapterDishInCheck(contextCheck: FragmentCheckInfo): RecyclerView.Adapter<AdapterDishInCheck.DishInCheckHolder>() {

    val dishInCheckList = ArrayList<DishInfo>()
    val context = contextCheck

    class DishInCheckHolder(item: View, contextView: FragmentCheckInfo): RecyclerView.ViewHolder(item) {
        val binding = ItemDishBinding.bind(item)
        val context = contextView

        fun bind(dishInCheck: DishInfo) = with(binding) {
            foodName = dishInCheck.name_cl
            foodPrice = (dishInCheck.price_tomain_cl * dishInCheck.mass_tomain_cl / 100)
            massOfDish = dishInCheck.mass_tomain_cl.toString()

            dishInfo.setOnClickListener {
                val str1 = dishInCheck.name_cl
                val str2 = dishInCheck.calories_toinfo_cl       //calories_info_cl
                val str3 = dishInCheck.p_toinfo_cl.toString()       //p_info_cl
                val str4 = dishInCheck.f_toinfo_cl.toString()       //f_info_cl
                val str5 = dishInCheck.c_toinfo_cl.toString()       //c_info_cl
                val str6 = dishInCheck.composition_toinfo_cl       //composition_info_cl

                val data_to_info = DishInfoToInfo(str1, str2, str3, str4, str5, str6)
                val set_info = FragmentCheckInfoDirections.actionFragmentCheckInfoToDishinfoDialog(data_to_info)
                //FragmentCheckInfoDirections.actionFragmentCheckInfoToDishinfoDialog(data_to_info)
                MAIN.navController.navigate(set_info)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishInCheckHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dish, parent, false)
        return DishInCheckHolder(view, context)
    }

    override fun onBindViewHolder(holder: DishInCheckHolder, position: Int) {
        holder.bind(dishInCheckList[position])
    }

    override fun getItemCount(): Int {
        return  dishInCheckList.size
    }

    fun addDishInCheck(dishInCheck: DishInfo) {
        dishInCheckList.add(dishInCheck)
        notifyDataSetChanged()
    }

    fun clearDishInCheckList() {
        dishInCheckList.clear()
    }
}