package com.discopotatodevelopment.giros_app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DishInfo(
    val name_cl: String,
    val price_tomain_cl: Float,
    val mass_tomain_cl: Float,
    val calories_toinfo_cl: String,
    val p_toinfo_cl: Float,
    val f_toinfo_cl: Float,
    val c_toinfo_cl: Float,
    val composition_toinfo_cl: String): Parcelable
