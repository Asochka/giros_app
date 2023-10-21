package com.discopotatodevelopment.giros_app.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.discopotatodevelopment.giros_app.DishInfo
import com.discopotatodevelopment.giros_app.DishInfoToInfo
import com.discopotatodevelopment.giros_app.MAIN
import com.discopotatodevelopment.giros_app.R
import com.discopotatodevelopment.giros_app.databinding.ItemDishBinding
import com.discopotatodevelopment.giros_app.main.fragments.FragmentMain
import com.discopotatodevelopment.giros_app.main.fragments.FragmentMainDirections
import java.text.DecimalFormat
import kotlin.math.roundToInt

class AdapterDishInfo(contextMain: FragmentMain): RecyclerView.Adapter<AdapterDishInfo.DishInfoHolder>() {

    val dishList = ArrayList<DishInfo>()
    val context = contextMain

    class DishInfoHolder(item: View, contextView: FragmentMain): RecyclerView.ViewHolder(item) {
        val binding = ItemDishBinding.bind(item)
        val context = contextView

        fun bind(dish: DishInfo) = with(binding) {
            foodName = dish.name_cl

            foodPrice = DecimalFormat("#.##").format((dish.price_tomain_cl * dish.mass_tomain_cl / 100))
            massOfDish = dish.mass_tomain_cl.roundToInt().toString() + " грамм"

            binding.loadBar.visibility = View.VISIBLE
            binding.dishPhoto.load(dish.photo_url) {
                listener { _, _ ->
                    binding.loadBar.visibility = View.GONE
                }
            }



            dishInfo.setOnClickListener {
                val str1 = dish.name_cl
                val str2 = dish.calories_toinfo_cl.toInt().toString()       //calories_info_cl
                val str3 = dish.p_toinfo_cl.toInt().toString()       //p_info_cl
                val str4 = dish.f_toinfo_cl.toInt().toString()       //f_info_cl
                val str5 = dish.c_toinfo_cl.toInt().toString()       //c_info_cl
                val str6 = dish.composition_toinfo_cl       //composition_info_cl

                val data_to_info = DishInfoToInfo(str1, str2, str3, str4, str5, str6)
                val set_info = FragmentMainDirections.actionMainScreenToDishinfoDialog(data_to_info)
                MAIN.navController.navigate(set_info)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishInfoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dish, parent, false)
        return DishInfoHolder(view, context)
    }

    override fun onBindViewHolder(holder: DishInfoHolder, position: Int) {
        holder.bind(dishList[position])
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    fun addDish(dish: DishInfo) {
        dishList.add(dish)
        notifyDataSetChanged()
    }

    fun clearList() {
        dishList.clear()
    }
}