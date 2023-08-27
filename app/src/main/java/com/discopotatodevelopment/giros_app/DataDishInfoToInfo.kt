package com.discopotatodevelopment.giros_app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DishInfoToInfo(
    val name_toinfo_cl: String? = null,
    val calories_toinfo_cl: String? = null,
    val p_toinfo_cl: String? = null,
    val f_toinfo_cl: String? = null,
    val c_toinfo_cl: String? = null,
    val composition_toinfo_cl: String? = null): Parcelable
