package com.discopotatodevelopment.giros_app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class qrCreateInfo(
    val account_id_cl: String? = null,
    val id_cl: String? = null,
    val amount_cl: String? = null): Parcelable
